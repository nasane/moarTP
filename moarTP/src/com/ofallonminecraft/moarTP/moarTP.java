package com.ofallonminecraft.moarTP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class moarTP extends JavaPlugin
{

	
	
	// ---------- INITIALIZE HASMAPS TO STORE LOCATIONS AND DESCRIPTIONS ---------- //
	public static Map<String, MTLocation> locations = new HashMap<String, MTLocation>();
	public static Map<String, String>     info      = new HashMap<String, String>();
	public static Map<String, String>     homes     = new HashMap<String, String>();
	// ---------- END INITIALIZE HASMAPS TO STORE LOCATIONS AND DESCRIPTIONS ---------- //

	
	
	
	
	
	// ---------- MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //
	public void onEnable() // on enable, load the location file and description file
	{
		try
		{
			if (new File("plugins/moarTP/").exists()) {
				if (!(new File("plugins/moarTP/moarTP_locs.bin").exists())) {
					new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
					SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				}
				if (!(new File("plugins/moarTP/moarTP_info.bin").exists())) {
					new File("plugins/moarTP/moarTP_info.bin").createNewFile();
					SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				}
				if (!(new File("plugins/moarTP/moarTP_homes.bin").exists())) {
					new File("plugins/moarTP/moarTP_homes.bin").createNewFile();
					SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
				}
			} else {
				new File("plugins/moarTP").mkdir();
				new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
				new File("plugins/moarTP/moarTP_info.bin").createNewFile();
				new File("plugins/moarTP/moarTP_homes.bin").createNewFile();
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
			}
			getLogger().info("moarTP has been enabled");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() // on disable, save the files
	{
		getLogger().info("moarTP hs been disabled.");
	}
	// ---------- END MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //

	
	
	
	

	// ---------- HANDLE THE COMMANDS ---------- //
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		// ----- Functions that do not require user to be a player ----- //

		// About
		if (cmd.getName().equalsIgnoreCase("about")) {
			return About.about(sender, args);
		}

		// Move
		if (cmd.getName().equalsIgnoreCase("move")) {
			return Move.move(sender, args);
		}

		// Unclaim
		if (cmd.getName().equalsIgnoreCase("unclaim")) {
			return Unclaim.unclaim(sender, args);
		}

		// View
		if ((cmd.getName().equalsIgnoreCase("view")) && (sender.hasPermission("moarTP.view"))) {
			return View.view(sender, args);
		}


		// ----- Functions that do require the user to be a player ----- //

		// check if user is a player
		if (sender instanceof Player) {
			Player player = (Player)sender;

			// Tpto
			if (cmd.getName().equalsIgnoreCase("tpto")) {
				return Tpto.tpto(sender, args, player);
			}

			// Claim
			if (cmd.getName().equalsIgnoreCase("claim")) {
				return Claim.claim(sender, args, player);
			}
			
			// SetHome
			if (cmd.getName().equalsIgnoreCase("sethome")) {
				return SetHome.sethome(sender, args, player);
			}
			
			// GoHome
			if (cmd.getName().equalsIgnoreCase("gohome")) {
				return GoHome.gohome(sender, args, player);
			}
			
		} else {
			// if user is not a player, present an error message
			sender.sendMessage("You must be a player!");
			return false;
		}
		return false;
	}
	// --------- END HANDLE THE COMMANDS ---------- //
	
}