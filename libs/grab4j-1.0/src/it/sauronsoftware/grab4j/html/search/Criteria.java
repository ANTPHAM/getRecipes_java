package it.sauronsoftware.grab4j.html.search;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * This class represents a search criteria to find elements within a HTML
 * document representation.
 * </p>
 * 
 * <p>
 * A criteria is parsed from a string, using some simple rules. A grab4j search
 * criteria, first of all, is very similar to a XPath query.
 * </p>
 * 
 * <p>
 * You can launch a search from any element in a document. The search will be
 * performed over the sub-elements available. The next examples are supposed to
 * be executed over a whole document, but this is not ever required. You can
 * start to query wherever you want, of course using relative search paths.
 * </p>
 * 
 * <p>
 * A search criteria string representation is splitted in several parts,
 * separated by a slash character:
 * </p>
 * 
 * <pre>
 * token1/token2/token3
 * </pre>
 * 
 * <p>
 * Each token is used to recognize a tag or a set of tags. The general model is
 * the following:
 * </p>
 * 
 * <pre>
 * tagNamePattern[index](attribute1=valuePattern1)(attribute2=valuePattern2)(...)
 * </pre>
 * 
 * <p>
 * The first element in the token model is the tag name pattern. It is usefull
 * to find the wanted tag(s). It is a wildcard pattern: the star character can
 * be used to match any characters sequence.
 * </p>
 * 
 * <p>
 * A first simple example:
 * </p>
 * 
 * <pre>
 * html/body/div
 * </pre>
 * 
 * <p>
 * This criteria finds all the "div" elements whose father is the "body" tag,
 * which in turn is inside a "html" tag.
 * </p>
 * 
 * <p>
 * A wildcard example:
 * </p>
 * 
 * <pre>
 * html/body/*
 * </pre>
 * 
 * <p>
 * This criteria finds all the elements whose father is the "body" tag, within
 * the "html" one.
 * </p>
 * 
 * <p>
 * Another one:
 * </p>
 * 
 * <pre>
 * html/body/h*
 * </pre>
 * 
 * <p>
 * This criteria finds all the elements whose father is the "body" tag and whose
 * name starts with the "h" letter, such "h1", "h2", "h3" and so on.
 * </p>
 * 
 * <p>
 * Using the index selector:
 * </p>
 * 
 * <pre>
 * html/body/div[1]
 * </pre>
 * 
 * <p>
 * This criteria returns the second "div" element whose father is the "body"
 * tag. Note that the index lesser value is 0, just like in arrays.
 * </p>
 * 
 * <pre>
 * html/body/h*[2]
 * </pre>
 * 
 * <p>
 * This criteria returns the third element whose father is the "body" tag and
 * whose name starts with the "h" letter.
 * </p>
 * 
 * <p>
 * Using attribute selector(s):
 * </p>
 * 
 * <pre>
 * html/body/div(id=d1)
 * </pre>
 * 
 * <p>
 * This one searches for divs with an attribute called "id", whose value is
 * exactly "d1".
 * </p>
 * 
 * <p>
 * The star wildcard is admitted in the value part of the selector:
 * </p>
 * 
 * <pre>
 * html/body/div(id=*)
 * </pre>
 * 
 * <p>
 * This one searches for divs with an attribute called "id", regardless of its
 * value.
 * </p>
 * 
 * <pre>
 * html/body/div(id=d*)
 * </pre>
 * 
 * <p>
 * This one searches for divs with an attribute called "id", whose value starts
 * with the "d" letter.
 * </p>
 * 
 * <p>
 * More attribute selectors can be combined together:
 * </p>
 * 
 * <pre>
 * html/body/div(id=d*)(align=left)
 * </pre>
 * 
 * <p>
 * A index selector and two attribute selectors in this example:
 * </p>
 * 
 * <pre>
 * html/body/div[1](id=d*)(align=left)
 * </pre>
 * 
 * <p>
 * This will search for the second "div" tag, inside the "html"-"body" sequence,
 * whose attribute "id" has a value starting with "d" and whose attribute
 * "align" is exactly "left".
 * </p>
 * 
 * <p>
 * Search criterias admit a special token, called the "recursive deep token" and
 * represented by a sequence of three points.
 * </p>
 * 
 * <pre>
 * html/body/.../table
 * </pre>
 * 
 * <p>
 * This criteria will search for tables inside the body of the document,
 * regardless if they are placed straight under the "body" tag or not. This is,
 * of course, a recursive search within the body sub-elements. The criteria will
 * return all the tables like the following
 * </p>
 * 
 * <pre>
 * &lt;html&gt;&lt;body&gt;&lt;table&gt;...
 * </pre>
 * 
 * <p>
 * but it will return also all the ones like
 * </p>
 * 
 * <pre>
 * &lt;html&gt;&lt;body&gt;&lt;div&gt;&lt;div&gt;table&gt;...
 * </pre>
 * 
 * <p>
 * Escaping of reserved characters is possibile through the sequence
 * <em>&lt;xx&gt;</em>, where <em>xx</em> is the exadecimal code of the
 * escaped character.
 * </p>
 * 
 * @author Carlo Pelliccia
 */
public class Criteria {

	/**
	 * RegExp pattern to parse the whole search criteria.
	 * 
	 * Group 1: tag selector
	 * 
	 * Group 2: index selector
	 * 
	 * Group 3: attribute selectors
	 */
	private static Pattern tagPattern = Pattern
			.compile("^([^\\[\\]()]+)(?:\\[([0-9]+)\\])?"
					+ "(((?:\\((?:[^=]+(?:=[^)]+)?)?\\))*)?)$");

	/**
	 * RegExp pattern to parse the attribute selectors.
	 */
	private static Pattern attributesPattern = Pattern
			.compile("(?:\\((?:([^=]+)(?:=([^)]+))?)?\\))");

	/**
	 * RegExp pattern to find the escape sequences.
	 */
	private static Pattern escapeSequencePattern = Pattern
			.compile("<([0-9a-fA-F]{2})>");

	/**
	 * The conditions list.
	 */
	private Condition[] conditions;

	/**
	 * It parses and builds the criteria.
	 * 
	 * @param criteria
	 *            The criteria as a string.
	 * @throws InvalidCriteriaException
	 *             If the given string is not a valid criteria.
	 */
	public Criteria(String criteria) throws InvalidCriteriaException {
		if (criteria == null) {
			throw new InvalidCriteriaException("criteria cannot be null");
		}
		criteria = criteria.trim();
		if (criteria.length() == 0) {
			throw new InvalidCriteriaException("criteria cannot be empty");
		}
		ArrayList list = new ArrayList();
		StringTokenizer st = new StringTokenizer(criteria, "/");
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			Matcher matcher = tagPattern.matcher(token);
			if (!matcher.matches()) {
				throw new InvalidCriteriaException("invalid token: " + token);
			}
			String tagSelector = unescape(matcher.group(1));
			int indexSelector = matcher.group(2) != null ? Integer
					.parseInt(matcher.group(2)) : -1;
			String attributeSelectors = matcher.group(3);
			ArrayList asList = null;
			if (attributeSelectors != null && attributeSelectors.length() > 0) {
				asList = new ArrayList();
				matcher = attributesPattern.matcher(attributeSelectors);
				while (matcher.find()) {
					String attributeNameSelector = unescape(matcher.group(1));
					String attributeValueSelector = unescape(matcher.group(2));
					asList.add(new AttributeSelector(attributeNameSelector,
							attributeValueSelector));
				}
			}
			if (tagSelector.equals("...")
					&& (indexSelector >= 0 || asList != null)) {
				throw new InvalidCriteriaException(
						"invalid use of the \"...\" sequence");
			}
			list.add(new Condition(tagSelector, indexSelector, asList));
		}
		int size = list.size();
		conditions = new Condition[size];
		for (int i = 0; i < size; i++) {
			conditions[i] = (Condition) list.get(i);
		}
	}

	/**
	 * This method returns the number of the conditions in the criteria.
	 * 
	 * @return The number of the conditions in the criteria.
	 */
	public int getConditionCount() {
		return conditions.length;
	}

	/**
	 * This method returns a criteria condition.
	 * 
	 * @param index
	 *            The index of the wanted condition, starting from 0 to
	 *            getConditionCount() - 1.
	 * @return The requested condition.
	 */
	public Condition getCondition(int index) {
		return conditions[index];
	}

	/**
	 * Unescape the &lt;xx&gt; sequences.
	 */
	private String unescape(String str) {
		if (str == null) {
			return null;
		}
		String ret = "";
		int index = 0;
		Matcher matcher = escapeSequencePattern.matcher(str);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			String token = matcher.group(1);
			ret += str.substring(index, start);
			ret += (char) Integer.parseInt(token, 16);
			index = end;
		}
		if (index > 0) {
			int length = str.length();
			if (index < length) {
				ret += str.substring(index, length);
			}
			return ret;
		} else {
			return str;
		}
	}

}
