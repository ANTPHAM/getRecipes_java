package it.sauronsoftware.grab4j.html;

import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLImageElement;

/**
 * This class represents the HTML image tags ("img" tags).
 * 
 * @author Carlo Pelliccia
 */
public class HTMLImage extends HTMLTag {

	/**
	 * The image source URL.
	 */
	private String imageUrl;

	/**
	 * Builds the representation.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param imageTag
	 *            The W3C DOM ImageTag object to explore and wrap.
	 */
	HTMLImage(HTMLDocument document, HTMLElement parent,
			HTMLImageElement imageTag) {
		super(document, parent, imageTag);
		imageUrl = imageTag.getSrc();
	}

	/**
	 * Builds the representation.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param imageTag
	 *            The W3C DOM Element object to explore and wrap.
	 * @param src
	 *            The src attribute value.
	 */
	HTMLImage(HTMLDocument document, HTMLElement parent, Element imageTag,
			String src) {
		super(document, parent, imageTag);
		imageUrl = src;
	}

	/**
	 * This method extracts and returns the image source URL.
	 * 
	 * @return The image source URL, turned to an absolute URL if possibile.
	 */
	public String getImageURL() {
		return getDocument().buildURL(imageUrl);
	}

}
