package it.sauronsoftware.grab4j;

/**
 * This kind of exception is thrown if an exception occurs in the script run by
 * the JavaScript interpreter used by the grabber.
 * 
 * @author Carlo Pelliccia
 */
public class ScriptException extends Exception {

	private static final long serialVersionUID = 1L;

	ScriptException() {
		super();
	}

	ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	ScriptException(String message) {
		super(message);
	}

	ScriptException(Throwable cause) {
		super(cause);
	}

}
