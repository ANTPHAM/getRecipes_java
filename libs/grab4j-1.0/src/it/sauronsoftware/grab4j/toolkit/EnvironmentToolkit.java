package it.sauronsoftware.grab4j.toolkit;

import it.sauronsoftware.grab4j.html.HTMLDocument;
import it.sauronsoftware.grab4j.html.HTMLDocumentFactory;
import it.sauronsoftware.grab4j.html.HTMLEntities;

/**
 * A set of functions for the JavaScript interpreter environment. This is not
 * useful for the Java programmer, since all those functions are available in
 * other packages and they are represented here just for the interpreter
 * convenience.
 * 
 * @author Carlo Pelliccia
 */
public class EnvironmentToolkit {

	public static void print(String message) {
		System.out.println(message);
	}

	public static HTMLDocument openDocument(String url) throws Exception {
		return HTMLDocumentFactory.buildDocument(url);
	}

	public static String encodeEntities(String str) {
		return HTMLEntities.encode(str);
	}

	public static String decodeEntities(String str) {
		return HTMLEntities.decode(str);
	}

}
