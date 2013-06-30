package com.ofallonminecraft.moarTP;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class ClaimSecret {

	// TODO: find a way to prevent the entered password from showing up on the server logs
	
	public static boolean claimSecret(CommandSender sender, String[] args, Player player, Connection c) {

		// open file of locs and associated location info
		Map<String, MTLocation>   locations  = null;
		Map<String, List<String>> secretLocs = null;
		Map<String, String>       info       = null;
		Map<String, List<String>> creators   = null;
		try {
			locations  = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
			info       = SLAPI.load("plugins/moarTP/moarTP_info.bin");
			creators   = SLAPI.load("plugins/moarTP/moarTP_creators.bin");
			secretLocs = SLAPI.load("plugins/moarTP/moarTP_secret.bin"); 
		} catch (Exception e) {
			e.printStackTrace();
		}


		// check user permissions
		if (sender.hasPermission("moarTP.claimsecret")) {

			// check number of arguments
			if (args.length < 1) {
				sender.sendMessage("Must enter a location name!");
				return false;
			}
			// check that any extra location info is enclosed in quotes
			if (args.length>2) {
				if (!(args[2].startsWith("\"") && args[args.length-1].endsWith("\""))) {
					sender.sendMessage("Location name or description not formatted correctly!");
					return false;
				} 
			}


			// ----- CLAIMSECRET ----- //

			// check that the location name isn't taken
			if (locations.containsKey(args[0].toLowerCase()) || secretLocs.containsKey(args[0].toLowerCase())) {
				player.sendMessage(args[0]+" is already in the library!");
			}
			else {
				// save the location
				Location loc = player.getLocation();
				MTLocation toSave = MTLocation.getMTLocationFromLocation(loc);

				/* FOR YOU CRYPTO NUTS!
				 * The password is hashed with a salted PBKDF2 algorithm. The
				 * location is encrypted with 128-bit AES.  While the password
				 * and location is sent to the function in cleartext, only the 
				 * hashed password and the encrypted location is saved to any 
				 * files or variables accessible beyond the scope of this 
				 * function.  If you need better encryption in Minecraft, you
				 * probably work at the NSA (in which case, contact me and tell
				 * me cool ways to strengthen the encryption further).
				 */
				String hashedPassword    = "";
				String encryptedLocation = "";
				try {
					hashedPassword    = PasswordHash.createHash(args[1]);  // Note: password is case sensitive!
					encryptedLocation = SimpleCrypto.encrypt(args[1], toSave.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				List<String> encryptedLocInfo = new ArrayList<String>();
				encryptedLocInfo.add(hashedPassword);
				encryptedLocInfo.add(encryptedLocation);

				secretLocs.put(args[0].toLowerCase(), encryptedLocInfo);
				try {
					SLAPI.save(secretLocs, "plugins/moarTP/moarTP_locs.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}

				// TODO: revise the rest of the source from here on (info should be encrypted)

				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
				Date date = new Date();
				String timeStamp = dateFormat.format(date);
				String locInfo = "Created by "+player.getDisplayName()+" on "+timeStamp+".";

				// format the description
				if (args.length>1) {
					String description = "";
					for (int i=1; i<args.length; ++i) {
						description += args[i] + ' ';
					}
					description = description.substring(1,description.length()-2 );
					info.put(args[0].toLowerCase(), description + "\n" + locInfo);
				} else {
					info.put(args[0].toLowerCase(), locInfo);
				}

				// save the location info
				try {
					SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// save the creator info -- TODO: refactor this
				if (creators.get(player.getDisplayName())!=null) {
					List<String> theirList = creators.get(player.getDisplayName());
					theirList.add(args[0].toLowerCase());
					Collections.sort(theirList);
					creators.remove(player.getDisplayName());
					creators.put(player.getDisplayName(), theirList);
				} else {
					List<String> theirList = new ArrayList<String>();
					theirList.add(args[0].toLowerCase());
					creators.put(player.getDisplayName(), theirList);
				}

				player.sendMessage(args[0]+" successfully saved to library.");
			}

			// save the files
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				SLAPI.save(creators, "plugins/moarTP/moarTP_creators.bin");
				SLAPI.save(secretLocs, "plugins/moarTP/moarTP_secretLocs.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;

			// ----- END CLAIMSECRET ----- //
		}

		else {
			// close file streams regardless
			try {
				SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
				SLAPI.save(info, "plugins/moarTP/moarTP_info.bin");
				SLAPI.save(creators, "plugins/moarTP/moarTP_creators.bin");
				SLAPI.save(secretLocs, "plugins/moarTP/moarTP_secretLocs.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// if user doesn't have permission, present an error message
			sender.sendMessage("You don't have permission to do this!");
			return false;	
		}
	}
}
