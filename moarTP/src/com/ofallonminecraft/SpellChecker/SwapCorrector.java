package com.ofallonminecraft.SpellChecker;

import java.util.HashSet;
import java.util.Set;

// Stolen from github.com/bozy17 with permission

/**
 * A Corrector whose spelling suggestions come from "swapped letter" typos.
 * <p>
 * A common misspelling is accidentally swapping two adjacent letters, e.g.
 * "with" -> "wiht". This Corrector is given a Dictionary of valid words and
 * proposes corrections that are precisely one "swap" away from the incorrect
 * word.
 * <p>
 * For example, if the incorrect word is "haet", then this Corrector might
 * suggest "heat" and "hate", provided that both of these words occur in the
 * dictionary.
 * <p>
 * Only swaps between adjacent letters are considered by this corrector.
 */
public class SwapCorrector extends Corrector {
	
	private Dictionary d;
	
  /**
   * Constructs a SwapCorrector instance using the given Dictionary.
   * Should throw an <code>IllegalArgumentException</code> if the Dictionary
   * provided is null.
   *
   * @param dict the reference dictionary to use to look for
   *   corrections arising from letter swaps
   */
  public SwapCorrector(Dictionary dict) {
	  if (dict == null) {
		  throw new IllegalArgumentException();
	  }

	  d = dict;
  }

  /**
   * Suggests as corrections any words in the Dictionary (provided when the
   * object is constructed) that are one swap away from the input word. A swap
   * is defined as two adjacent letters exchanging positions.
   * <p>
   * For example, if the dictionary contains the word "heat" and "hate", then
   * both should be returned if the input word is "haet".
   * <p>
   * The input word (regardless of whether it itself is in the Dictionary or
   * not) should never appear in the result set.
   * <p>
   * See superclass documentation for more information.
   *
   * @param wrong the input word
   */
  public Set<String> getCorrections(String wrong) {
	  Set<String> answer = new HashSet<String>();
	  	
	  wrong.trim();
	  	
	  String w = wrong.toLowerCase();
	  	
	  if (d.isWord(w)) {
		  answer.clear();
	  }
	  
	  else {
		  //iterate along word to swap all pairs of adjacent letters
		 for (int i = 0; i < wrong.length() - 1; i++) {
			  
			  //swap two adjacent letters in the wrong word
			  char[] c = w.toCharArray();
			  char temp = c[i];
			  c[i] = c[i + 1];
			  c[i + 1] = temp;
			  String swap = new String(c);

			  if (d.isWord(swap)) {
				  answer.add(swap);
			  }
		  }
		  answer = matchCase(wrong, answer);
	  }
	  
	  return answer;
  }
}
