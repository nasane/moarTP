package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class Move {

	public static boolean move(CommandSender sender, String[] args, Map<String, MTLocation> locations) {

		// check user permissions
		if (sender.hasPermission("moarTP.unclaim")) 
		{

			// check number of arguments
			if (args.length > 2) 
			{
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length < 2) 
			{
				sender.sendMessage("Not enough arguments!");
				return false;
			}


			// ----- MOVE ----- //

			if (locations.containsKey(args[1].toLowerCase())) {
				MTLocation toGoTo = locations.get(args[1].toLowerCase());
				Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
				String playerToMove = "";
				for (int i=0; i <= args[0].length(); i++){
					char c = ',';
					if (i!=args[0].length()) c = args[0].charAt(i);      
					if (c!=',') {
						playerToMove += c;
					} else {
						if (Bukkit.getServer().getPlayer(playerToMove).isOnline()) {
							Bukkit.getServer().getPlayer(playerToMove).teleport(toGoTo2);
							sender.sendMessage("Successfully teleported " + playerToMove + " to "+args[1].toLowerCase()+'.');
							playerToMove = "";
						} else {
							sender.sendMessage(playerToMove+" could not be found on the server.");
						}
					}
				}	
			} else {
				sender.sendMessage(args[1] + " is not in the library!");
			}
			return true;

			// ----- END MOVE ----- //
		}

		// if the user doesn't have permission, present an error message
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
