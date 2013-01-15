package com.ofallonminecraft.moarTP;

import java.util.Map;

import org.bukkit.command.CommandSender;

public class Unclaim {
	
	public static boolean unclaim(CommandSender sender, String[] args) {
		
		
		Map<String, MTLocation> locations = null;
		Map<String, String> info = null;
		try {
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			info = SLAPI.load("plugins/moarTP/moarTP_info.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// check number of arguments
		if (args.length > 1) 
		{
			sender.sendMessage("Location name must be one word!");
			return false;
		}
		if (args.length < 1) 
		{
			sender.sendMessage("Must choose a location to unclaim!");
			return false;
		}
		
		boolean isCreator = false;
		String s = sender.toString().substring(17, sender.toString().length()-1);
		String c = "  ";
		if (info.get(args[0].toLowerCase()) != null) {
			if (11+s.length()<=info.get(args[0].toLowerCase()).length()) {
				c = info.get(args[0].toLowerCase()).substring(11, 11+s.length());
			} else {
				sender.sendMessage("You don't have permission to do this!");
				return false;
			}
		} else {
			sender.sendMessage(args[0]+" doesn't either doesn't exist in the library or was made with an older version of the moarTP plugin!");
			return false;
		}
		if (c.substring(0,c.length()-2)==s) isCreator = true;
		if (c.endsWith(" ")) isCreator = false; 
		
		// check user permissions
					if (sender.isOp() || isCreator) 
					{

						// ----- UNCLAIM ----- //

						// if the location exists in the hashmap, remove it
						if (locations.containsKey(args[0].toLowerCase())) 
						{
							locations.remove(args[0].toLowerCase());
							try
							{
								SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							if (info.containsKey(args[0].toLowerCase())) {
								// delete info
								info.remove(args[0].toLowerCase());
								try
								{
									SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
							sender.sendMessage(args[0]+" was successfully deleted from the library.");
						}

						// if the location doesn't exist in the hashmap, present an error message
						else 
						{
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
