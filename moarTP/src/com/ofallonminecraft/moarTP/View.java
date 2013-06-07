package com.ofallonminecraft.moarTP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class View {

	public static boolean view(CommandSender sender, String[] args) {

		// load necessary files
		Map<String, MTLocation>   locations = null;
		Map<String, List<String>> creators  = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			creators  = SLAPI.load("plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check user permissions
		if (sender.hasPermission("moarTP.view")) {
			// check number of arguments
			if (args.length > 1) {
				sender.sendMessage("Please choose one player name.");
				return false;
			}

			// ----- VIEW ----- //
			// TODO: Refactor this section so it doesn't look and function like poop
			if (args.length == 1) {
				String creator = args[0];
				if (creators.get(creator)!=null) {
					List<String> sortedLocs = creators.get(creator);
					Collections.sort(sortedLocs);	
					Iterator<String> i = sortedLocs.iterator();
					String toView = i.next();	
					while (i.hasNext()) {
						toView += ", " + i.next();
					}
					sender.sendMessage(toView);
				} else {
					sender.sendMessage("Sorry, there aren't any locations claimed by "+args[0] +
							". Double check: names are case sensitive!");
				}
			} else {
				Set<String>  viewLocs   = locations.keySet();  // set of locations
				List<String> sortedLocs = new ArrayList<String>(viewLocs);  // list of locs
				if (sortedLocs.size()>0) {
					// display all locations to player			
					Collections.sort(sortedLocs);
					Iterator<String> i = sortedLocs.iterator();
					String toView = i.next();
					while (i.hasNext()) {
						toView += ", " + i.next();
					}
					sender.sendMessage(toView);
				} else {
					sender.sendMessage("No locations claimed yet. Claim one with /claim <location>");
				}
			}

			// close file streams
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(creators,  "plugins/moarTP/moarTP_creators.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
			// ----- END VIEW ----- //

		}

		// close file streams regardless
		try {
			SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
			SLAPI.save(creators,  "plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if, for whatever reason, the player doesn't have permission
		sender.sendMessage("You don't have permission to do this!");
		return false;
	}
}
