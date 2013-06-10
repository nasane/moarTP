package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHome {

	// TODO: handle secret locs; tell them they cannot set a secret loc as their home
	
	public static boolean sethome(CommandSender sender, String[] args, Player player) {
		if (sender.hasPermission("moarTP.sethome")) {

			// load locs and homes files
			Map<String, MTLocation> locations = null;
			Map<String, String>     homes     = null;
			try {
				locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
				homes     = SLAPI.load("plugins/moarTP/moarTP_homes.bin");
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

			// check if specified locations exists
			if (locations.containsKey(args[0].toLowerCase())) {
				if (homes.containsKey(player.getDisplayName())) {
					// overwrite player's old home
					String oldHome = homes.get(player.getName());
					homes.remove(player.getName());
					homes.put(player.getName(), args[0].toLowerCase());
					sender.sendMessage("Old home ("+oldHome+") overwritten; new home"
							+ " set to "+args[0]+"."); 
				} else {
					// assign player a home
					homes.put(player.getName(), args[0].toLowerCase());
					sender.sendMessage("New home set to "+args[0]+".");
				}
			} else {
				sender.sendMessage(args[0]+" could not be found in the library!"
						+ "  Choose an existing location to be your home.");
			}

			// save locs and homes files
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(homes,     "plugins/moarTP/moarTP_homes.bin");
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
