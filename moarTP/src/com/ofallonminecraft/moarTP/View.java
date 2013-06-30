package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.command.CommandSender;

public class View {

	// TODO: display secret locs as well (just the names!)

	public static boolean view(CommandSender sender, String[] args, Connection c) {

		// check user permissions
		if (sender.hasPermission("moarTP.view")) {
			// check number of arguments
			if (args.length > 1) {
				sender.sendMessage("Please choose one player name.");
				return false;
			}

			// ----- VIEW ----- //
			try {
				ResultSet rs = null;
				PreparedStatement s = null;
				if (args.length == 1) {
					s = c.prepareStatement("select location from moarTP where creator=?;");
					s.setString(1, args[0]);
					rs = s.executeQuery();
				} else {
					rs = c.createStatement().executeQuery("select location from moarTP;");
				}
				if (rs.next()) {
					String toView = rs.getString(1);
					while (rs.next()) toView += ", " + rs.getString(1);
					sender.sendMessage(toView);
				} else {
					if (args.length == 1) {
						sender.sendMessage("Sorry, there aren't any locations claimed by "+args[0] +
								". Double check: names are case sensitive!");
					} else sender.sendMessage("No locations claimed yet. Claim one with /claim <location>");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// ----- END VIEW ----- //
		}
		// if, for whatever reason, the player doesn't have permission
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
