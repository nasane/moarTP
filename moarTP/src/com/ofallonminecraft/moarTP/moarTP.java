package com.ofallonminecraft.moarTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.commons.lang.StringUtils;

public class moarTP extends JavaPlugin
{

	// initialize hashmap to store locations
	public Map<String, MTLocation> locations = new HashMap<String, MTLocation>();

	public void onEnable() // on enable, load the location file
	{
		try
		{
			// if file exists, load it
			if (new File("plugins/moarTP/moarTP_locs.bin").exists()) {
				locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");	
			} else {
				// if the file doesn't exist, create and initialize it
				new File("plugins/moarTP").mkdir();
				new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			}
			getLogger().info("moarTP has been enabled");
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	public void onDisable() // on disable, save the location file
	{
		try
		{
			SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
			getLogger().info("moarTP has been disabled.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		// Unclaim (doesn't require user to be a player)
		if (cmd.getName().equalsIgnoreCase("unclaim")) 
		{

			// check user permissions
			if (sender.hasPermission("moarTP.unclaim")) 
			{

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
					sender.sendMessage("Location successfully deleted from the library.");
				}

				// if the location doesn't exist in the hashmap, present an error message
				else 
				{
					sender.sendMessage("That location doesn't exist in the library!");
				}
				return true;

				// ----- END UNCLAIM ----- //
			}

			// if the user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}


		// View (doesn't require user to be a player)
		if ((cmd.getName().equalsIgnoreCase("view")) && (sender.hasPermission("moarTP.view")))
		{

			// check number of arguments
			if (args.length > 0) 
			{
				sender.sendMessage("This command doesn't take arguments.");
				return false;
			}


			// ----- VIEW ----- //

			Set<String> viewLocs = locations.keySet(); // set of locations
			List<String> sortedLocs = new ArrayList<String>(viewLocs);
			Collections.sort(sortedLocs);
			Iterator<String> i = sortedLocs.iterator();  // iterator on set of locs
			int numPerLine = (viewLocs.size()/10);       // number of locs to be displayed per line minus one
			int maxLength = 0;
			while (i.hasNext()) {                        // find the maximum length of an entry
				int localLength = i.next().length();
				if (localLength>maxLength) maxLength=localLength;
			}
			int columnSpace = maxLength+3;   // determine column size
			i = sortedLocs.iterator();       // reinitialize iterator
			while (i.hasNext()) {
				String toView = i.next();           // add one location to the output string
				toView += StringUtils.repeat(" ",(columnSpace-toView.length()));   // adjust for column space
				for (int j=0; j<numPerLine; j++){   // append the rest of the line to the output string
					if (i.hasNext()) toView += StringUtils.repeat(" ",(columnSpace*(j+1)-toView.length()))+i.next();  // adjust for column space
				}
				sender.sendMessage(toView);  // output the string
			}
			return true;

			// ----- END VIEW ----- //

		}


		// check if user is a player
		if (sender instanceof Player) 
		{
			Player player = (Player)sender;


			// Goto
			if (cmd.getName().equalsIgnoreCase("goto"))
			{

				// check user permissions
				if (sender.hasPermission("moarTP.goto"))
				{

					// check number of arguments
					if (args.length > 1) {
						sender.sendMessage("Choose ONE location!");
						return false;
					}
					if (args.length < 1) {
						sender.sendMessage("Must choose a location!");
						return false;
					}


					// ----- GOTO ----- //

					if (locations.containsKey(args[0].toLowerCase()))
					{
						MTLocation toGoTo = locations.get(args[0].toLowerCase());
						Location toGoTo2 = new Location(player.getWorld(), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
						player.teleport(toGoTo2);
						player.sendMessage("Successfully teleported to "+args[0]+'.');
					}
					else
					{
						player.sendMessage("Location not in library!");
					}

					return true;

					// ----- END GOTO ----- //
				}

				// if user doesn't have permission, present an error message
				sender.sendMessage("You don't have permission to do this!");
				return false;
			}


			// Claim
			if (cmd.getName().equalsIgnoreCase("claim"))
			{
				// check user permissions
				if (sender.hasPermission("moarTP.claim"))
				{

					// check number of arguments
					if (args.length > 1) {
						sender.sendMessage("Location name must be one word!");
						return false;
					}
					if (args.length < 1) {
						sender.sendMessage("Must enter a location name!");
						return false;
					}


					// ----- CLAIM ----- //

					if (locations.containsKey(args[0].toLowerCase()))
					{
						player.sendMessage("That location is already in the library!");
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

						player.sendMessage("Location successfully saved to library.");
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


		// if user is not a player, present an error message
		sender.sendMessage("You must be a player!");
		return false;
	}
}