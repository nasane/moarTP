package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.Location;

public class Move {

	public static boolean move(CommandSender sender, String[] args) {

		// load locations file
		Map<String, MTLocation> locations = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check user permissions
		if (sender.hasPermission("moarTP.move")) {

			// check number of arguments
			if (args.length > 2) {
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage("Not enough arguments!");
				return false;
			}


			// ----- MOVE ----- //

			// check that location exists
			if (locations.containsKey(args[1].toLowerCase())) {
				// get location
				MTLocation toGoTo  = locations.get(args[1].toLowerCase());
				Location   toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), 
						toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());

				// move each comma-separated player to the location
				String[] playersToMove = args[0].split(",");
				for (String playerToMove : playersToMove) {
					if (Bukkit.getServer().getPlayer(playerToMove)!=null && 
							Bukkit.getServer().getPlayer(playerToMove).isOnline()) {
						Bukkit.getServer().getPlayer(playerToMove).teleport(toGoTo2);
						sender.sendMessage("Successfully teleported " + playerToMove
								+ " to "+args[1].toLowerCase()+'.');
					} else {
						sender.sendMessage(playerToMove+" could not be found on the server.");
					}
				}
			} else {
				sender.sendMessage(args[1] + " is not in the library!");
			}

			// close file stream
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
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
