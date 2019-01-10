package it.sauronsoftware.grab4j.html;

import java.util.Enumeration;
import java.util.Properties;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class represents the HTML tags.
 * 
 * @author Carlo Pelliccia
 */
public class HTMLTag extends HTMLElement {

	/**
	 * The tag name.
	 */
	private String tagName;

	/**
	 * The tag attributes
	 */
	private Properties attributes = new Properties();

	/**
	 * Is this tag empty?
	 */
	private boolean empty;

	/**
	 * Builds the tag.
	 * 
	 * @param document
	 *            The document.
	 * @param parent
	 *            The parent element.
	 * @param tagNode
	 *            The W3C DOM Element object to explore and wrap.
	 */
	HTMLTag(HTMLDocument document, HTMLElement parent,
			Element tagNode) {
		super(document, parent, tagNode);
		tagName = tagNode.getTagName();
		empty = !tagNode.hasChildNodes();
		if (tagNode.hasAttributes()) {
			NamedNodeMap attrs = tagNode.getAttributes();
			int size = attrs.getLength();
			for (int i = 0; i < size; i++) {
				Node attr = attrs.item(i);
				String name = attr.getNodeName();
				String value = attr.getNodeValue();
				if (value == null) {
					value = "";
				}
				attributes.setProperty(name, value);
			}
		}
	}

	/**
	 * This method returns the value of an attribute of the tag.
	 * 
	 * @param attributeName
	 *            The attribute name.
	 * @return The attribute value, or null if the tag doesn't have the
	 *         suggested attribute.
	 */
	public String getAttribute(String attributeName) {
		return attributes.getProperty(attributeName.toLowerCase());
	}

	/**
	 * This method returns the tag name.
	 * 
	 * @return The tag name.
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * This method tests if the tag has a content.
	 * 
	 * @return true if the tag is empty.
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * This method returns the HTML code with the tag and its contents.
	 * 
	 * @return The HTML code of the tag and its contents.
	 */
	public String getOuterHTML() {
		String tagLine = tagName;
		Enumeration keys = attributes.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = HTMLEntities.encode(attributes.getProperty(key));
			tagLine += " " + key + "=\"" + value + "\"";
		}
		if (empty) {
			return "<" + tagLine + " />";
		} else {
			return "<" + tagLine + ">" + getInnerHTML() + "</" + tagName + ">";
		}
	}

	/**
	 * This method returns the HTML code in the tag contents.
	 * 
	 * @return The HTML code in the tag contents.
	 */
	public String getInnerHTML() {
		StringBuffer buffer = new StringBuffer();
		HTMLElement[] children = getElements();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof HTMLTag) {
				HTMLTag tag = (HTMLTag) children[i];
				buffer.append(tag.getOuterHTML());
			} else {
				buffer.append(HTMLEntities.encode(children[i].getContent()));
			}
		}
		return buffer.toString();
	}

	/**
	 * This method extracts a plain text from the tag contents.
	 * 
	 * @return A plain text representation of the tag contents.
	 */
	public String getInnerText() {
		StringBuffer buffer = new StringBuffer();
		HTMLElement[] children = getElements();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof HTMLTag) {
				HTMLTag tag = (HTMLTag) children[i];
				buffer.append(tag.getInnerText());
				buffer.append(' ');
			} else {
				buffer.append(children[i].getContent());
			}
		}
		return buffer.toString().replace('\u00A0', ' ').replaceAll("\\s+", " ")
				.trim();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TagElement[");
		buffer.append(tagName);
		Enumeration keys = attributes.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = HTMLEntities.encode(attributes.getProperty(key));
			buffer.append(' ');
			buffer.append(key);
			buffer.append('=');
			buffer.append(value);
		}
		buffer.append(']');
		return buffer.toString();
	}

}
