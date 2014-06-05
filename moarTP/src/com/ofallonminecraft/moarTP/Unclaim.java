package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.ofallonminecraft.SpellChecker.SpellChecker;

public class Unclaim {

  public static boolean unclaim(CommandSender sender, String[] args, Connection c) {

    // check number of arguments
    if (args.length > 1) {
      sender.sendMessage("Location name must be one word!");
      return false;
    }
    if (args.length < 1) {
      sender.sendMessage("Must choose a location to unclaim!");
      return false;
    }

    // check if user is the creator of the location
    boolean isCreator = false;
    try {
      PreparedStatement s = c.prepareStatement("select creator from moarTP where location=?;");
      s.setString(1, args[0].toLowerCase());
      ResultSet creatorName = s.executeQuery();
      if (creatorName.next() && 
          creatorName.getString(1)!=null &&
          !creatorName.getString(1).equals("null") &&
          Bukkit.getServer().getOfflinePlayer(UUID.fromString(
              creatorName.getString(1))).getName().equals(
                  sender.getName())) isCreator = true;
      creatorName.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // check user permissions
    if (sender.isOp() || isCreator) {

      // ----- UNCLAIM ----- //
      try {
        PreparedStatement s = c.prepareStatement("select location from moarTP where location=?;");
        s.setString(1, args[0].toLowerCase());
        ResultSet rs = s.executeQuery();
        if (!rs.next()) {
          sender.sendMessage(args[0].toLowerCase()+" is not in the library!");
          HashSet<String> dict_subs = new HashSet<String>();
          dict_subs.add(SpellChecker.LOCATIONS);
          String sug = new SpellChecker(c, dict_subs).getSuggestion(args[0].toLowerCase());
          if (sug != null) {
            sender.sendMessage("Did you mean \"/unclaim " + sug + "\"?");
          }
        } else {
          PreparedStatement ps = c.prepareStatement("delete from moarTP where location=?;");
          ps.setString(1, args[0].toLowerCase());
          ps.executeUpdate();
          sender.sendMessage(args[0].toLowerCase()+" was successfully deleted from the library.");
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END UNCLAIM ----- //
    }
    // if the user doesn't have permission, present an error message
    sender.sendMessage("You don't have permission to do this!");
    return false;
  }
}
