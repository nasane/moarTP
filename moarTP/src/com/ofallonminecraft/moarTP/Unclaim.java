package com.ofallonminecraft.moarTP;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

public class Unclaim {

	public static boolean unclaim(CommandSender sender, String[] args) {

		// TODO: make people homeless if their home is unclaimed!
		// TODO: handle secret locations
		
		// load locs and info files
		Map<String, List<String>> creators  = null;
		Map<String, MTLocation>   locations = null;
		Map<String, String>       info      = null;
		try {
			creators  = SLAPI.load("plugins/moarTP/moarTP_creators.bin");
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			info      = SLAPI.load("plugins/moarTP/moarTP_info.bin");
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
		if (creators.get(sender.toString()) != null) {
			isCreator = (creators.get(sender.toString()).contains(args[0].toLowerCase()));
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
				// update creator info
				Set<String> creatorSet = creators.keySet();
				boolean done = false;
				for (String creator : creatorSet) {
					if (creators.get(creator)!=null) {  // in the bizarre occurence that this changes during this call's execution
						List<String> creatorLocs = creators.get(creator);
						for (String loc : creatorLocs) {
							if (loc.equals(args[0].toLowerCase())) {
								creatorLocs.remove(loc);
								creators.remove(creator);
								creators.put(creator, creatorLocs);
								done = true;  // cut this off asap to counter O(n^2) a bit
							}
							if (done) break;
						}
					}
					if (done) break;
				}
				sender.sendMessage(args[0]+" was successfully deleted from the library.");
			}

			// if the location doesn't exist in the hashmap, present an error message
			else {
				sender.sendMessage(args[0]+" either doesn't exist in the library or was made"
						+ " with an ancient version of the moarTP plugin!");
			}

			// close file streams
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info,      "plugins/moarTP/moarTP_info.bin");
				SLAPI.save(creators,  "plugins/moarTP/moarTP_creators.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
			// ----- END UNCLAIM ----- //
		}

		// close file streams regardless
		try {
			SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
			SLAPI.save(info,      "plugins/moarTP/moarTP_info.bin");
			SLAPI.save(creators,  "plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if the user doesn't have permission, present an error message
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
