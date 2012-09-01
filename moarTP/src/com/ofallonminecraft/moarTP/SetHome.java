package com.ofallonminecraft.moarTP;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHome {

	public static boolean sethome(CommandSender sender, String[] args, Player player) {
		if (sender.hasPermission("moarTP.sethome")) {

			
			Map<String, MTLocation> locations = null;
			Map<String, String> homes = null;
			try {
				locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
				homes = SLAPI.load("plugins/moarTP/moarTP_homes.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
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
			
			boolean overwrite = false;
			if (homes.containsKey(player.getDisplayName())) {
				overwrite = true;
			}
			if (locations.containsKey(args[0].toLowerCase())) {
				
				if (overwrite) {
					String oldHome = homes.get(player.getName());
					homes.remove(player.getName());
					homes.put(player.getName(), args[0].toLowerCase());
					sender.sendMessage("Old home ("+oldHome+") overwritten; new home set to "+args[0]); 
				}
				else {
					homes.put(player.getName(), args[0].toLowerCase());
					sender.sendMessage("New home set to "+args[0]+".");
				}
				
				try {
					SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				sender.sendMessage(args[0]+" could not be found in the library!  Choose an existing location to be your home.");
			}

			
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
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
}
