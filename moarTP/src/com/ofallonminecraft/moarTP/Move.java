package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.Location;

public class Move {

	public static boolean move(CommandSender sender, String[] args, Connection c) {

		// check user permissions
		if (sender.hasPermission("moarTP.move")) {

			// check number of arguments
			if (args.length > 3) {
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage("Not enough arguments!");
				return false;
			}

			// ----- MOVE ----- //
			try {
				PreparedStatement s = c.prepareStatement("select x,y,z,world,secret from moarTP where location=?;");
				s.setString(1, args[1].toLowerCase());
				ResultSet rs = s.executeQuery();
				if (!rs.next()) sender.sendMessage(args[1].toLowerCase()+" is not in the library!");
				else {
					Location toGoTo = null;
					if (rs.getString(4).equals("Y")) {
						if (args.length<3) {
							sender.sendMessage(args[1].toLowerCase()+" is secret! A password is required for access.");
							return false;
						} else {
							s = c.prepareStatement("select encryptedLocation,hashedPass from moarTP where location=?;");
							s.setString(1, args[1].toLowerCase());
							rs = s.executeQuery();
							rs.next();
							String encryptedLoc = rs.getString(1);
							String passwordHash = rs.getString(2);
							boolean validated = false;
							String[] decryptedLocation = null;
							try {
								validated = PasswordHash.validatePassword(args[2], passwordHash);
								if (validated) {
									decryptedLocation = SimpleCrypto.decrypt(args[2], encryptedLoc).split(",");
									toGoTo = new Location(
											Bukkit.getServer().getWorld(decryptedLocation[0]),
											Integer.parseInt(decryptedLocation[1]),
											Integer.parseInt(decryptedLocation[2]),
											Integer.parseInt(decryptedLocation[3]));
								} else {
									sender.sendMessage("Password could not be verified.");
									return false;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						toGoTo = new Location(Bukkit.getServer().getWorld(rs.getString(4)),
								rs.getInt(1),rs.getInt(2),rs.getInt(3));
					}
					String[] playersToMove = args[0].split(",");
					for (String playerToMove : playersToMove) {
						if (Bukkit.getServer().getPlayer(playerToMove)!=null &&
								Bukkit.getServer().getPlayer(playerToMove).isOnline()) {
							Bukkit.getServer().getPlayer(playerToMove).teleport(toGoTo);
							sender.sendMessage("Successfully teleported " + playerToMove
									+ " to " + args[1].toLowerCase()+'.');
						} else sender.sendMessage(playerToMove + " could not be found on the server.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// ----- END MOVE ----- //
		}

		// if the user doesn't have permission, present an error message
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
