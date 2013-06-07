package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class GoHome {

	public static boolean gohome(CommandSender sender, String[] args, Player player) {

		// open locs and homes files
		Map<String, MTLocation> locations = null;
		Map<String, String>     homes     = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			homes     = SLAPI.load("plugins/moarTP/moarTP_homes.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check user permissions
		if (sender.hasPermission("moarTP.gohome")) {
			// make sure there are no args
			if (args.length > 0) {
				sender.sendMessage("This command doesn't take any arguments!");
				return false;
			}

			// ----- GOHOME ----- //

			// check that user has declared a home
			if (homes.containsKey(player.getName())) {
				// check that user's home still exists
				if (locations.containsKey(homes.get(player.getName()))) {
					// teleport user home
					MTLocation toGoTo  = locations.get(homes.get(player.getName()));
					Location   toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), 
							toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
					player.teleport(toGoTo2);
					player.sendMessage("Successfully teleported to "+homes.get(player.getName())
							+ '.');
				} else {
					sender.sendMessage("It appears that your home was deleted from the location"
							+ " library.  Set a new one!");					
				}
			} else {
				sender.sendMessage("You are homeless! Set a home with /sethome [location].");
			}

			// close file streams
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(homes,     "plugins/moarTP/moarTP_homes.bin");
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
