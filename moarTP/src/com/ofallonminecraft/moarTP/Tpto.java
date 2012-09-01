package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpto {
	
	public static boolean tpto(CommandSender sender, String[] args, Player player) {
		
		
		Map<String, MTLocation> locations = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		// check user permissions
		if (sender.hasPermission("moarTP.tpto"))
		{

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

			if (locations.containsKey(args[0].toLowerCase()))
			{
				MTLocation toGoTo = locations.get(args[0].toLowerCase());
				Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
				player.teleport(toGoTo2);
				player.sendMessage("Successfully teleported to "+args[0]+'.');
			}
			else
			{
				player.sendMessage(args[0]+" is not in the library!");
			}

			
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
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
