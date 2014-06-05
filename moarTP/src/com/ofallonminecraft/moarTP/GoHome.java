package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class GoHome {

  public static boolean gohome(CommandSender sender, String[] args, Player player, Connection c) {

    // check user permissions
    if (sender.hasPermission("moarTP.gohome")) {
      // make sure there are no args
      if (args.length > 0) {
        sender.sendMessage("This command doesn't take any arguments!");
        return false;
      }

      // ----- GOHOME ----- //
      try {
        PreparedStatement s = c.prepareStatement("select home,x,y,z,world,location from moarTP where home LIKE ?;");
        s.setString(1, "%"+player.getUniqueId().toString()+"%");
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
          boolean playerVerified = false;
          boolean hasNext = true;
          while (!playerVerified && hasNext) {
            String[] playerList = rs.getString(1).split(",");
            for (String person : playerList) {
              if (person.equals(player.getUniqueId().toString())) {
                playerVerified = true;
                player.teleport(new Location(Bukkit.getServer().getWorld(rs.getString(5)),
                    rs.getInt(2),rs.getInt(3),rs.getInt(4)));
                player.sendMessage("Successfully teleported to "+rs.getString(6) + '.');
              }
            }
            hasNext = rs.next();
          }
          if (!playerVerified) sender.sendMessage("You are homeless! Set a home with /sethome [location].");
        } else {
          sender.sendMessage("You are homeless! Set a home with /sethome [location].");
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- GOHOME ----- //

    } else {
      sender.sendMessage("You don't have permission to do this!");
      return false;
    }
  }
}
