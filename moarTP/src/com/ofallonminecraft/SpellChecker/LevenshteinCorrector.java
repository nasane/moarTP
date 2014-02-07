package com.ofallonminecraft.SpellChecker;

import java.util.HashSet;
import java.util.Set;

public class LevenshteinCorrector extends Corrector {

  private Dictionary d;

  public LevenshteinCorrector(Dictionary dict) {
    if (dict == null) throw new IllegalArgumentException();
    d = dict;
  }

  public Set<String> getCorrections(String wrong) {
    Set<String> answer = new HashSet<String>();
    wrong.trim();
    if (d.isWord(wrong)) {
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
      if (min>5) answer.clear();
    }
    return answer;
  }

  private int LevenshteinDistance(String s, String t) {
    if (s.equals(t)) return 0;
    if (s.length() == 0) return t.length();
    if (t.length() == 0) return s.length();
    int[] v0 = new int[t.length()+1];
    int[] v1 = new int[t.length()+1];
    for (int i=0; i<=v0.length; ++i) v0[i] = i;
    for (int i=0; i<s.length(); ++i) {
      v1[0] = i+1;
      for (int j=0; j<t.length(); ++j) {
        int cost = (s.charAt(i)==t.charAt(j)) ? 0 : 1;
        v1[j+1] = minimumOf(v1[j]+1, v0[j+1], v0[j]+cost);
      }
      for (int j=0; j<v0.length; ++j) v0[j] = v1[j];
    }
    return v1[t.length()];
  }

  private int minimumOf(int i, int j, int k) {
    if (i<j) {
      if (i<k) return i;
      else return k;
    } else {
      if (j<k) return j;
      else return k;
    }
  }

}
