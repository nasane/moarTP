package com.ofallonminecraft.moarTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Claim {
	
	public static boolean claim(CommandSender sender, String[] args, Map<String, MTLocation> locations, Map<String, String> info, Player player) {
		
		// check user permissions
		if (sender.hasPermission("moarTP.claim"))
		{

			// check number of arguments
			if (args.length < 1) {
				sender.sendMessage("Must enter a location name!");
				return false;
			}
			int numArguments = args.length;
			if (numArguments>1) {
				char quote = '"';
				String quoteString = "";
				quoteString += quote;
				if (!(args[1].startsWith(quoteString) && args[numArguments-1].endsWith(quoteString))) {
					sender.sendMessage("Location name or description not formatted correctly!");
					return false;
				} 
			}




			// ----- CLAIM ----- //

			if (locations.containsKey(args[0].toLowerCase()))
			{
				player.sendMessage(args[0]+" is already in the library!");
			}
			else
			{
				Location loc = player.getLocation();
				MTLocation toSave = MTLocation.getMTLocationFromLocation(loc);
				locations.put(args[0].toLowerCase(), toSave);
				try
				{
					SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
				Date date = new Date();
				String timeStamp = dateFormat.format(date);
				String locInfo = "Created by "+player.getDisplayName()+" on "+timeStamp+".";

				// format the description
				String description = "";
				for (int i=1; i<args.length; ++i) {
					description += args[i] + ' ';
				}
				description = description.substring(1,description.length()-2 );

				info.put(args[0].toLowerCase(), description + "\n" + locInfo);
				try
				{
					SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				player.sendMessage(args[0]+" successfully saved to library.");
			}
			return true;

			// ----- END CLAIM ----- //
		}

		else 
		{
			// if user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;	
		}
		
		
		
	}

}
