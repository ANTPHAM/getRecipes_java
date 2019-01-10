package it.sauronsoftware.grab4j.html;

import it.sauronsoftware.grab4j.html.search.AttributeSelector;
import it.sauronsoftware.grab4j.html.search.Condition;
import it.sauronsoftware.grab4j.html.search.Criteria;
import it.sauronsoftware.grab4j.html.search.InvalidCriteriaException;
import it.sauronsoftware.grab4j.html.search.WildcardPattern;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLBaseElement;
import org.w3c.dom.html.HTMLImageElement;

/**
 * Package reserved utilities.
 * 
 * @author Carlo Pelliccia
 */
class HTMLUtils {

	/**
	 * This method builds a document element.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param node
	 *            The W3C DOM Node object to wrap.
	 * @return La rappresentazione dell'elemento.
	 */
	static HTMLElement buildElement(HTMLDocument document, HTMLElement parent,
			Node node) {
		if (node instanceof HTMLAnchorElement) {
			HTMLAnchorElement aux = (HTMLAnchorElement) node;
			return new HTMLLink(document, parent, aux);
		}
		if (node instanceof HTMLImageElement) {
			HTMLImageElement aux = (HTMLImageElement) node;
			return new HTMLImage(document, parent, aux);
		}
		if (node instanceof HTMLBaseElement) {
			HTMLBaseElement aux = (HTMLBaseElement) node;
			String href = aux.getHref();
			if (href != null) {
				document.setBase(href);
			}
		}
		if (node instanceof Element) {
			Element aux = (Element) node;
			String tagName = aux.getTagName();
			if (tagName.equalsIgnoreCase("a")) {
				String href = aux.getAttribute("href");
				if (href != null) {
					return new HTMLLink(document, parent, aux, href);
				}
			}
			if (tagName.equalsIgnoreCase("img")) {
				String src = aux.getAttribute("src");
				if (src != null) {
					return new HTMLImage(document, parent, aux, src);
				}
			}
			if (tagName.equalsIgnoreCase("base")) {
				String href = aux.getAttribute("href");
				if (href != null) {
					document.setBase(href);
				}
			}
			return new HTMLTag(document, parent, aux);
		}
		if (node instanceof Text) {
			Text aux = (Text) node;
			String text = aux.getNodeValue().trim();
			if (text.length() == 0) {
				return null;
			} else {
				return new HTMLText(document, parent, aux);
			}
		}
		return null;
	}

	/**
	 * This method explores a set of elements and their sub-elements
	 * (recursively), searching the first occurrence of an element with the
	 * given value in its "id" attribute.
	 * 
	 * @param elements
	 *            The elements set.
	 * @param id
	 *            The id.
	 * @return The element, or null if not found.
	 */
	static HTMLElement getElementById(HTMLElement[] elements, String id) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof HTMLTag) {
				HTMLTag tag = (HTMLTag) elements[i];
				String aux = tag.getAttribute("id");
				if (aux != null && aux.equals(id)) {
					return tag;
				}
			}
		}
		for (int i = 0; i < elements.length; i++) {
			HTMLElement aux = getElementById(elements[i].getElements(), id);
			if (aux != null) {
				return aux;
			}
		}
		return null;
	}

	/**
	 * This method searches recursively inside a set of elements, selecting the
	 * ones whose name is equal to the given tag name.
	 * 
	 * @param elements
	 *            The elements set.
	 * @param tagName
	 *            The tag name.
	 * @return The elements that satisfy the search criteria.
	 */
	static HTMLElement[] getElementsByTag(HTMLElement[] elements, String tagName) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof HTMLTag) {
				HTMLTag tag = (HTMLTag) elements[i];
				String aux = tag.getTagName();
				if (aux.equalsIgnoreCase(tagName)) {
					list.add(tag);
				}
			}
			HTMLElement[] aux = getElementsByTag(elements[i].getElements(),
					tagName);
			for (int j = 0; j < aux.length; j++) {
				list.add(aux[j]);
			}
		}
		int size = list.size();
		HTMLElement[] ret = new HTMLElement[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (HTMLElement) list.get(i);
		}
		return ret;
	}

	/**
	 * This method searches recursively inside a set of elements, selecting the
	 * ones whose have a given attribute with a given value.
	 * 
	 * @param elements
	 *            The elements set.
	 * @param attributeName
	 *            The attribute name.
	 * @param attributeValue
	 *            The attribute value.
	 * @return The elements that satisfy the search criteria.
	 */
	static HTMLElement[] getElementsByAttribute(HTMLElement[] elements,
			String attributeName, String attributeValue) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof HTMLTag) {
				HTMLTag tag = (HTMLTag) elements[i];
				String aux = tag.getAttribute(attributeName);
				if (aux != null && aux.equals(attributeValue)) {
					list.add(tag);
				}
			}
			HTMLElement[] aux = getElementsByAttribute(elements[i]
					.getElements(), attributeName, attributeValue);
			for (int j = 0; j < aux.length; j++) {
				list.add(aux[j]);
			}
		}
		int size = list.size();
		HTMLElement[] ret = new HTMLElement[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (HTMLElement) list.get(i);
		}
		return ret;
	}

	/**
	 * This method searches recursively inside a set of elements.
	 * 
	 * @param elements
	 *            The elements set.
	 * @param searchCriteria
	 *            The search criteria.
	 * @return The elements that satisfy the search criteria.
	 * @throws InvalidCriteriaException
	 *             If the given criteria is invalid.
	 * @see it.sauronsoftware.grab4j.html.search.Criteria
	 */
	static HTMLElement[] searchElements(HTMLElement[] elements,
			String searchCriteria) throws InvalidCriteriaException {
		return searchElements(elements, new Criteria(searchCriteria));
	}

	/**
	 * This method searches recursively inside a set of elements.
	 * 
	 * @param elements
	 *            The elements set.
	 * @param searchCriteria
	 *            The search criteria.
	 * @return The elements that satisfy the search criteria.
	 */
	static HTMLElement[] searchElements(HTMLElement[] elements,
			Criteria searchCriteria) {
		ArrayList res = privateSearchElements(elements, searchCriteria, 0);
		if (res != null) {
			int size = res.size();
			HTMLElement[] retValue = new HTMLElement[size];
			for (int i = 0; i < size; i++) {
				retValue[i] = (HTMLElement) res.get(i);
			}
			return retValue;
		} else {
			return new HTMLElement[0];
		}
	}

	/**
	 * Recursive utility method.
	 */
	private static ArrayList privateSearchElements(HTMLElement[] elements,
			Criteria criteria, int criteriaOffset) {
		ArrayList retValue = null;
		boolean lastStep = criteriaOffset == criteria.getConditionCount() - 1;
		Condition current = criteria.getCondition(criteriaOffset);
		WildcardPattern tagSelector = current.getTagSelector();
		String pattern = tagSelector.getPattern();
		if (!pattern.equals("...")) {
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof HTMLTag) {
					HTMLTag tag = (HTMLTag) elements[i];
					boolean matches = tagSelector.matches(tag.getTagName());
					if (matches) {
						int index = current.getIndexSelector();
						if (index >= 0) {
							int currInd = -1;
							for (int j = 0; j < i; j++) {
								if (elements[j] instanceof HTMLTag) {
									HTMLTag tag2 = (HTMLTag) elements[j];
									if (tagSelector.matches(tag2.getTagName())) {
										currInd++;
									}
								}
							}
							currInd++;
							matches = currInd == index;
						}
					}
					if (matches) {
						AttributeSelector[] selectors = current
								.getAttributeSelectors();
						for (int j = 0; j < selectors.length; j++) {
							String aux = tag.getAttribute(selectors[j]
									.getNameSelector());
							if (aux == null) {
								matches = false;
								break;
							} else {
								WildcardPattern valueSelector = selectors[j]
										.getValueSelector();
								if (valueSelector != null) {
									matches = valueSelector.matches(aux);
								}
							}
						}
					}
					if (matches) {
						if (lastStep) {
							if (retValue == null) {
								retValue = new ArrayList();
							}
							retValue.add(tag);
						} else {
							ArrayList returned = privateSearchElements(tag
									.getElements(), criteria,
									criteriaOffset + 1);
							if (returned != null) {
								if (retValue == null) {
									retValue = new ArrayList();
								}
								int size = returned.size();
								for (int k = 0; k < size; k++) {
									retValue.add(returned.get(k));
								}
							}
						}
					}
				}
			}
		} else if (!lastStep) {
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof HTMLTag) {
					HTMLTag tag = (HTMLTag) elements[i];
					ArrayList returned1 = privateSearchElements(tag
							.getElements(), criteria, criteriaOffset);
					if (returned1 != null) {
						if (retValue == null) {
							retValue = new ArrayList();
						}
						int size = returned1.size();
						for (int k = 0; k < size; k++) {
							retValue.add(returned1.get(k));
						}
					}
					ArrayList returned2 = privateSearchElements(tag
							.getElements(), criteria, criteriaOffset + 1);
					if (returned2 != null) {
						if (retValue == null) {
							retValue = new ArrayList();
						}
						int size = returned2.size();
						for (int k = 0; k < size; k++) {
							retValue.add(returned2.get(k));
						}
					}
				}
			}
		}
		return retValue;
	}

	/**
	 * This method parses the value of a HTTP "Content-Type" header.
	 * 
	 * @param contentType
	 *            The value of the HTTP "Content-Type" header.
	 * @return The charset in the header, or null if not found.
	 */
	static String getCharset(String contentType) {
		String charset = null;
		if (contentType != null) {
			StringTokenizer st1 = new StringTokenizer(contentType, ";");
			out: while (st1.hasMoreTokens()) {
				String token = st1.nextToken().trim();
				StringTokenizer st2 = new StringTokenizer(token, "=");
				if (st2.countTokens() == 2) {
					String key = st2.nextToken().trim();
					if (key.equalsIgnoreCase("charset")) {
						charset = st2.nextToken().trim();
						break out;
					}
				}
			}
		}
		return charset;
	}

}
