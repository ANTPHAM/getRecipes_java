package it.sauronsoftware.grab4j.html;

/**
 * This kind of exception is thrown if the parse of a HTML document fails. This
 * means that the document is not at all HTML or, at most, it is not valid HTML.
 * 
 * @author Carlo Pelliccia
 */
public class HTMLParseException extends Exception {

	private static final long serialVersionUID = 1L;

	HTMLParseException() {
		super();
	}

	HTMLParseException(String message) {
		super(message);
	}

	HTMLParseException(Throwable cause) {
		super(cause);
	}

	HTMLParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
