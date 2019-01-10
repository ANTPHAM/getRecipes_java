package it.sauronsoftware.grab4j.html.search;

import java.util.ArrayList;

/**
 * This class represents criteria conditions.
 * 
 * @author Carlo Pelliccia
 */
public class Condition {

	/**
	 * Tag name pattern.
	 */
	private WildcardPattern tagSelector;

	/**
	 * Tag index (if &lt; 0 no selector is used on index).
	 */
	private int indexSelector;

	/**
	 * Attributes restrictions.
	 */
	private AttributeSelector[] attributeSelectors;

	/**
	 * It builds the criteria condition.
	 * 
	 * @param tagSelector
	 *            A tag name pattern. This is a wildacrd pattern, and it can use
	 *            the star character to recognize any characters sequence.
	 * @param indexSelector
	 *            The tag index (if &lt; 0 no selector is used on index).
	 * @param attributeSelectors
	 *            Attributes restrictions.
	 * @see it.sauronsoftware.grab4j.html.search.WildcardPattern
	 */
	Condition(String tagSelector, int indexSelector,
			ArrayList attributeSelectors) {
		this.tagSelector = new WildcardPattern(tagSelector, true);
		this.indexSelector = indexSelector;
		if (attributeSelectors != null) {
			int size = attributeSelectors.size();
			AttributeSelector[] aux = new AttributeSelector[size];
			for (int i = 0; i < size; i++) {
				aux[i] = (AttributeSelector) attributeSelectors.get(i);
			}
			this.attributeSelectors = aux;
		} else {
			this.attributeSelectors = new AttributeSelector[0];
		}
	}

	/**
	 * This method returns the restrictions on attributes.
	 * 
	 * @return The restrictions on attributes
	 */
	public AttributeSelector[] getAttributeSelectors() {
		return attributeSelectors;
	}

	/**
	 * This method returns the restriction on the tag index. If less than 0 then
	 * no restriction on the index should be used.
	 * 
	 * @return The restriction on the tag index. If less than 0 then no
	 *         restriction on the index should be used.
	 */
	public int getIndexSelector() {
		return indexSelector;
	}

	/**
	 * This method returns a pattern representing the restriction on the name of
	 * the tag.
	 * 
	 * @return A pattern representing the restriction on the name of the tag.
	 */
	public WildcardPattern getTagSelector() {
		return tagSelector;
	}

}
