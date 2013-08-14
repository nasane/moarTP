package com.ofallonminecraft.moarTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class moarTP extends JavaPlugin
{

	public String version = "0.60";
	public static Map<String, String> metaData = new HashMap<String, String>();
	public boolean enabled = false;
	Connection c = null;

	// on enable, ensure correct directory structure plugin files, and db connection
	public void onEnable() {
		try {
			if (new File("plugins/moarTP/").exists()) {
				if (new File("plugins/moarTP/moarTP_db.config").exists()) {
					boolean skipConnect = false;
					if (new File("plugins/moarTP/moarTP.bin").exists()) {
						metaData = SLAPI.load("plugins/moarTP/moarTP.bin");
						String oldVersion = metaData.get("version");
						if (!oldVersion.equals(version)) {
							// provide backwards and forward compatibility for future versions
							metaData.remove("version");
							metaData.put("version", version);
						}
						SLAPI.save(metaData, "plugins/moarTP/moarTP.bin");
					} else {
						boolean uploadSuccess = UploadToDB.uploadToDB(version);
						if (uploadSuccess) {
							getLogger().info("All moarTP locations have been moved "
									+ "to the database provided.  You may delete all plugin files "
									+ "for moarTP EXCEPT moarTP_db.config AND moarTP.bin.");
							metaData.put("version", version);
							SLAPI.save(metaData, "plugins/moarTP/moarTP.bin");
						}
						else {
							getLogger().info("Something went horribly wrong when trying to "
									+ "upload the moarTP locations to the database.  Double check "
									+ "your moarTP_db.config file and try reloading again.");
							skipConnect = true;
						}
					}
					if (!skipConnect) {
						String         hostName = null;
						String         port     = null;
						String         database = null;
						String         user     = null;
						String         pass     = null;
						BufferedReader reader   = null;
						String f = "";
						try {
							reader = new BufferedReader(new FileReader("plugins/moarTP/moarTP_db.config"));
							String l = null;
							while ((l=reader.readLine())!=null) f += l + "\n";
						} catch (Exception e) {
							e.printStackTrace();
						}
						String[] flines = f.split("\n");
						hostName = flines[0].split("\\s")[1];
						port     = flines[1].split("\\s")[1];
						database = flines[2].split("\\s")[1];
						user     = flines[3].split("\\s")[1];
						pass     = flines[4].split("\\s")[1];

						MySQL MySQL = new MySQL(hostName, port, database, user, pass);
						c = MySQL.open();
						getLogger().info("moarTP has been enabled");
						enabled = true;
					}
				} else createCredentialFileSkeleton();
			} else {
				new File("plugins/moarTP").mkdir();
				createCredentialFileSkeleton();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createCredentialFileSkeleton() {
		try {
			new File("plugins/moarTP/moarTP_db.config").createNewFile();
			FileWriter writer = null;
			try {
				writer = new FileWriter("plugins/moarTP/moarTP_db.config");
				writer.write("Host: \nPort: \nDatabase: \nUsername: \nPassword: \n");
			} finally {
				if (writer!=null) writer.close();
			}
			getLogger().info("For moarTP to work, please insert database "
					+ "credentials into the plugins/moarTP/moarTP_db.config "
					+ "file.  Then reload the server.");
		} catch (Exception e) {
			getLogger().info("moarTP encountered a problem when attempting "
					+ "to generate a database credential file.");
		}
	}

	// on disable, print a message
	public void onDisable() {
		try {
			if (!c.isClosed()) c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getLogger().info("moarTP hs been disabled.");
	}


	// ---------- HANDLE THE COMMANDS ---------- //
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{

		if (enabled) {
			
			// reconnect if we've lost our connection to the db
			try {
				if (!c.isValid(2)) {
					String         hostName = null;
					String         port     = null;
					String         database = null;
					String         user     = null;
					String         pass     = null;
					BufferedReader reader   = null;
					String f = "";
					try {
						reader = new BufferedReader(new FileReader("plugins/moarTP/moarTP_db.config"));
						String l = null;
						while ((l=reader.readLine())!=null) f += l + "\n";
					} catch (Exception e) {
						e.printStackTrace();
					}
					String[] flines = f.split("\n");
					hostName = flines[0].split("\\s")[1];
					port     = flines[1].split("\\s")[1];
					database = flines[2].split("\\s")[1];
					user     = flines[3].split("\\s")[1];
					pass     = flines[4].split("\\s")[1];

					MySQL MySQL = new MySQL(hostName, port, database, user, pass);
					c = MySQL.open();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// ----- Functions that do not require user to be a player ----- //

			if (cmd.getName().equalsIgnoreCase("about")) {
				return About.about(sender, args, c);
			} else if (cmd.getName().equalsIgnoreCase("move")) {
				return Move.move(sender, args, c);
			} else if (cmd.getName().equalsIgnoreCase("unclaim")) {
				return Unclaim.unclaim(sender, args, c);
			} else if (cmd.getName().equalsIgnoreCase("view")) {
				return View.view(sender, args, c);
			} else if (cmd.getName().equalsIgnoreCase("whereis")) {
				return WhereIs.whereIs(sender, args, c);
			} else


				// ----- Functions that do require the user to be a player ----- //

				// check if user is a player
				if (sender instanceof Player) {
					Player player = (Player) sender;

					// Tpto
					if (cmd.getName().equalsIgnoreCase("tpto")) {
						return Tpto.tpto(sender, args, player, c);
					} else if (cmd.getName().equalsIgnoreCase("claim")) {
						return Claim.claim(sender, args, player, version, c);
					} else if (cmd.getName().equalsIgnoreCase("sethome")) {
						return SetHome.sethome(sender, args, player, c);
					} else if (cmd.getName().equalsIgnoreCase("gohome")) {
						return GoHome.gohome(sender, args, player, c);
					} else if (cmd.getName().equalsIgnoreCase("claimsecret")) {
						return ClaimSecret.claimSecret(sender, args, player, c, version);
					}

				} else {
					// if user is not a player, present an error message
					sender.sendMessage("You must be a player!");
					return false;
				}
			return false;
		}
		// --------- END HANDLE THE COMMANDS ---------- //

		return false;
	}
}