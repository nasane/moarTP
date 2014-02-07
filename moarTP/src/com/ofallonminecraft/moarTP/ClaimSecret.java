package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimSecret {

  public static boolean claimSecret(CommandSender sender, String[] args, Player player, Connection c, String version) {

    // check user permissions
    if (sender.hasPermission("moarTP.claimsecret")) {

      // check number of arguments
      if (args.length < 2) {
        sender.sendMessage("Must enter a location name and a password!");
        return false;
      }
      // check that any extra location info is enclosed in quotes
      if (args.length>2) {
        if (!(args[2].startsWith("\"") && args[args.length-1].endsWith("\""))) {
          sender.sendMessage("Location name or description not formatted correctly!");
          return false;
        } 
      }

      try {
        PreparedStatement s = c.prepareStatement("select location from moarTP where location=?;");
        s.setString(1, args[0].toLowerCase());
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
          player.sendMessage(args[0].toLowerCase()+" is already in the library!");
        } else {
          /* The password is hashed with a salted PBKDF2 algorithm.  The
           * location is encrypted with 128-bit AES.  While the password
           * and location is sent to the function in cleartext, only the
           * hashed password and the encrypted location is saved to any
           * files or variables accessible beyond the scope of this
           * function.
           */
          Location loc = player.getLocation();
          MTLocation toSave = MTLocation.getMTLocationFromLocation(loc);
          String hashedPassword = null;
          String encryptedLocation = null;
          try {
            hashedPassword = PasswordHash.createHash(args[1]);
            encryptedLocation = SimpleCrypto.encrypt(args[1], toSave.toString());
          } catch (Exception e) {
            e.printStackTrace();
          }
          String creator = player.getDisplayName();
          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date date = new Date();
          String timeStamp = dateFormat.format(date);
          String description = null;
          if (args.length>2) {
            description = "";
            for (int i=2; i<args.length; ++i) {
              description += args[i] + ' ';
            }
            description = description.substring(1,description.length()-2 );
          }
          String template = "insert into moarTP (location, creationTime, creator, "
              + "info, secret, version, hashedPass, encryptedLocation) values("
              + "?, ?, ?, ?, 'Y', ?, ?, ?);";
          PreparedStatement insertion = c.prepareStatement(template);
          insertion.setString(1, args[0].toLowerCase());
          insertion.setString(2, timeStamp);
          insertion.setString(3, creator);
          if (description!=null) insertion.setString(4, description);
          else insertion.setNull(4, 12);
          insertion.setString(5, version);
          insertion.setString(6, hashedPassword);
          insertion.setString(7, encryptedLocation);
          insertion.executeUpdate();
          player.sendMessage(args[0]+" successfully saved to library.");
          if (args.length==1) {
            player.sendMessage("In the future, try adding a description to your claimed locations. "
                + "(example: /claimsecret " + args[0] + " (password) \"My favorite place.\")");
            player.sendMessage("You can add a description to this location using /describe " 
                + args[0] + " \"description\"");
          }
        }
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
      // ----- END CLAIMSECRET ----- //
    } else {
      // if user doesn't have permission, present an error message
      sender.sendMessage("You don't have permission to do this!");
      return false;	
    }
  }
}
