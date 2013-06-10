package com.ofallonminecraft.moarTP;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class moarTP extends JavaPlugin
{

	// ---------- INITIALIZE HASMAPS TO STORE LOCATIONS AND DESCRIPTIONS ---------- //
	public static Map<String, MTLocation>   locations  = new HashMap<String, MTLocation>();
	public static Map<String, String>       info       = new HashMap<String, String>();
	public static Map<String, String>       homes      = new HashMap<String, String>();
	public static Map<String, List<String>> creators   = new HashMap<String, List<String>>();
	public static Map<String, List<String>> secretLocs = new HashMap<String, List<String>>(); 
	public double                           version    = 0.60;
	// ---------- END INITIALIZE HASMAPS TO STORE LOCATIONS AND DESCRIPTIONS ---------- //



	// ---------- MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //

	// on enable, ensure correct directory structure and plugin files
	public void onEnable() {
		try {
			double oldVersion = 0.0;
			if (new File("plugins/moarTP/moarTP_version.bin").exists()) {
				oldVersion = SLAPI.load("plugins/moarTP/moarTP_version.bin");
				// opportunity to fix anything to provide backwards compatibility
				if (oldVersion<version) {}
				SLAPI.save(version, "plugins/moarTP/moarTP_version.bin");
			}
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
				boolean creatorsInitialized = false;
				if (!(new File("plugins/moarTP/moarTP_creators.bin").exists())) {
					new File("plugins/moarTP/moarTP_creators.bin").createNewFile();
					SLAPI.save(creators, "plugins/moarTP/moarTP_creators.bin");
					InitializeCreators.initializeCreators();
					creatorsInitialized = true;
				}
				if (!(new File("plugins/moarTP/moarTP_version.bin").exists())) {
					new File("plugins/moarTP/moarTP_version.bin").createNewFile();
					SLAPI.save(version, "plugins/moarTP/moarTP_version.bin");
					if (!creatorsInitialized) InitializeCreators.initializeCreators();
				}
				if (!(new File("plugins/moarTP/moarTP_secret.bin").exists())) {
					new File("plugin/moarTP/moarTP_secret.bin").createNewFile();
					SLAPI.save(secretLocs, "plugins/moarTP/moarTP_secret.bin");
				}
			} else {
				new File("plugins/moarTP").mkdir();
				new File("plugins/moarTP/moarTP_locs.bin").createNewFile();
				new File("plugins/moarTP/moarTP_info.bin").createNewFile();
				new File("plugins/moarTP/moarTP_homes.bin").createNewFile();
				new File("plugins/moarTP/moarTP_creators.bin").createNewFile();
				new File("plugins/moarTP/moarTP_version.bin").createNewFile();
				new File("plugins/moarTP/moarTP_secret.bin").createNewFile();
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				SLAPI.save(homes, "plugins/moarTP/moarTP_homes.bin");
				SLAPI.save(creators, "plugins/moarTP/moarTP_creators.bin");
				SLAPI.save(version, "plugins/moarTP/moarTP_version.bin");
				SLAPI.save(secretLocs, "plugins/moarTP/moarTP_secret.bin");
			}
			getLogger().info("moarTP has been enabled");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// on disable, print a message
	public void onDisable() {
		getLogger().info("moarTP hs been disabled.");
	}
	// ---------- END MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //



	// ---------- HANDLE THE COMMANDS ---------- //
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{

		// ----- Functions that do not require user to be a player ----- //

		if (cmd.getName().equalsIgnoreCase("about")) {
			return About.about(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("move")) {
			return Move.move(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("unclaim")) {
			return Unclaim.unclaim(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("view")) {
			return View.view(sender, args);
		} else


			// ----- Functions that do require the user to be a player ----- //

			// check if user is a player
			if (sender instanceof Player) {
				Player player = (Player) sender;

				// Tpto
				if (cmd.getName().equalsIgnoreCase("tpto")) {
					return Tpto.tpto(sender, args, player);
				} else if (cmd.getName().equalsIgnoreCase("claim")) {
					return Claim.claim(sender, args, player);
				} else if (cmd.getName().equalsIgnoreCase("sethome")) {
					return SetHome.sethome(sender, args, player);
				} else if (cmd.getName().equalsIgnoreCase("gohome")) {
					return GoHome.gohome(sender, args, player);
				} else if (cmd.getName().equalsIgnoreCase("claimsecret")) {
					return ClaimSecret.claimSecret(sender, args, player);
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