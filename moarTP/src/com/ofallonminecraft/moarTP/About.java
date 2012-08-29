package com.ofallonminecraft.moarTP;

import java.util.Map;
import org.bukkit.command.CommandSender;

public class About {

	public static boolean about(CommandSender sender, String[] args, Map<String, MTLocation> locations, Map<String, String> info) {

		if (sender.hasPermission("moarTP.about"))
		{
			if (args.length > 1)
			{
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length < 1)
			{
				sender.sendMessage("Not enough arguments!");
				return false;
			}

			// ----- ABOUT ----- //
			if (locations.containsKey(args[0].toLowerCase())) {
				if (info.containsKey(args[0].toLowerCase())) {
					// retrieve info and display to sender
					String locInfo = info.get(args[0].toLowerCase());
					sender.sendMessage(locInfo);
				} else {
					sender.sendMessage("Information for "+args[0]+" is unavailable. It appears that this location was created with an earlier version of the moarTP plugin.");
				}
			} else {
				sender.sendMessage(args[0] + " is not in the library!");
			}

			return true;
			// ----- END ABOUT ----- //
		}
		sender.sendMessage("You don't have permission to do this!");
		return false;

	}
}
