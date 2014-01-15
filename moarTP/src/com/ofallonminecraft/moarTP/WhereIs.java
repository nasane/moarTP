package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.ofallonminecraft.SpellChecker.SpellChecker;

public class WhereIs {

	public static boolean whereIs(CommandSender sender, String[] args, Connection c) {

		// check user permissions
		if (sender.hasPermission("moarTP.whereis")) {
			// check number of arguments
			if (args.length != 1) {
				sender.sendMessage("Please choose one player name.");
				return false;
			}

			if (Bukkit.getServer().getPlayer(args[0])!=null && Bukkit.getServer().getPlayer(args[0]).isOnline()) {
				Location playerLoc = Bukkit.getServer().getPlayer(args[0]).getLocation();
				int x = playerLoc.getBlockX();
				int y = playerLoc.getBlockY();
				int z = playerLoc.getBlockZ();
				String closestLoc = "";
				double closestDistance = -1;
				try {
					PreparedStatement s = c.prepareStatement("select location,x,y,z from moarTP where secret='N';");
					ResultSet rs = s.executeQuery();
					while (rs.next()) {
						int xcomp = rs.getInt(2);
						int ycomp = rs.getInt(3);
						int zcomp = rs.getInt(4);
						double dist = getDistance(x, y, z, xcomp, ycomp, zcomp);
						if (closestDistance==-1 || dist<closestDistance) {
							closestDistance = dist;
							closestLoc = rs.getString(1);
						}
					}
					if (closestLoc.equals("") || closestDistance==-1) {
						sender.sendMessage("Sorry, no public locations were found!");
						return false;
					} else {
						sender.sendMessage(closestLoc);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(args[0] + " could not be found on the server.");
				SpellChecker sc = new SpellChecker(c);
				if (sc.getSuggestion(args[0]) != null) {
					sender.sendMessage("Did you mean \"/whereis " + sc.getSuggestion(args[0]) + "\"?");
				}
			}
			return true;
		} else {
			// if, for whatever reason, the player doesn't have permission
			sender.sendMessage("You don't have permission to do this!");
			return false;
		}
	}

	private static double getDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) + (z1-z2)*(z1-z2));
	}

}
