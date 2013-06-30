package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class Tpto {

	// TODO: add in support for secret locs and prompting for a password

	public static boolean tpto(CommandSender sender, String[] args, Player player, Connection c) {

		// check user permissions
		if (sender.hasPermission("moarTP.tpto")) {

			// check number of arguments
			if (args.length > 1) {
				sender.sendMessage("Choose ONE location!");
				return false;
			}
			if (args.length < 1) {
				sender.sendMessage("Must choose a location!");
				return false;
			}

			// ----- TPTO ----- //
			try {
				PreparedStatement s = c.prepareStatement("select x,y,z,world from moarTP where location=?;");
				s.setString(1, args[0].toLowerCase());
				ResultSet rs = s.executeQuery();
				if (!rs.next()) {
					sender.sendMessage(args[0].toLowerCase()+" is not in the library!");
				} else {
					Location toGoTo = new Location(Bukkit.getServer().getWorld(rs.getString(4)),
							rs.getInt(1),rs.getInt(2),rs.getInt(3));
					player.teleport(toGoTo);
					player.sendMessage("Successfully teleported to "+args[0].toLowerCase()+'.');
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
