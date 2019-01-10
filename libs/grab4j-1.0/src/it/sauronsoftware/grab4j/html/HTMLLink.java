package it.sauronsoftware.grab4j.html;

import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;

/**
 * This class represents the HTML link tags ("a" tags).
 * 
 * @author Carlo Pelliccia
 */
public class HTMLLink extends HTMLTag {

	/**
	 * The link URL.
	 */
	private String linkUrl;

	/**
	 * Builds the representation.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param linkTag
	 *            The W3C DOM LinkTag object to explore and wrap.
	 */
	HTMLLink(HTMLDocument document, HTMLElement parent,
			HTMLAnchorElement linkTag) {
		super(document, parent, linkTag);
		linkUrl = linkTag.getHref();
	}

	/**
	 * Builds the representation.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param linkTag
	 *            The W3C Element object to explore and wrap.
	 * @param href
	 *            The href attribute value.
	 */
	HTMLLink(HTMLDocument document, HTMLElement parent, Element linkTag,
			String href) {
		super(document, parent, linkTag);
		linkUrl = href;
	}

	/**
	 * This method extracts and returns the link URL.
	 * 
	 * @return The link URL, turned to an absolute URL if possibile.
	 */
	public String getLinkURL() {
		return getDocument().buildURL(linkUrl);
	}

}
