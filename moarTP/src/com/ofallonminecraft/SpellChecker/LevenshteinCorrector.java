package com.ofallonminecraft.SpellChecker;

import java.util.HashSet;
import java.util.Set;

public class LevenshteinCorrector extends Corrector {

	private Dictionary d;

	public LevenshteinCorrector(Dictionary dict) {
		if (dict == null) {
			throw new IllegalArgumentException();
		}
		d = dict;
	}

	public Set<String> getCorrections(String wrong) {
		Set<String> answer = new HashSet<String>();
		wrong.trim();
		String w = wrong.toLowerCase();
		if (d.isWord(w)) {
			answer.clear();
		} else {
			int min = -1;
			for (String word : d.getWords()) {
				if (wrong!=null && word!=null) {
					int dist = LevenshteinDistance(wrong, word);
					if (min==-1 || dist<min) {
						answer.clear();
						answer.add(word);
						min = dist;
					} else if (dist==min) {
						answer.add(word);
					}
				}
			}
			if (min>5) {  // don't return suggestions if more than 5 changes are needed
				answer.clear();
			}
		}
		return answer;
	}

	private int LevenshteinDistance(String s, String t) {
		int[][] d = new int[s.length()+1][t.length()+1];
		for (int i=1; i<=s.length(); ++i) {
			d[i][0] = i;
		}
		for (int j=1; j<=t.length(); ++j) {
			d[0][j] = j;
		}
		for (int k=1; k<=t.length(); ++k) {
			for (int l=1; l<=s.length(); ++l) {
				if (s.charAt(l-1) == t.charAt(k-1)) {
					d[l][k] = d[l-1][k-1];
				} else {
					int del = d[l-1][k]   + 1;
					int ins = d[l][k-1]   + 1;
					int sub = d[l-1][k-1] + 1;
					d[l][k] = minimumOf(del, ins, sub);
				}
			}
		}
		return d[s.length()][t.length()];
	}


	private int minimumOf(int del, int ins, int sub) {
		if (del<ins) {
			if (del<sub) {
				return del;
			} else {
				return sub;
			}
		} else {
			if (ins<sub) {
				return ins;
			} else {
				return sub;
			}
		}
	}

}
