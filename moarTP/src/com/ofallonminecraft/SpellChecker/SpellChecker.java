package com.ofallonminecraft.SpellChecker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpellChecker {

  public static String LOCATIONS       = "locs";
  public static String ONLINE_PLAYERS  = "online_players";
  public static String CREATORS        = "creators";

  // TODO: generate global spell checker object that is initially loaded, kept up to date, and accessible from the methods that need it
  // This will improve efficiency.

  static LevenshteinCorrector lc;

  public SpellChecker(Connection c, HashSet<String> dictionary_subsets) {

    // build dictionary from playerlist and list of locs
    Dictionary d = new Dictionary();
    try {
      ResultSet rs = null;
      if (dictionary_subsets==null || dictionary_subsets.contains(LOCATIONS)) {
        rs = c.createStatement().executeQuery("select location from moarTP;");
        if (rs.next()) {
          d.addWord(rs.getString(1));
          while (rs.next()) d.addWord(rs.getString(1));
        } 
        rs.close();
      }
      // TODO: this won't work anymore
      if (dictionary_subsets==null || dictionary_subsets.contains(CREATORS)) {
        rs = c.createStatement().executeQuery("select distinct creator from moarTP;");
        if (rs.next()) {
          d.addWord(rs.getString(1));
          while (rs.next()) d.addWord(rs.getString(1));
        }
        rs.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (dictionary_subsets==null || dictionary_subsets.contains(ONLINE_PLAYERS)); {
      Player[] list = Bukkit.getOnlinePlayers();
      for (Player p : list){
        d.addWord(p.getName());
      }
    }

    lc = new LevenshteinCorrector(d);

  }

  public String getSuggestion(String toCorrect) {
    Set<String> corr = lc.getCorrections(toCorrect);
    if (corr.size()>0) {
      return (String) corr.toArray()[0];
    }
    return null;
  }

}
