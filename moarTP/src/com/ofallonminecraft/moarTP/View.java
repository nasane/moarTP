package com.ofallonminecraft.moarTP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class View {
	
	public static boolean view(CommandSender sender, String[] args, Map<String, MTLocation> locations) {
		

		// check number of arguments
		if (args.length > 0) 
		{
			sender.sendMessage("This command doesn't take arguments.");
			return false;
		}


		// ----- VIEW ----- //

		Set<String> viewLocs = locations.keySet();   // set of locations
		List<String> sortedLocs = new ArrayList<String>(viewLocs);  // create a list of the locations
		Collections.sort(sortedLocs);                // sort the list
		Iterator<String> i = sortedLocs.iterator();  // iterator on list of locs
		int numPerLine = ((viewLocs.size()-1)/10);   // number of locs to be displayed per line minus one

		int maxLength = 0;
		while (i.hasNext()) {                        // find the maximum length of an entry
			int localLength = i.next().length();
			if (localLength>maxLength) maxLength=localLength;
		}
		int columnSpace = maxLength+3;               // determine column size

		i = sortedLocs.iterator();                   // reinitialize iterator
		while (i.hasNext()) {
			// add one location to the output string
			String toView = i.next();
			// adjust for column space
			toView += StringUtils.repeat(" ",(columnSpace-toView.length()));
			// append the rest of the line to the output string
			for (int j=0; j<numPerLine; j++){ 
				// adjust for column space
				if (i.hasNext()) toView += StringUtils.repeat(" ",(columnSpace*(j+1)-toView.length()))+i.next();
			}
			// output the string
			sender.sendMessage(toView);
		}
		return true;

		// ----- END VIEW ----- //

		
	}

}
