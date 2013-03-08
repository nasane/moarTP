package com.ofallonminecraft.moarTP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class View {
	
	public static boolean view(CommandSender sender, String[] args) {
	    
	    
	    Map<String, MTLocation> locations = null;
	    try {
		locations = SLAPI.load("plugins/moarTP/moarTP_locs.bin");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    
		
	    

	    // check number of arguments
	    if (args.length > 0) 
		{
		    sender.sendMessage("This command doesn't take arguments.");
		    return false;
		}
	    
	    
	    // ----- VIEW ----- //
	    
	    Set<String> viewLocs = locations.keySet();       // set of locations
	    List<String> sortedLocs = new ArrayList<String>(viewLocs);  // create a list of the locations
	    if (sortedLocs.size()>0) {
		Collections.sort(sortedLocs);                // sort the list
		Iterator<String> i = sortedLocs.iterator();  // iterator on list of locs
		
		String toView = i.next();
		while (i.hasNext()) {
		    toView += ", " + i.next();
		}
		sender.sendMessage(toView);
		
		
		try {
		    SLAPI.save(locations, "plugins/moarTP/moarTP_locs.bin");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    } else {
		sender.sendMessage("No locations claimed yet. Claim one with /claim <location>");
	    }
	    
	    return true;
	    
	    // ----- END VIEW ----- //
	    
	}
}
