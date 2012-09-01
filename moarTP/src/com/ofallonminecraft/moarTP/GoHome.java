package com.ofallonminecraft.moarTP;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoHome {

	public static boolean gohome(CommandSender sender, String[] args, Player player) {

		
		Map<String, MTLocation> locations = null;
		Map<String, String> homes = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			homes = SLAPI.load("plugins/moarTP/moarTP_homes.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		if (sender.hasPermission("moarTP.gohome")) {
			if (args.length > 0) {
				sender.sendMessage("This command doesn't take any arguments!");
				return false;
			}

			// ----- GOHOME ----- //
			if (homes.containsKey(player.getName())) {
				if (locations.containsKey(homes.get(player.getName()))) {
					MTLocation toGoTo = locations.get(homes.get(player.getName()));
					Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
					player.teleport(toGoTo2);
					player.sendMessage("Successfully teleported to "+homes.get(player.getName())+'.');
				} else {
					sender.sendMessage("It appears that your home was deleted from the location library.  Set a new one!");					
				}
			} else {
				sender.sendMessage("You are homeless! Set a home with /sethome [location].");
			}
			
			
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return true;
			// ----- GOHOME ----- //

		} else {
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}
	}

}
