package it.sauronsoftware.grab4j.html.search;

/**
 * This kind of exception is thrown when an invalid criteria is encountered.
 * 
 * @author Carlo Pelliccia
 */
public class InvalidCriteriaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	InvalidCriteriaException(String message) {
		super(message);
	}

}
