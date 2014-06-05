package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.ofallonminecraft.SpellChecker.SpellChecker;

public class Describe {

  public static boolean describe(CommandSender sender, String[] args, Connection c) {

    // check number of arguments
    if (args.length < 1) {
      sender.sendMessage("Must enter a location name and a description!");
      return false;
    } else if (args.length < 2) {
      sender.sendMessage("Must enter a description for the location!");
      return false;
    } else {
      if (!(args[1].startsWith("\"") && args[args.length-1].endsWith("\""))) {
        sender.sendMessage("Location name or description not formatted correctly!");
        return false;
      } 
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

      // ----- DESCRIBE ----- //
      try {
        PreparedStatement s = c.prepareStatement("select location from moarTP where location=?;");
        s.setString(1, args[0].toLowerCase());
        String description = "";
        for (int i=1; i<args.length; ++i) {
          description += args[i] + ' ';
        }
        description = description.substring(1,description.length()-2 );
        ResultSet rs = s.executeQuery();
        if (!rs.next()) {
          sender.sendMessage(args[0].toLowerCase()+" is not in the library!");
          HashSet<String> dict_subs = new HashSet<String>();
          dict_subs.add(SpellChecker.LOCATIONS);
          String sug = new SpellChecker(c, dict_subs).getSuggestion(args[0].toLowerCase());
          if (sug != null) {
            sender.sendMessage("Did you mean '/describe " + sug + "\"" + description + "\"'?");
          }
        } else {
          PreparedStatement ps = c.prepareStatement("update moarTP set info=? where location=?;");
          ps.setString(1, description);
          ps.setString(2, args[0].toLowerCase());
          ps.executeUpdate();
          sender.sendMessage(args[0].toLowerCase()+"'s description was successfully updated in the library.");
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END DESCRIBE ----- //
    }
    // if the user doesn't have permission, present an error message
    sender.sendMessage("You don't have permission to do this!");
    return false;
  }
}
