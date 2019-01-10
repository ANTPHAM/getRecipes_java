package it.sauronsoftware.grab4j.html.search;

/**
 * A plain structure used in search criteria involving attribute names and
 * values.
 * 
 * @author Carlo Pelliccia
 */
public class AttributeSelector {

	/**
	 * The attribute name.
	 */
	private String nameSelector;

	/**
	 * The attribute value pattern (optional).
	 */
	private WildcardPattern valueSelector;

	/**
	 * It builds the selector.
	 * 
	 * @param nameSelector
	 *            The attribute name.
	 * @param valueSelector
	 *            The attribute value pattern (optional). This is a wilcard
	 *            pattern, and can use the star character (*) to recognize any
	 *            characters sequence.
	 * @see it.sauronsoftware.grab4j.html.search.WildcardPattern
	 */
	AttributeSelector(String nameSelector, String valueSelector) {
		this.nameSelector = nameSelector;
		if (valueSelector != null) {
			this.valueSelector = new WildcardPattern(valueSelector, true);
		} else {
			this.valueSelector = null;
		}
	}

	/**
	 * This method returns the attribute name.
	 * 
	 * @return The attribute name.
	 */
	public String getNameSelector() {
		return nameSelector;
	}

	/**
	 * This method returns the attribute value pattern.
	 * 
	 * @return The attribute value pattern.
	 */
	public WildcardPattern getValueSelector() {
		return valueSelector;
	}

}
