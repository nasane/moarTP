package com.ofallonminecraft.SpellChecker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpellChecker {
	
	// TODO: generate global spell checker object that is initially loaded, kept up to date, and accessible from the methods that need it
	// This will improve efficiency.
	
	static LevenshteinCorrector lc;

	public SpellChecker(Connection c) {
		
		// build dictionary from playerlist and list of locs
		Dictionary d = new Dictionary();
		try {
			ResultSet rs = null;
			rs = c.createStatement().executeQuery("select location from moarTP;");
			if (rs.next()) {
				d.addWord(rs.getString(1));
				while (rs.next()) d.addWord(rs.getString(1));
			} 
			rs.close();
			rs = c.createStatement().executeQuery("select distinct creator from moarTP;");
			if (rs.next()) {
				d.addWord(rs.getString(1));
				while (rs.next()) d.addWord(rs.getString(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Player[] list = Bukkit.getOnlinePlayers();
		for (Player p : list){
			d.addWord(p.getName());
		}		
		
		lc = new LevenshteinCorrector(d);

	}

	public String getSuggestion(String toCorrect) {
		Set<String> corr = lc.getCorrections(toCorrect);
		if (corr.size()>0) {
			return (String) corr.toArray()[0];
		}
		return null;
	}

}
