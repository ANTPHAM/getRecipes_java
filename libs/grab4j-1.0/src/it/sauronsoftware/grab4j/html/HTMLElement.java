package it.sauronsoftware.grab4j.html;

import it.sauronsoftware.grab4j.html.search.Criteria;
import it.sauronsoftware.grab4j.html.search.InvalidCriteriaException;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Base abstract model for HTML elements.
 * 
 * @author Carlo Pelliccia
 */
public abstract class HTMLElement {

	/**
	 * The document owner of the element.
	 */
	private HTMLDocument document;

	/**
	 * The parent element.
	 */
	private HTMLElement parent;

	/**
	 * The previous element.
	 */
	private HTMLElement previous;

	/**
	 * The next element.
	 */
	private HTMLElement next;

	/**
	 * The sub-elements array.
	 */
	private HTMLElement[] children;

	/**
	 * The content of the element.
	 */
	private String content;

	/**
	 * Builds the element.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param node
	 *            The W3C DOM Node object to explore and wrap.
	 */
	HTMLElement(HTMLDocument document, HTMLElement parent, Node node) {
		// Associa il documento.
		this.document = document;
		// Associa il genitore.
		this.parent = parent;
		// Estrae i valori di interesse.
		content = node.getNodeValue();
		// Esplora i nodi figli.
		ArrayList auxList = new ArrayList();
		if (node.hasChildNodes()) {
			NodeList list = node.getChildNodes();
			if (list != null) {
				int size = list.getLength();
				for (int i = 0; i < size; i++) {
					HTMLElement el = HTMLUtils.buildElement(document, this,
							list.item(i));
					if (el != null) {
						auxList.add(el);
					}
				}
			}
		}
		int size = auxList.size();
		children = new HTMLElement[size];
		for (int i = 0; i < size; i++) {
			children[i] = (HTMLElement) auxList.get(i);
			if (i > 0) {
				children[i].setPreviousElement(children[i - 1]);
				children[i - 1].setNextElement(children[i]);
			}
		}
	}

	/**
	 * This method returns the document owner of the element.
	 * 
	 * @return he document owner of the element.
	 */
	public HTMLDocument getDocument() {
		return document;
	}

	/**
	 * This method returns the parent element.
	 * 
	 * @return The parent element.
	 */
	public HTMLElement getParentElement() {
		return parent;
	}

	/**
	 * This method returns the next element.
	 * 
	 * @return The next element (null if no more elements in the group).
	 */
	public HTMLElement getNextElement() {
		return next;
	}

	/**
	 * This method sets the next element.
	 * 
	 * @param next
	 *            The next element.
	 */
	void setNextElement(HTMLElement next) {
		this.next = next;
	}

	/**
	 * This method returns the previous element.
	 * 
	 * @return The previous element (null if this one is the first element in
	 *         its group).
	 */
	public HTMLElement getPreviousElement() {
		return previous;
	}

	/**
	 * This method sets the previous element.
	 * 
	 * @param previous
	 *            The previous element.
	 */
	void setPreviousElement(HTMLElement previous) {
		this.previous = previous;
	}

	/**
	 * This method returns the element content.
	 * 
	 * @return The element content.
	 */
	protected String getContent() {
		return content;
	}

	/**
	 * This method returns a sub-element.
	 * 
	 * @param index
	 *            The index of the wanted sub-element, starting from 0 untill
	 *            getElementCount() - 1.
	 * @return The suggested sub-element.
	 */
	public HTMLElement getElement(int index) {
		return children[index];
	}

	/**
	 * This method returns the number of the sub-elements owned by the element.
	 * 
	 * @return The number of the sub-elements.
	 */
	public int getElementCount() {
		return children.length;
	}

	/**
	 * This method returns an array with the sub-elements.
	 * 
	 * @return An array with the sub-elements.
	 */
	public HTMLElement[] getElements() {
		return children;
	}

	/**
	 * This method explores recursively the element children, searching the
	 * first occurrence of an element with the given value in its "id"
	 * attribute.
	 * 
	 * @param id
	 *            The id of the wanted sub-element.
	 * @return The element, or null if not found.
	 */
	public HTMLElement getElementById(String id) {
		return HTMLUtils.getElementById(children, id);
	}

	/**
	 * This method searches recursively inside the element children, selecting
	 * the ones whose name is equal to the given tag name.
	 * 
	 * @param tagName
	 *            The tag name.
	 * @return An array with the sub-elements that satisfy the search criteria.
	 */
	public HTMLElement[] getElementsByTag(String tagName) {
		return HTMLUtils.getElementsByTag(children, tagName);
	}

	/**
	 * This method searches recursively inside the element children, selecting
	 * the ones whose have a given attribute with a given value.
	 * 
	 * @param attributeName
	 *            The attribute name.
	 * @param attributeValue
	 *            The attribute value.
	 * @return An array with the sub-elements that satisfy the search criteria.
	 */
	public HTMLElement[] getElementsByAttribute(String attributeName,
			String attributeValue) {
		return HTMLUtils.getElementsByAttribute(children, attributeName,
				attributeValue);
	}

	/**
	 * This method searches recursively inside the element children.
	 * 
	 * @param searchCriteria
	 *            The search criteria.
	 * @return An array with the sub-elements that satisfy the search criteria.
	 * @throws InvalidCriteriaException
	 *             If the given criteria is invalid.
	 * @see it.sauronsoftware.grab4j.html.search.Criteria
	 */
	public HTMLElement[] searchElements(String searchCriteria)
			throws InvalidCriteriaException {
		return HTMLUtils.searchElements(children, searchCriteria);
	}

	/**
	 * This method searches recursively inside the element children and returns
	 * the first occurrence of the results list.
	 * 
	 * @param searchCriteria
	 *            The search criteria.
	 * @return The first occurrence of the results list, or null if no result is
	 *         found.
	 * @throws InvalidCriteriaException
	 *             If the given criteria is invalid.
	 * @see it.sauronsoftware.grab4j.html.search.Criteria
	 */
	public HTMLElement searchElement(String searchCriteria)
			throws InvalidCriteriaException {
		HTMLElement[] res = searchElements(searchCriteria);
		if (res.length > 0) {
			return res[0];
		} else {
			return null;
		}
	}

	/**
	 * This method searches recursively inside the element children.
	 * 
	 * @param searchCriteria
	 *            The search criteria.
	 * @return An array with the sub-elements that satisfy the search criteria.
	 */
	public HTMLElement[] searchElements(Criteria searchCriteria) {
		return HTMLUtils.searchElements(children, searchCriteria);
	}

	/**
	 * This method searches recursively inside the element children and returns
	 * the first occurrence of the results list.
	 * 
	 * @param searchCriteria
	 *            The search criteria.
	 * @return The first occurrence of the results list, or null if no result is
	 *         found.
	 */
	public HTMLElement searchElement(Criteria searchCriteria) {
		HTMLElement[] res = searchElements(searchCriteria);
		if (res.length > 0) {
			return res[0];
		} else {
			return null;
		}
	}

}
