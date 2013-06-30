package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class Claim {

	public static boolean claim(CommandSender sender, String[] args, Player player, String version, Connection c) {


		// check user permissions
		if (sender.hasPermission("moarTP.claim")) {

			// check number of arguments
			if (args.length < 1) {
				sender.sendMessage("Must enter a location name!");
				return false;
			}
			// check that any extra location info is enclosed in quotes
			if (args.length>1) {
				if (!(args[1].startsWith("\"") && args[args.length-1].endsWith("\""))) {
					sender.sendMessage("Location name or description not formatted correctly!");
					return false;
				} 
			}


			// ----- CLAIM ----- //

			try {
				PreparedStatement s = c.prepareStatement("select location from moarTP where location=?;");
				s.setString(1, args[0].toLowerCase());
				ResultSet rs = s.executeQuery();
				if (rs.next()) {
					player.sendMessage(args[0].toLowerCase()+" is already in the library!");
				} else {
					Location loc = player.getLocation();
					String creator = player.getDisplayName();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					String timeStamp = dateFormat.format(date);
					String description = null;
					if (args.length>1) {
						description = "";
						for (int i=1; i<args.length; ++i) {
							description += args[i] + ' ';
						}
						description = description.substring(1,description.length()-2 );
					}
					String template = "insert into moarTP (location, creationTime, creator, x, y, z, "
							+ "world, info, secret, version) values(?, ?, ?, ?, ?, ?, ?, ?, 'N', ?);";
					PreparedStatement insertion = c.prepareStatement(template);
					insertion.setString(1, args[0].toLowerCase());
					insertion.setInt(4, loc.getBlockX());
					insertion.setInt(5, loc.getBlockY());
					insertion.setInt(6, loc.getBlockZ());
					insertion.setString(7, loc.getWorld().getName());
					insertion.setString(9, version);
					insertion.setString(3, creator);
					insertion.setString(2, timeStamp);
					if (description!=null) {
						insertion.setString(8, description);
					} else {
						insertion.setNull(8, 12);
					}
					insertion.executeUpdate();
					player.sendMessage(args[0]+" successfully saved to library.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// ----- END CLAIM ----- //
		}

		else {
			// if user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;	
		}
	}
}
