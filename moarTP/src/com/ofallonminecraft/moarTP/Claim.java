package com.ofallonminecraft.moarTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class Claim {

	public static boolean claim(CommandSender sender, String[] args, Player player) {

		// open file of locs and associated location info
		Map<String, MTLocation> locations = null;
		Map<String, String>     info      = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			info      = SLAPI.load("plugins/moarTP/moarTP_info.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}


		// check user permissions
		if (sender.hasPermission("moarTP.claim")) {

			// check number of arguments
			if (args.length < 1) {
				sender.sendMessage("Must enter a location name!");
				return false;
			}
			// check that any extra location info is enclosed in quotes
			if (args.length>1) {
				if (!(args[1].startsWith("\"") && args[args.length-1].endsWith("\""))) {
					sender.sendMessage("Location name or description not formatted correctly!");
					return false;
				} 
			}


			// ----- CLAIM ----- //

			// check that the location name isn't taken
			if (locations.containsKey(args[0].toLowerCase())) {
				player.sendMessage(args[0]+" is already in the library!");
			}
			else {
				// save the location
				Location loc = player.getLocation();
				MTLocation toSave = MTLocation.getMTLocationFromLocation(loc);
				locations.put(args[0].toLowerCase(), toSave);
				try {
					SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
				Date date = new Date();
				String timeStamp = dateFormat.format(date);
				String locInfo = "Created by "+player.getDisplayName()+" on "+timeStamp+".";

				// format the description
				if (args.length>1) {
					String description = "";
					for (int i=1; i<args.length; ++i) {
						description += args[i] + ' ';
					}
					description = description.substring(1,description.length()-2 );
					info.put(args[0].toLowerCase(), description + "\n" + locInfo);
				} else {
					info.put(args[0].toLowerCase(), locInfo);
				}

				// save the location info
				try {
					SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// TODO: save creator info
				player.sendMessage(args[0]+" successfully saved to library.");
			}

			// save the files
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;

			// ----- END CLAIM ----- //
		}

		else {
			// if user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;	
		}
	}
}
