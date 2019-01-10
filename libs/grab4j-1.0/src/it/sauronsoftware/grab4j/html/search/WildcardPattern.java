package it.sauronsoftware.grab4j.html.search;

import java.util.regex.Pattern;

/**
 * A WildcardPattern is a simple pattern for string recognition. In its
 * definition you can use the wildcard character star (*) to identify any
 * character sequence.
 * 
 * @author Carlo Pelliccia
 */
public class WildcardPattern {

	/**
	 * The pattern as a string.
	 */
	private String pattern;

	/**
	 * Is this pattern case insensitive?
	 */
	private boolean caseInsensitive;

	/**
	 * Is the wildcard character start used in the pattern? If not we can
	 * optimize the recognition routine, using simple string comparisons.
	 * Otherwise we have to use some RegExps.
	 */
	private boolean wildcardUsed;

	/**
	 * The RegExp pattern implementing the wildcard pattern.
	 */
	private Pattern regexp;

	/**
	 * It builds a WildcardPattern.
	 * 
	 * @param pattern
	 *            The pattern as a string.
	 * @param caseInsensitive
	 *            true and the pattern will be case-insensitive.
	 */
	WildcardPattern(String pattern, boolean caseInsensitive) {
		// Set the values.
		this.pattern = pattern;
		this.caseInsensitive = caseInsensitive;
		// Is the start character used?
		wildcardUsed = (pattern.indexOf('*') != -1);
		// If the star is used a RegExp is built to implement the pattern.
		if (wildcardUsed) {
			StringBuffer buffer = new StringBuffer(pattern);
			// This chars need an escape sequence...
			replace(buffer, "\\", "\\\\");
			replace(buffer, "[", "\\[");
			replace(buffer, "]", "\\]");
			replace(buffer, "(", "\\(");
			replace(buffer, ")", "\\)");
			replace(buffer, "{", "\\{");
			replace(buffer, "}", "\\}");
			replace(buffer, "^", "\\^");
			replace(buffer, "$", "\\$");
			replace(buffer, "-", "\\-");
			replace(buffer, "&", "\\&");
			replace(buffer, "?", "\\?");
			replace(buffer, "+", "\\+");
			replace(buffer, "|", "\\|");
			replace(buffer, ":", "\\:");
			replace(buffer, "=", "\\=");
			replace(buffer, "!", "\\!");
			replace(buffer, "<", "\\<");
			replace(buffer, ">", "\\>");
			replace(buffer, ".", "\\.");
			replace(buffer, "*", ".*");
			// Start and end of the string.
			buffer.insert(0, '^');
			buffer.append('$');
			// Build the pattern.
			if (caseInsensitive) {
				regexp = Pattern.compile(buffer.toString(),
						Pattern.CASE_INSENSITIVE);
			} else {
				regexp = Pattern.compile(buffer.toString());
			}
		}
	}

	/**
	 * It builds a case-insensitive wildcard pattern.
	 * 
	 * @param pattern
	 *            The pattern as a string.
	 */
	public WildcardPattern(String pattern) {
		this(pattern, true);
	}

	/**
	 * This method returns true if the pattern is case-insensitive.
	 * 
	 * @return true if the pattern is case-insensitive.
	 */
	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	/**
	 * This method returns the pattern as a string.
	 * 
	 * @return The pattern as a string.
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * This method tests a string against the pattern.
	 * 
	 * @param str
	 *            The string.
	 * @return true if the given string matches the pattern.
	 */
	public boolean matches(String str) {
		if (wildcardUsed) {
			// RegExp needed!
			return regexp.matcher(str).matches();
		} else {
			// Simple string comparison.
			if (caseInsensitive) {
				// Case-insensitive.
				return pattern.equalsIgnoreCase(str);
			} else {
				// Case-sensitive.
				return pattern.equals(str);
			}
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof WildcardPattern) {
			WildcardPattern aux = (WildcardPattern) obj;
			if (caseInsensitive || aux.caseInsensitive) {
				return pattern.equalsIgnoreCase(aux.pattern);
			} else {
				return pattern.equals(aux.pattern);
			}
		}
		return false;
	}

	public String toString() {
		return "WildcardPattern[pattern=" + pattern + ",caseInsensitive="
				+ caseInsensitive + "]";
	}

	/**
	 * This methods compare the current pattern with another one. It returns
	 * true if the given pattern is a sub-pattern of the current one. A pattern
	 * "a" is sub-pattern of "b" if all the strings matched by "a" are also
	 * matched by "b".
	 * 
	 * @param wildcardPattern
	 *            The pattern to compare with the current one.
	 * @return true if the given pattern is a sub-pattern of the current one.
	 */
	public boolean comprehends(WildcardPattern wildcardPattern) {
		if (!caseInsensitive && wildcardPattern.caseInsensitive) {
			return false;
		}
		return matches(wildcardPattern.pattern);
	}

	/**
	 * This method replaces in a StringBuffer all the occurrences of a given
	 * sub-string with another sub-string.
	 * 
	 * @param buffer
	 *            The StringBuffer on which the operation have to be performed.
	 * @param search
	 *            The sub-string to search and replace.
	 * @param replacement
	 *            The replacement sub-string.
	 */
	private void replace(StringBuffer buffer, String search, String replacement) {
		int index = 0;
		int searchLength = search.length();
		int replacementLength = replacement.length();
		while (true) {
			index = buffer.indexOf(search, index);
			if (index > -1) {
				buffer.replace(index, index + searchLength, replacement);
				index += replacementLength;
			} else {
				break;
			}
		}
	}

}
