package com.ofallonminecraft.moarTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.commons.lang.StringUtils;

public class moarTP extends JavaPlugin
{

	// initialize hashmaps to store locations and descriptions
	public Map<String, MTLocation> locations = new HashMap<String, MTLocation>();
	public Map<String, String> descriptions = new HashMap<String, String>();
	public Map<String, String> claimRecords = new HashMap<String, String>();

	public void onEnable() // on enable, load the location file and description file
	{
		try
		{
			if (new File("plugins/moarTP/").exists()) {
				if (new File("plugins/moarTP/moarTP_locs.bin").exists()) {
					locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
				} else {
					new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
					SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				}
				if (new File("plugins/moarTP/moarTP_des.bin").exists()) {
					descriptions = SLAPI.load("plugins/moarTP/moarTP_des.bin");
				} else {
					new File("plugin/moarTP/moarTP_des.bin").createNewFile();
					SLAPI.save(descriptions, "plugins/moarTP/moarTP_des.bin");
				}
				if (new File("plugins/moarTP/moarTP_claimRecs.bin").exists()) {
					claimRecords = SLAPI.load("plugins/moarTP/moarTP_claimRecs.bin");
				} else {
					new File("plugin/moarTP/moarTP_claimRecs.bin").createNewFile();
					SLAPI.save(claimRecords, "plugins/moarTP/moarTP_claimRecs.bin");
				}
			} else {
				new File("plugins/moarTP").mkdir();
				new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
				new File("plugins/moarTP/moarTP_des.bin").createNewFile();
				new File("plugins/moarTP/moarTP_claimRecs.bin").createNewFile();
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(descriptions, "plugins/moarTP/moarTP_des.bin");
				SLAPI.save(claimRecords, "plugins/moarTP/moarTP_claimRecs.bin");
			}
			getLogger().info("moarTP has been enabled");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() // on disable, save the files
	{
		try
		{
			SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
			SLAPI.save(descriptions, "plugins/moarTP/moarTP_des.bin");
			getLogger().info("moarTP has been disabled.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		
		
		
		// Describe (doesn't require user to be a player)
		if (cmd.getName().equalsIgnoreCase("describe")) {
			if (sender.hasPermission("moarTP.describe")) {
				if (args.length > 1) {
					sender.sendMessage("Too many arguments!");
					return false;
				}
				if (args.length < 1) {
					sender.sendMessage("Not enough arguments!");
					return false;
				}
				
				
				// ----- DESCRIBE ----- //
				/* IN PROGRESS
				if (descriptions.containsKey(args[0])) {
					if (!claimRecords.containsKey(args[0])) {
						String accept;
						sender.sendMessage("No claimer information could be found.  If you would like to add a description, the location will be assigned to you.  Would you like to continue? (y/n): ");
						
					}
				}
				
				return true;
				*/
				
				// ----- END DESCRIBE ----- //
				
			}
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}
		
		
		
		

		// Move (doesn't require user to be a player)
		if (cmd.getName().equalsIgnoreCase("move")) 
		{

			// check user permissions
			if (sender.hasPermission("moarTP.unclaim")) 
			{

				// check number of arguments
				if (args.length > 2) 
				{
					sender.sendMessage("Too many arguments!");
					return false;
				}
				if (args.length < 2) 
				{
					sender.sendMessage("Not enough arguments!");
					return false;
				}


				// ----- MOVE ----- //

				if (locations.containsKey(args[1].toLowerCase())) {
					MTLocation toGoTo = locations.get(args[1].toLowerCase());
					Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
					String playerToMove = null;
					for (int i=0; i <= args[0].length(); i++){
						char c = args[0].charAt(i);      
						if (c!=',' && i!=args[0].length()) {
							playerToMove += c;
						} else {
							if (Bukkit.getServer().getPlayer(playerToMove).isOnline()) {
								Bukkit.getServer().getPlayer(playerToMove).teleport(toGoTo2);
								sender.sendMessage("Successfully teleported " + playerToMove + " to "+args[1]+'.');
								playerToMove = null;
							} else {
								sender.sendMessage(playerToMove+" could not be found on the server.");
							}
						}
					}	
				} else {
					sender.sendMessage(args[1] + " is not in the library!");
				}
				return true;

				// ----- END MOVE ----- //
			}

			// if the user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}

		
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
					sender.sendMessage(args[0]+" was successfully deleted from the library.");
				}

				// if the location doesn't exist in the hashmap, present an error message
				else 
				{
					sender.sendMessage(args[0]+" doesn't exist in the library!");
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

			Set<String> viewLocs = locations.keySet();   // set of locations
			List<String> sortedLocs = new ArrayList<String>(viewLocs);  // create a list of the locations
			Collections.sort(sortedLocs);                // sort the list
			Iterator<String> i = sortedLocs.iterator();  // iterator on list of locs
			int numPerLine = ((viewLocs.size()-1)/10);   // number of locs to be displayed per line minus one

			int maxLength = 0;
			while (i.hasNext()) {                        // find the maximum length of an entry
				int localLength = i.next().length();
				if (localLength>maxLength) maxLength=localLength;
			}
			int columnSpace = maxLength+3;               // determine column size

			i = sortedLocs.iterator();                   // reinitialize iterator
			while (i.hasNext()) {
				// add one location to the output string
				String toView = i.next();
				// adjust for column space
				toView += StringUtils.repeat(" ",(columnSpace-toView.length()));
				// append the rest of the line to the output string
				for (int j=0; j<numPerLine; j++){ 
					// adjust for column space
					if (i.hasNext()) toView += StringUtils.repeat(" ",(columnSpace*(j+1)-toView.length()))+i.next();
				}
				// output the string
				sender.sendMessage(toView);
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
						Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
						player.teleport(toGoTo2);
						player.sendMessage("Successfully teleported to "+args[0]+'.');
					}
					else
					{
						player.sendMessage(args[0]+" is not in the library!");
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


		// if user is not a player, present an error message
		sender.sendMessage("You must be a player!");
		return false;
	}
}