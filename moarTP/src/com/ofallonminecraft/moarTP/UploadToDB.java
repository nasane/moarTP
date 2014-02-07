package com.ofallonminecraft.moarTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UploadToDB {

  public static boolean uploadToDB(String version) {

    Map<String, MTLocation> locations  = new HashMap<String, MTLocation>();
    Map<String, String>     info       = new HashMap<String, String>();
    Map<String, String>     homes      = new HashMap<String, String>();

    // retrieve credentials from moarTP_db.config
    String hostName = null;
    String port     = null;
    String database = null;
    String user     = null;
    String pass     = null;

    BufferedReader reader = null;
    String f = "";
    try {
      reader = new BufferedReader(new FileReader("plugins/moarTP/moarTP_db.config"));
      String l = null;
      while ((l=reader.readLine())!=null) f += l + "\n";
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    hostName = f.split("\n")[0].split("\\s")[1];
    port     = f.split("\n")[1].split("\\s")[1];
    database = f.split("\n")[2].split("\\s")[1];
    user     = f.split("\n")[3].split("\\s")[1];
    pass     = f.split("\n")[4].split("\\s")[1];

    // connect to db
    MySQL MySQL = new MySQL(hostName, port, database, user, pass);
    Connection c = null;
    c = MySQL.open();

    /*
		 case 1: no existing locations
		 solution: just create the table and prepare for use

		 case 2: existing locations
		 solution: create table, upload all information, and prepare for use
     */

    try {
      Statement implementSchema = c.createStatement();
      String table = "create table moarTP (location VARCHAR(256), "
          + "creationTime DATETIME, creator VARCHAR(256), x INT, y INT, "
          + "z INT, world VARCHAR(256), info VARCHAR(256), secret CHAR(1), "
          + "hashedPass VARCHAR(256), encryptedLocation VARCHAR(256), "
          + "home VARCHAR(256), version DOUBLE);";
      implementSchema.executeUpdate(table);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    boolean locExist = false;
    boolean infoExist = false;
    boolean homesExist = false;
    try {
      if ((new File("plugins/moarTP/moarTP_locs.bin").exists())) {
        locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
        locExist = true;
      }
      if ((new File("plugins/moarTP/moarTP_info.bin").exists())) {
        info = SLAPI.load("plugins/moarTP/moarTP_info.bin");
        infoExist = true;
      }
      if ((new File("plugins/moarTP/moarTP_homes.bin").exists())) {
        homes = SLAPI.load("plugins/moarTP/moarTP_homes.bin");
      }
    } catch (Exception e) {
      e.printStackTrace();	
      return false;
    }

    if (locExist) {
      Set<String> locsToInsert = locations.keySet();
      Iterator<String> i = locsToInsert.iterator();
      while (i.hasNext()) {
        String locName = (String) i.next();
        MTLocation coords = locations.get(locName);
        String description = null;
        if (infoExist) {
          if (info.containsKey(locName)) description = info.get(locName);
        }
        String template = "insert into moarTP (location, creationTime, creator, x, y, z, "
            + "world, info, secret, version) values(?, ?, ?, ?, ?, ?, ?, ?, 'N', ?);";
        try {
          PreparedStatement insertion = c.prepareStatement(template);
          insertion.setString(1, locName);
          insertion.setInt(4, coords.x);
          insertion.setInt(5, coords.y);
          insertion.setInt(6, coords.z);
          insertion.setString(7, coords.world);
          insertion.setString(9, version);
          if (description!=null) {
            if (description.split("\\r?\\n").length!=1) {
              String trimmedDescription = description.split("\\r?\\n")[0];
              insertion.setString(8, trimmedDescription);
            } else {
              insertion.setNull(8,12);
            }
            insertion.setString(3, description.split("\n")[description.split("\n").length-1].split("\\s")[2]);
            String[] dateParts = description.split("\n")[description.split("\n").length-1].split("\\s")[5].split("/");
            String reformattedDate = dateParts[2]+"-"+dateParts[0]+"-"+dateParts[1];
            insertion.setString(2, reformattedDate+" "+description.split("\n")[description.split("\n").length-1].split("\\s")[4]);
          }
          else {
            insertion.setNull(8, 12);
            insertion.setNull(3, 12);
            insertion.setNull(2, 93);
          }
          insertion.executeUpdate();
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }

      if (homesExist) {
        Set<String> homesToInsert = homes.keySet();
        Iterator<String> j = homesToInsert.iterator();
        while (j.hasNext()) {
          String person = (String) j.next();
          String personsHome = homes.get(person);
          try {
            String template = "update moarTP set home=? where location=?;";
            PreparedStatement alter = c.prepareStatement(template);
            Statement s = c.createStatement();
            String newHomeList;
            ResultSet currentHomeList = s.executeQuery("select home from moarTP where location='"
                +personsHome+"';");
            if (!currentHomeList.isBeforeFirst()) {
              newHomeList = person;
            }
            else {
              currentHomeList.next();
              if (currentHomeList.getString(1)==null || 
                  currentHomeList.getString(1).equals("null")) newHomeList = person;
              else newHomeList = currentHomeList.getString(1) + "," + person;
            }
            alter.setString(1, newHomeList);
            alter.setString(2, personsHome);
            alter.executeUpdate();
          } catch (Exception e) {
            e.printStackTrace();
            return false;
          }
        }
      }
    }

    try {
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

}
