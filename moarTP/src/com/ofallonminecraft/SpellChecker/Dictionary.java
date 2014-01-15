package com.ofallonminecraft.SpellChecker;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// Stolen from github.com/bozy17 with permission

/**
 * A dictionary manages a collection of known, correctly-spelled words.
 */
public class Dictionary {

	private Set<String> dict;

	/**
	 * Constructs a dictionary from words contained in a given file.
	 * <p>
	 * Blank lines or lines containing only whitespace should be
	 * ignored. Each (non-blank) line of the file will contain one word.
	 * For the dictionary, a word is any sequence of non-whitespace
	 * characters.  You should remove any leading or trailing whitespace
	 * around the word. (For this last condition, you will want to check
	 * the String Javadocs).
	 * <p>
	 * The constructor should read the file, store the file contents in
	 * memory, and then close the file.  It should throw an
	 * <code>IllegalArgumentException</code> if the String filename
	 * provided is null.
	 *
	 * @param filename a path of a dictionary file
	 * @throws IOException if error reading the file
	 */
	public Dictionary() {
		dict = new HashSet<String>();
	}
	
	/**
	 * Adds a word to the dictionary.
	 * 
	 * @param s the word to add
	 */
	public void addWord(String s) {
		dict.add(s);
	}

	/**
	 * Returns the number of unique words in this dictionary. This count is
	 * case insensitive: if both "DOGS" and "dogs" appeared in the file, it
	 * should only be counted once in the returned sum.
	 * 
	 * @return number of unique words in the dictionary
	 */
	public int getNumWords() {
		return dict.size();
	}

	/**
	 * Test whether the input word is present in the Dictionary.
	 * <p>
	 * This check should be case insensitive.  For example, if the
	 * Dictionary contains "dog" or "dOg" then isWord("DOG") should return true.
	 * <p>
	 * The null string is not in the dictionary.
	 * <p>
	 * Calling this method should not re-open or re-read the source file.
	 *
	 * @param word a string token to check. Assume any leading or trailing
	 *    whitespace has already been removed.
	 * @return whether the word is in the dictionary
	 */
	public boolean isWord(String word) {
		if (dict.contains(word.toLowerCase())) { 
			return true; 
		}
		else return false;
	}
	
	public Set<String> getWords() {
		return dict;
	}
}
