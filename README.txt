moarTP v0.25
Author: Nathan Bossart
Website: http://ofallonminecraft.com/
Contact: info@ofallonminecraft.com


Description:
The moarTP plugin adds 4 new commands for convenient, customized teleportation. Instead of dealing with coordinates or depending on other players, users may save locations to the server for easy teleportation later. This provides for easy navigation between player creations.

The 4 commands added by this plugin are:
/goto [location] : teleport to a custom location
/claim [location] : define a custom location (where you're standing)
/unclaim [location] : remove a custom location
/view : view all locations in the system

The plugin is still in its early stages, as a number of new features and cleaned-up interfaces will soon be added. These include a better display for /view, first-load fixes, the ability to define sub-locations for locations, and more. Source code to be provided via github as soon as possible.


Notes:
This is the first publicly released beta of this Minecraft Plugin.  New features and code tweaks will be added periodically in order to make it the best!


Instructions for use:
Download the plugin from http://dev.bukkit.org/server-mods/moarTP.  Place in your plugins folder for your Bukkit server, and reload the server.  It should be ready to go!

-----Changelog-----

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