package com.ofallonminecraft.moarTP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitializeCreators {

	public static void initializeCreators() {

		// load necessary files
		Map<String, String>       info     = null;
		Map<String, List<String>> creators = null;
		try {
			info     = SLAPI.load("plugins/moarTP/moarTP_info.bin");
			creators = SLAPI.load("plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}	

		if (info.keySet()!=null) {
			// obtain list of locations
			Set<String>  allLocs = info.keySet();  // set of locations
			List<String> locs    = new ArrayList<String>(allLocs);  // list of locs

			// built multimap
			for (String loc : locs) {
				if (info.get(loc)!=null) {
					// get creator of location
					String creator = (info.get(loc).split("\\s"))[2];

					// get current list of their locations
					List<String> theirLocs = null;
					if (creators.get(creator)==null) theirLocs = new ArrayList<String>();
					else {
						theirLocs = creators.get(creator);
						creators.remove(creator);
					}

					// add the location to their list
					theirLocs.add(loc);
					Collections.sort(theirLocs);
					creators.put(creator, theirLocs);
				}
			}
		}

		// close and save files
		try {
			SLAPI.save(info,     "plugins/moarTP/moarTP_info.bin");
			SLAPI.save(creators, "plugins/moarTP/moarTP_creators.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}