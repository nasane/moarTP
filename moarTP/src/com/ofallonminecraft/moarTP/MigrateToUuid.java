package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;

public class MigrateToUuid {

  @SuppressWarnings("deprecation")
  public static boolean migrateToUuid(Connection c) {

    // fix creators
    Set<String> stringPlayers = new HashSet<String>();
    try {
      PreparedStatement s = c.prepareStatement("select distinct creator from moarTP;");
      ResultSet rs = s.executeQuery();
      while (rs.next()) {
        if (rs.getString(1) != null && rs.getString(1) != "") {
          stringPlayers.add(rs.getString(1));
        }
      }
      rs.close();
      for (String stringPlayer : stringPlayers) {
        PreparedStatement ps = c.prepareStatement("update moarTP set creator=? where creator=?;");
        // TODO: find another way (getOfflinePlayer is deprecated)
        ps.setString(1, Bukkit.getServer().getOfflinePlayer(stringPlayer).getUniqueId().toString());
        ps.setString(2, stringPlayer);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    // fix homes
    try {
      PreparedStatement s = c.prepareStatement("select home,location from moarTP;");
      ResultSet rs = s.executeQuery();
      while (rs.next()) {
        if (rs.getString(1)!=null && !rs.getString(1).equals("")) {
          String[] playerList = rs.getString(1).split(",");
          String newPlayerList = "";
          for (String person : playerList) {
            if (newPlayerList.equals("")) {
              // TODO: find another way (getOfflinePlayer is deprecated)
              newPlayerList += Bukkit.getServer().getOfflinePlayer(person).getUniqueId().toString();
            } else {
              newPlayerList += "," + Bukkit.getServer().getOfflinePlayer(person).getUniqueId().toString();
            }
          }
          s = c.prepareStatement("update moarTP set home=? where location=?");
          s.setString(1, newPlayerList);
          s.setString(2, rs.getString(2));
          s.executeUpdate();
        }
      }
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

}
