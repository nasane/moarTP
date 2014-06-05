### v0.71
  -Migrated to UUID's.   

### v0.70
  -Added /desribe.   
  -/claim and /claimsecret encourage users to describe their claims.   

### v0.62
  -Minor improvements with the spell checking feature.   

### v0.61
  -Fixed /claimsecret to check if a password is provided.   
  -Misspelled commands now provide suggestions (based on the Levenshtein distance).   

### v0.60
  -Locations are now stored in a database.   
  -Can now claim secret locations that are encrypted with a password (/claimsecret).   
  -Can now retrieve the closest public location to a player with /whereis.   
  -Bug with listing a creator's locations is now fixed.   

### v0.50
  -/view now has the ability to display locations made by a certain player.   
  -Optimized throughout.  
  -Migrated to an MIT license.  

### v0.41
  -/unclaim now allows location creators to remove their locations.   
  -/view now displays a nice message instead of an error when there are no locations.   

### v0.40  
  -Fixed major bugs in /move.  
  -Cleaned up messages a bit.  

### v0.38  
  -Further encapsulated to fix bugs with file access.  

### v0.37  
  -Added /sethome and /gohome.  

### v0.36  
  -Encapsulated the code.  
  -Simplified /view for a cleaner interface.  

### v0.35  
  -Changed /goto to /tpto (for now) due to conflicting command names.  


### v0.34  
  -Added ability to provide description using /claim.  
  -Added /about to view the location description.  
  -Added /move so that ops can move player(s) to a custom location.  

### v0.31  
  -Fixed a major bug that caused errors in inter-world teleporting.  

### v0.30  
  -Second release  

### v0.25  
  -Cleaned up code and customized error messages.  

### v0.24  
  -Truly fixed first-run problem  
  -Adjusted output of /view into standardized columns  
  -Adjusted output of /view to sort the locations  

### v0.22  
  -Adjusted output of /view to account for servers with many locations defined.  

### v0.21  
  -Fixed first-run problem  
  -Changed directory of storage file to ~/plugins/moarTP/moarTP_locs.bin  

### v0.20  
  -First beta  
