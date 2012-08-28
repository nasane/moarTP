package com.ofallonminecraft.moarTP;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.commons.lang.StringUtils;

public class moarTP extends JavaPlugin
{

	// initialize hashmaps to store locations and descriptions
	public Map<String, MTLocation> locations = new HashMap<String, MTLocation>();
	public Map<String, String> info = new HashMap<String, String>();

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
				if (new File("plugins/moarTP/moarTP_info.bin").exists()) {
					info = SLAPI.load("plugins/moarTP/moarTP_info.bin");
				} else {
					new File("plugins/moarTP/moarTP_info.bin").createNewFile();
					SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				}
			} else {
				new File("plugins/moarTP").mkdir();
				new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
				new File("plugins/moarTP/moarTP_info.bin").createNewFile();
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
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
			SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
			getLogger().info("moarTP has been disabled.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{



		// About (doesn't require user to be a player)
		if (cmd.getName().equalsIgnoreCase("about"))
		{
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
					String playerToMove = "";
					for (int i=0; i <= args[0].length(); i++){
						char c = ',';
						if (i!=args[0].length()) c = args[0].charAt(i);      
						if (c!=',') {
							playerToMove += c;
						} else {
							if (Bukkit.getServer().getPlayer(playerToMove).isOnline()) {
								Bukkit.getServer().getPlayer(playerToMove).teleport(toGoTo2);
								sender.sendMessage("Successfully teleported " + playerToMove + " to "+args[1].toLowerCase()+'.');
								playerToMove = "";
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


			// Tpto
			if (cmd.getName().equalsIgnoreCase("tpto"))
			{

				// check user permissions
				if (sender.hasPermission("moarTP.tpto"))
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


					// ----- TPTO ----- //

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

					// ----- END TPTO ----- //
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


		// if user is not a player, present an error message
		sender.sendMessage("You must be a player!");
		return false;
	}
}