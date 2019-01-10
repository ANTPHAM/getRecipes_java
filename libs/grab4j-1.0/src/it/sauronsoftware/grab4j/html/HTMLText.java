package it.sauronsoftware.grab4j.html;

import org.w3c.dom.Text;

/**
 * This class represents the textual nodes of a HTML document.
 * 
 * @author Carlo Pelliccia
 */
public class HTMLText extends HTMLElement {

	/**
	 * Builds the element.
	 * 
	 * @param document
	 *            The element document.
	 * @param parent
	 *            The parent element.
	 * @param text
	 *            The W3C DOM Text object to wrap.
	 */
	HTMLText(HTMLDocument document, HTMLElement parent, Text text) {
		super(document, parent, text);
	}

	/**
	 * This method returns the text inside the node.
	 * 
	 * @return The text inside the node.
	 */
	public String getText() {
		return getContent();
	}

	/**
	 * This method returns the text inside the node.
	 * 
	 * @return The text inside the node.
	 */
	public String getInnerText() {
		return getText();
	}

	public String toString() {
		return "TextElement[" + getText() + "]";
	}

}
