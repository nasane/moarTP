package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.command.CommandSender;

public class Unclaim {

	public static boolean unclaim(CommandSender sender, String[] args) {

		// load locs and info files
		Map<String, MTLocation> locations = null;
		Map<String, String>     info      = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			info = SLAPI.load("plugins/moarTP/moarTP_info.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check number of arguments
		if (args.length > 1) {
			sender.sendMessage("Location name must be one word!");
			return false;
		}
		if (args.length < 1) {
			sender.sendMessage("Must choose a location to unclaim!");
			return false;
		}

		// check if user is the creator of the location
		boolean isCreator = false;
		if (info.get(args[0].toLowerCase()) != null) {
			String   c   = info.get(args[0].toLowerCase());
			String   s   = sender.toString().substring(17,sender.toString().length()-1);
			String[] c_a = c.split("\\s");
			c = c_a[2];
			if (c.equals(s)) isCreator = true;
		} else {
			sender.sendMessage(args[0]+" either doesn't exist in the library or was made"
					+ " with an ancient version of the moarTP plugin!");
		}

		// check user permissions
		if (sender.isOp() || isCreator) {

			// ----- UNCLAIM ----- //

			// if the location exists in the hashmap, remove it
			if (locations.containsKey(args[0].toLowerCase())) {
				locations.remove(args[0].toLowerCase());
				if (info.containsKey(args[0].toLowerCase())) {
					// delete info
					info.remove(args[0].toLowerCase());
				}
				sender.sendMessage(args[0]+" was successfully deleted from the library.");
			}

			// if the location doesn't exist in the hashmap, present an error message
			else {
				sender.sendMessage(args[0]+" doesn't exist in the library!");
			}


			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
			// ----- END UNCLAIM ----- //
		}

		// if the user doesn't have permission, present an error message
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
