package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import com.ofallonminecraft.SpellChecker.SpellChecker;

public class Tpto {

	public static boolean tpto(CommandSender sender, String[] args, Player player, Connection c) {

		// check user permissions
		if (sender.hasPermission("moarTP.tpto")) {

			// check number of arguments
			if (args.length > 2) {
				sender.sendMessage("Choose ONE location!");
				return false;
			}
			if (args.length < 1) {
				sender.sendMessage("Must choose a location!");
				return false;
			}

			// ----- TPTO ----- //
			try {
				PreparedStatement s = c.prepareStatement("select x,y,z,world,secret from moarTP where location=?;");
				s.setString(1, args[0].toLowerCase());
				ResultSet rs = s.executeQuery();
				if (!rs.next()) {
					sender.sendMessage(args[0].toLowerCase()+" is not in the library!");
					HashSet<String> dict_subs = new HashSet<String>();
					dict_subs.add(SpellChecker.LOCATIONS);
					String sug = new SpellChecker(c, dict_subs).getSuggestion(args[0].toLowerCase());
					if (sug != null) {
						sender.sendMessage("Did you mean \"/tpto " + sug
								+ (args.length>1 ?  " " + args[1].toLowerCase() : "") + "\"?");
					}
				} else {
					if (rs.getString(5).equals("Y")) {
						if (args.length<2) {
							player.sendMessage(args[0].toLowerCase()+" is secret! A password is required for access.");
							return false;
						} else {
							s = c.prepareStatement("select encryptedLocation,hashedPass from moarTP where location=?;");
							s.setString(1, args[0].toLowerCase());
							rs = s.executeQuery();
							rs.next();
							String encryptedLoc = rs.getString(1);
							String passwordHash = rs.getString(2);

							boolean validated = false;
							String[] decryptedLocation = null;
							try {
								validated = PasswordHash.validatePassword(args[1], passwordHash);
								if (validated) {
									decryptedLocation = SimpleCrypto.decrypt(args[1], encryptedLoc).split(",");
									Location toGoTo = new Location(
											Bukkit.getServer().getWorld(decryptedLocation[0]),
											Integer.parseInt(decryptedLocation[1]),
											Integer.parseInt(decryptedLocation[2]),
											Integer.parseInt(decryptedLocation[3]));
									player.teleport(toGoTo);
									player.sendMessage("Successfully teleported to "+args[0].toLowerCase()+'.');
									return true;
								} else {
									player.sendMessage("Password could not be verified.");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else {
						if (args.length>1) {
							player.sendMessage("Choose ONE location!");
							return false;
						} else {
							Location toGoTo = new Location(Bukkit.getServer().getWorld(rs.getString(4)),
									rs.getInt(1),rs.getInt(2),rs.getInt(3));
							player.teleport(toGoTo);
							player.sendMessage("Successfully teleported to "+args[0].toLowerCase()+'.');
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// ----- END TPTO ----- //
		}
		// if user doesn't have permission, present an error message
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
