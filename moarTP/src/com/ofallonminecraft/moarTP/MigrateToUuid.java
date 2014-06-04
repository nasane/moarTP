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

    // TODO: fix homes!
    
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
        ps.setString(1, Bukkit.getServer().getOfflinePlayer(stringPlayer).getUniqueId().toString());
        ps.setString(2, stringPlayer);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

}
