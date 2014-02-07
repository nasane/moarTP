package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.bukkit.command.CommandSender;
import com.ofallonminecraft.SpellChecker.SpellChecker;

public class View {

  public static boolean view(CommandSender sender, String[] args, Connection c) {

    // check user permissions
    if (sender.hasPermission("moarTP.view")) {
      // check number of arguments
      if (args.length > 1) {
        sender.sendMessage("Please choose one player name.");
        return false;
      }

      // ----- VIEW ----- //
      try {
        ResultSet rs = null;
        PreparedStatement s = null;
        if (args.length == 1) {
          s = c.prepareStatement("select location from moarTP where creator=?;");
          s.setString(1, args[0]);
          rs = s.executeQuery();
        } else {
          rs = c.createStatement().executeQuery("select location from moarTP;");
        }
        if (rs.next()) {
          List<String> toViewList = new ArrayList<String>();
          toViewList.add(rs.getString(1));
          while (rs.next()) toViewList.add(rs.getString(1));
          Collections.sort(toViewList);
          String toView = toViewList.get(0);
          for (int i=1; i<toViewList.size(); ++i) toView += ", "+toViewList.get(i);
          sender.sendMessage(toView);
        } else {
          if (args.length == 1) {
            sender.sendMessage("Sorry, there aren't any locations claimed by "+args[0] +
                ". Double check: names are case sensitive!");
            HashSet<String> dict_subs = new HashSet<String>();
            dict_subs.add(SpellChecker.CREATORS);
            String sug = new SpellChecker(c, dict_subs).getSuggestion(args[0]);
            if (sug != null) {
              sender.sendMessage("Did you mean \"/view " + sug + "\"?");
            }
          } else sender.sendMessage("No locations claimed yet. Claim one with /claim <location>");
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END VIEW ----- //
    }
    // if, for whatever reason, the player doesn't have permission
    sender.sendMessage("You don't have permission to do this!");
    return false;
  }
}
