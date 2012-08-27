moarTP v0.34
Author: Nathan Bossart
Website: http://ofallonminecraft.com/
Contact: info@ofallonminecraft.com


Description:
The moarTP plugin adds 6 new commands for convenient, customized teleportation. Instead of dealing with coordinates or depending on other players, users may save locations to the server for easy teleportation later. This provides for easy navigation between player creations.

The 6 commands added by this plugin are:
/goto [location] : teleport to a custom location
/claim [location] (["description"]): define a custom location (where you're standing)
/unclaim [location] : remove a custom location
/view : view all locations in the system
/about [location] : view location information
/move [player1(,player2,player3,...)] [location] : teleport other player(s) to a custom location

The plugin is still in its early stages, as a number of new features and cleaned-up interfaces will soon be added. These include a better display for /view, the ability to define sub-locations for locations, and more.  Visit http://github.com/ofallonminecraft/moarTP for the latest code!


Instructions for use:
Download the plugin from http://dev.bukkit.org/server-mods/moarTP.  Place in your plugins folder for your Bukkit server, and reload the server.  It should be ready to go!

-----Changelog-----

v0.34
-Added ability to provide description using /claim.
-Added /about to view the location description.
-Added /move so that ops can move player(s) to a custom location.

v0.31
-Fixed a major bug that caused errors in inter-world teleporting.

v0.30
-Second release

v0.25
-Cleaned up code and customized error messages.

v0.24
-Truly fixed first-run problem
-Adjusted output of /view into standardized columns
-Adjusted output of /view to sort the locations

v0.22
-Adjusted output of /view to account for servers with many locations defined.

v0.21
-Fixed first-run problem
-Changed directory of storage file to ~/plugins/moarTP/moarTP_locs.bin

v0.20
-First beta
