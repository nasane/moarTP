package com.ofallonminecraft.moarTP;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoHome {

	public static boolean gohome(CommandSender sender, String[] args, Map<String, MTLocation> locations, Map<String, String> homes, Player player) {

		if (sender.hasPermission("moarTP.gohome")) {
			if (args.length > 0) {
				sender.sendMessage("This command doesn't take any arguments!");
				return false;
			}

			// ----- GOHOME ----- //
			if (homes.containsKey(player.getDisplayName())) {
				if (locations.containsKey(homes.get(player.getDisplayName()))) {
					MTLocation toGoTo = locations.get(homes.get(player.getDisplayName()));
					Location toGoTo2 = new Location(Bukkit.getServer().getWorld(toGoTo.world), toGoTo.getBlockX(), toGoTo.getBlockY(), toGoTo.getBlockZ());
					player.teleport(toGoTo2);
					player.sendMessage("Successfully teleported to "+homes.get(player.getDisplayName())+'.');
				} else {
					sender.sendMessage("It appears that your home was deleted from the location library.  Set a new one!");					
				}
			} else {
				sender.sendMessage("You are homeless! Set a home with /sethome [location]");
			}
			return true;
			// ----- GOHOME ----- //

		} else {
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}
	}

}
