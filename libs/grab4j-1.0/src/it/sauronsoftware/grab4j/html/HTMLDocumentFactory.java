package it.sauronsoftware.grab4j.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A factory for HTML document representations.
 * 
 * Call this class buildDocument() static method to fetch and parse a HTML
 * document.
 * 
 * @author Carlo Pelliccia
 */
public class HTMLDocumentFactory {

	/**
	 * This method fetches and parses a HTML document.
	 * 
	 * @param documentUrl
	 *            The document URL.
	 * @return A document object representation.
	 * @throws IOException
	 *             This exception is thrown if an I/O error occurs.
	 * @throws HTMLParseException
	 *             This exception is thrown if the document cannot be parsed,
	 *             since it doesn't result to be valid HTML.
	 */
	public static HTMLDocument buildDocument(URL documentUrl)
			throws IOException, HTMLParseException {
		URLConnection connection = documentUrl.openConnection();
		String charset = HTMLUtils.getCharset(connection.getContentType());
		Reader reader = null;
		if (charset != null) {
			reader = new InputStreamReader(connection.getInputStream(), charset);
		} else {
			reader = new InputStreamReader(connection.getInputStream());
		}
		HTMLDocument doc = buildDocument(reader);
		doc.setURL(documentUrl);
		return doc;
	}

	/**
	 * This method fetches and parses a HTML document.
	 * 
	 * @param documentUrl
	 *            The document URL, as a string.
	 * @return A document object representation.
	 * @throws IOException
	 *             This exception is thrown if an I/O error occurs.
	 * @throws HTMLParseException
	 *             This exception is thrown if the document cannot be parsed,
	 *             since it doesn't result to be valid HTML.
	 */
	public static HTMLDocument buildDocument(String documentUrl)
			throws IOException, HTMLParseException {
		return HTMLDocumentFactory.buildDocument(new URL(documentUrl));
	}

	/**
	 * This method reads and parses a HTML document.
	 * 
	 * @param reader
	 *            The reader from which the document will be read.
	 * @return A document object representation.
	 * @throws IOException
	 *             This exception is thrown if an I/O error occurs
	 * @throws HTMLParseException
	 *             This exception is thrown if the document cannot be parsed,
	 *             since it doesn't result to be valid HTML.
	 */
	public static HTMLDocument buildDocument(Reader reader) throws IOException,
			HTMLParseException {
		try {
			return buildDocument(new InputSource(reader));
		} catch (IOException e) {
			throw e;
		} catch (HTMLParseException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

	/**
	 * This method reads and parses a HTML document.
	 * 
	 * @param inputStream
	 *            The inputStream from which the document will be read.
	 * @return A document object representation.
	 * @throws IOException
	 *             This exception is thrown if an I/O error occurs
	 * @throws HTMLParseException
	 *             This exception is thrown if the document cannot be parsed,
	 *             since it doesn't result to be valid HTML.
	 */
	public static HTMLDocument buildDocument(InputStream inputStream)
			throws IOException, HTMLParseException {
		try {
			return buildDocument(new InputSource(inputStream));
		} catch (IOException e) {
			throw e;
		} catch (HTMLParseException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

	/**
	 * This method reads and parses a HTML document.
	 * 
	 * @param inputSource
	 *            The inputSource from which the document will be read.
	 * @return A document object representation.
	 * @throws IOException
	 *             This exception is thrown if an I/O error occurs
	 * @throws HTMLParseException
	 *             This exception is thrown if the document cannot be parsed,
	 *             since it doesn't result to be valid HTML.
	 */
	public static HTMLDocument buildDocument(InputSource inputSource)
			throws IOException, HTMLParseException {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (SAXException e) {
			throw new HTMLParseException(e);
		}
		org.w3c.dom.html.HTMLDocument document;
		try {
			document = (org.w3c.dom.html.HTMLDocument) parser.getDocument();
		} catch (ClassCastException e) {
			throw new HTMLParseException("Document is not HTML", e);
		}
		return new HTMLDocument(document);
	}

}
