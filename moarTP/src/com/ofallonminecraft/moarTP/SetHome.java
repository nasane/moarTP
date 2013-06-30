package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHome {

	// TODO: handle secret locs; tell them they cannot set a secret loc as their home
	// TODO: fix result set closure issues

	public static boolean sethome(CommandSender sender, String[] args, Player player, Connection c) {
		if (sender.hasPermission("moarTP.sethome")) {

			// check number of arguments
			if (args.length < 1) {
				sender.sendMessage("Must enter a location name!");
				return false;
			}
			if (args.length > 1) {
				sender.sendMessage("You can only choose one home!");
				return false;
			}

			// ----- SETHOME ----- //
			try {
				PreparedStatement s = c.prepareStatement("select home from moarTP where location=?;");
				s.setString(1, args[0].toLowerCase());
				ResultSet rs = s.executeQuery();
				if (!rs.next()) {
					sender.sendMessage(args[0].toLowerCase()+" could not be found in the library!"
							+ "  Choose an existing location to be your home.");
				} else {
					String oldHomeList = rs.getString(1);
					PreparedStatement s2 = c.prepareStatement("select home,location from moarTP where home LIKE ?;");
					s2.setString(1, "%"+player.getDisplayName()+"%");
					ResultSet rs2 = s.executeQuery();
					if (rs2.next()) {
						boolean playerVerified = false;
						boolean hasNext = true;
						while (!playerVerified && hasNext) {
							String[] playerList = rs2.getString(1).split(",");
							for (String person : playerList) {
								if (person.equals(player.getDisplayName())) {
									playerVerified = true;

									// clear player's old home
									String newPlayerList = "";
									for (String person2 : playerList) {
										if (!person2.equals(player.getDisplayName())) {
											if (!newPlayerList.equals("")) newPlayerList += ","+person2;
											else newPlayerList = person2;
										}
									}
									String template = "update moarTP set home = ? where location = ?;";
									PreparedStatement update = c.prepareStatement(template);
									if (newPlayerList.equals("")) update.setNull(1, 12);
									else update.setString(1, newPlayerList);
									update.setString(2, rs2.getString(2));
									update.executeUpdate();

									// add player home
									addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());

									sender.sendMessage("Old home ("+rs2.getString(2)+") overwritten; new home"
											+ " set to "+args[0].toLowerCase()+".");
								}
							}
							hasNext = rs.next();
						}
						if (!playerVerified) {
							// add player home
							addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());
						}
					} else {
						// add player home
						addPlayerHome(player, oldHomeList, c, args[0].toLowerCase());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// ----- END SETHOME ----- //

		} else {
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}
	}

	public static void addPlayerHome(Player player, String oldPlayerList, Connection c, String newHome) {
		String newPlayerList = "";
		if (oldPlayerList==null || oldPlayerList.equals("null")) newPlayerList = player.getDisplayName();
		else newPlayerList = oldPlayerList+","+player.getDisplayName();
		String template = "update moarTP set home = ? where location = ?;";
		PreparedStatement update;
		try {
			update = c.prepareStatement(template);
			update.setString(1, newPlayerList);
			update.setString(2, newHome);
			update.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.sendMessage("New home set to "+newHome+".");
	}

}
