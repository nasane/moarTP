package com.ofallonminecraft.moarTP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class moarTP extends JavaPlugin
{

	// initialize hashmap to store locations
	public Map<String, MTLocation> locations = new HashMap<String, MTLocation>();

	public void onEnable() // on enable, load the location file
	{
		try
		{
			locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			getLogger().info("moarTP has been enabled");
		}
		catch (Exception e) {
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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

			Set<String> viewLocs = locations.keySet();
			Iterator<String> i = viewLocs.iterator();
			while (i.hasNext())
				sender.sendMessage((String)i.next());
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
						player.sendMessage("Successfully teleported.");
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