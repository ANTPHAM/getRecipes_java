package it.sauronsoftware.grab4j.html;

import it.sauronsoftware.grab4j.html.search.Criteria;
import it.sauronsoftware.grab4j.html.search.InvalidCriteriaException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.NodeList;

/**
 * A HTML document representation.
 * 
 * @author Carlo Pelliccia
 */
public class HTMLDocument {

	/**
	 * Document URL.
	 */
	private URL url = null;

	/**
	 * Document base URL, if one.
	 */
	private String base = null;

	/**
	 * The elements at the document root.
	 */
	private HTMLElement[] elements;

	/**
	 * Builds the document representation.
	 * 
	 * @param document
	 *            The document as a W3C DOME HTMLDocument object.
	 */
	HTMLDocument(org.w3c.dom.html.HTMLDocument document) {
		ArrayList list = new ArrayList();
		if (document.hasChildNodes()) {
			NodeList nodeList = document.getChildNodes();
			int size = nodeList.getLength();
			for (int i = 0; i < size; i++) {
				HTMLElement el = HTMLUtils.buildElement(this, null, nodeList
						.item(i));
				if (el != null) {
					list.add(el);
				}
			}
		}
		int size = list.size();
		elements = new HTMLElement[size];
		for (int i = 0; i < size; i++) {
			elements[i] = (HTMLElement) list.get(i);
			if (i > 0) {
				elements[i].setPreviousElement(elements[i - 1]);
				elements[i - 1].setNextElement(elements[i]);
			}
		}
	}

	/**
	 * Utility method. It builds an absolute URL starting from a relative URI,
	 * using the document URL and the (optional) document base URL.
	 */
	String buildURL(String uri) {
		try {
			// Absolute?
			return new URL(uri).toExternalForm();
		} catch (MalformedURLException e1) {
			// Relative!
			URL baseUrl = null;
			if (base != null) {
				try {
					baseUrl = new URL(base);
				} catch (MalformedURLException e2) {
					if (url != null) {
						try {
							baseUrl = new URL(url, base);
						} catch (MalformedURLException e3) {
							;
						}
					}
				}
			}
			if (baseUrl != null) {
				try {
					return new URL(baseUrl, uri).toExternalForm();
				} catch (MalformedURLException e2) {
					return uri;
				}
			} else if (url != null) {
				try {
					return new URL(url, uri).toExternalForm();
				} catch (MalformedURLException e2) {
					return uri;
				}
			} else {
				return uri;
			}
		}
	}

	/**
	 * This method sets the document source URL.
	 * 
	 * @param url
	 *            The document source URL.
	 */
	public void setURL(URL url) {
		this.url = url;
	}

	/**
	 * This method returns the document source URL.
	 * 
	 * @return The document source URL.
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * This method returns the element at the given index.
	 * 
	 * @param index
	 *            The index, starting from 0 untill getElementCount() - 1.
	 * @return The element at the given index.
	 */
	public HTMLElement getElement(int index) {
		return elements[index];
	}

	/**
	 * This method returns the number of the first-level elements in the
	 * document.
	 * 
	 * @return The number of the first-level elements in the document
	 */
	public int getElementCount() {
		return elements.length;
	}

	/**
	 * This method returns an array with all the document first-level elements.
	 * 
	 * @return An array with all the document first-level elements
	 */
	public HTMLElement[] getElements() {
		return elements;
	}

	/**
	 * This method explores recursively the document elements, searching the
	 * first occurrence of an element with the given value in its "id"
	 * attribute.
	 * 
	 * @param id
	 *            The id of the wanted sub-element.
	 * @return The element, or null if not found.
	 */
	public HTMLElement getElementById(String id) {
		return HTMLUtils.getElementById(elements, id);
	}

	/**
	 * This method searches recursively inside the document elements, selecting
	 * the ones whose name is equal to the given tag name.
	 * 
	 * @param tagName
	 *            The tag name.
	 * @return An array with the elements that satisfy the search criteria.
	 */
	public HTMLElement[] getElementsByTag(String tagName) {
		return HTMLUtils.getElementsByTag(elements, tagName);
	}

	/**
	 * This method searches recursively inside the document elements, selecting
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
		return HTMLUtils.getElementsByAttribute(elements, attributeName,
				attributeValue);
	}

	/**
	 * This method searches recursively inside the document elements.
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
		return HTMLUtils.searchElements(elements, searchCriteria);
	}

	/**
	 * This method searches recursively inside the document elements and returns
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
	 * This method searches recursively inside the document elements.
	 * 
	 * @param searchCriteria
	 *            The search criteria.
	 * @return An array with the sub-elements that satisfy the search criteria.
	 */
	public HTMLElement[] searchElements(Criteria searchCriteria) {
		return HTMLUtils.searchElements(elements, searchCriteria);
	}

	/**
	 * This method searches recursively inside the document elements and returns
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

	/**
	 * This method returns the document base URL, if there's one.
	 * 
	 * @param base
	 *            The document base URL, if there's one.
	 */
	void setBase(String base) {
		this.base = base;
	}

	public String toString() {
		return "Document[url=" + url + "]";
	}

}
