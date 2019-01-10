package it.sauronsoftware.grab4j;

import it.sauronsoftware.grab4j.html.HTMLDocument;
import it.sauronsoftware.grab4j.html.HTMLDocumentFactory;
import it.sauronsoftware.grab4j.html.HTMLParseException;
import it.sauronsoftware.grab4j.toolkit.EnvironmentToolkit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * This is the grabber.
 * 
 * The documentation about the grab4j scripting features is given inside the
 * distribution package.
 * 
 * @author Carlo Pelliccia
 */
public class WebGrabber {

	/**
	 * This array caches the name of the methods placed inside the
	 * EnvironmentToolkit class.
	 */
	private static String[] cachedEnvironmentToolkitMethodNames = null;

	/**
	 * A lock for safe-synchronization during the reflection on the
	 * EnvironmentToolkit class.
	 */
	private static Object cachedEnvironmentToolkitMethodNamesLock = new Object();

	/**
	 * This method fetches, parses and grabs a HTML document.
	 * 
	 * @param url
	 *            The document address as an URL object.
	 * @param jsLogic
	 *            The script implementing the grabbing logic, as a string of
	 *            JavaScript code.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws IOException
	 *             If an I/O error occurs during the document retrieving.
	 * @throws HTMLParseException
	 *             If the file retrieved isn't a valid HTML document.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(URL url, String jsLogic) throws IOException,
			HTMLParseException, ScriptException {
		HTMLDocument document = HTMLDocumentFactory.buildDocument(url);
		return grab(document, jsLogic);
	}

	/**
	 * This method fetches, parses and grabs a HTML document.
	 * 
	 * @param url
	 *            The document address as an URL object.
	 * @param jsLogicReader
	 *            The script implementing the grabbing logic, as a reader from
	 *            which will be read the JavaScript code. Note that this reader
	 *            will be closed by the method.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws IOException
	 *             If an I/O error occurs during the document or the script
	 *             retrieving.
	 * @throws HTMLParseException
	 *             If the file retrieved isn't a valid HTML document.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(URL url, Reader jsLogicReader)
			throws IOException, HTMLParseException, ScriptException {
		HTMLDocument document = HTMLDocumentFactory.buildDocument(url);
		return grab(document, jsLogicReader);
	}

	/**
	 * This method fetches, parses and grabs a HTML document.
	 * 
	 * @param url
	 *            The document address as an URL object.
	 * @param jsLogicFile
	 *            The script implementing the grabbing logic, as a file
	 *            containing the JavaScript code.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws IOException
	 *             If an I/O error occurs during the document or the script
	 *             retrieving.
	 * @throws HTMLParseException
	 *             If the file retrieved isn't a valid HTML document.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(URL url, File jsLogicFile) throws IOException,
			HTMLParseException, ScriptException {
		HTMLDocument document = HTMLDocumentFactory.buildDocument(url);
		return grab(document, jsLogicFile);
	}

	/**
	 * This method grabs a HTML document.
	 * 
	 * @param document
	 *            The already parsed HTML document.
	 * @param jsLogicReader
	 *            The script implementing the grabbing logic, as a reader from
	 *            which will be read the JavaScript code. Note that this reader
	 *            will be closed by the method.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws IOException
	 *             If an I/O error occurs during the script retrieving.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(HTMLDocument document, Reader jsLogicReader)
			throws IOException, ScriptException {
		StringBuffer script = new StringBuffer();
		try {
			char[] buffer = new char[1024];
			int l;
			while ((l = jsLogicReader.read(buffer)) > -1) {
				script.append(buffer, 0, l);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				jsLogicReader.close();
			} catch (Throwable t) {
				;
			}
		}
		return grab(document, script.toString());
	}

	/**
	 * This method grabs a HTML document.
	 * 
	 * @param document
	 *            The already parsed HTML document.
	 * @param jsLogicFile
	 *            The script implementing the grabbing logic, as a file
	 *            containing the JavaScript code.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws IOException
	 *             If an I/O error occurs during the script retrieving.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(HTMLDocument document, File jsLogicFile)
			throws IOException, ScriptException {
		return grab(document, new FileReader(jsLogicFile));
	}

	/**
	 * This method grabs a HTML document.
	 * 
	 * @param document
	 *            The already parsed HTML document.
	 * @param jsLogic
	 *            The script implementing the grabbing logic, as a string of
	 *            JavaScript code.
	 * @return The value returned by the grabbing script, or null if no value is
	 *         returned by the script.
	 * @throws ScriptException
	 *             If the grabbing script fails.
	 */
	public static Object grab(HTMLDocument document, String jsLogic)
			throws ScriptException {
		String[] functions = getEnvironmentToolkitMethodNames();
		try {
			// Enter the interpreter context.
			Context context = Context.enter();
			// Create the interpreter environment.
			ImporterTopLevel environment = new ImporterTopLevel(context, true);
			// Add the EnvironmentToolkit functions to the interpreter
			// environment.
			environment.defineFunctionProperties(functions,
					EnvironmentToolkit.class, ScriptableObject.READONLY);
			// Push the document in the interpreter environment.
			Object jsDocument = Context.javaToJS(document, environment);
			ScriptableObject.putProperty(environment, "document", jsDocument);
			// Push the "result" reference in the interpreter environment.
			ScriptableObject.putProperty(environment, "result", null);
			// Run the script.
			try {
				context.evaluateString(environment, jsLogic, "logic", 1, null);
			} catch (Throwable t) {
				throw new ScriptException(t);
			}
			// Retrieve the value of the "result" reference.
			Object res = ScriptableObject.getProperty(environment, "result");
			// Unwrap (if necessary) and retun the result.
			return unwrap(res, environment);
		} catch (ScriptException e) {
			throw e;
		} finally {
			// Exit the context.
			Context.exit();
		}
	}

	/**
	 * Unwraps an object from its javascript wrapper.
	 */
	private static Object unwrap(Object obj, Scriptable environment) {
		if (obj instanceof Undefined) {
			return null;
		} else if (obj instanceof NativeArray) {
			NativeArray jsArray = (NativeArray) obj;
			int length = (int) jsArray.getLength();
			Object[] ret = new Object[length];
			for (int i = 0; i < length; i++) {
				ret[i] = unwrap(jsArray.get(i, environment), environment);
			}
			return ret;
		} else if (obj instanceof NativeJavaArray) {
			NativeJavaArray javaArray = (NativeJavaArray) obj;
			return javaArray.unwrap();
		} else if (obj instanceof NativeJavaObject) {
			NativeJavaObject javaObject = (NativeJavaObject) obj;
			return javaObject.unwrap();
		} else if (obj instanceof ScriptableObject) {
			ScriptableObject scriptable = (ScriptableObject) obj;
			return scriptable.getDefaultValue(String.class);
		} else {
			return obj;
		}
	}

	/**
	 * Utility method, useful to extract on-the-fly the name of the methods
	 * placed in the EnvironmentToolkit class.
	 */
	private static String[] getEnvironmentToolkitMethodNames() {
		synchronized (cachedEnvironmentToolkitMethodNamesLock) {
			if (cachedEnvironmentToolkitMethodNames == null) {
				ArrayList list = new ArrayList();
				Class clazz = EnvironmentToolkit.class;
				Method[] methods = clazz.getMethods();
				for (int i = 0; i < methods.length; i++) {
					int mod = methods[i].getModifiers();
					if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
						list.add(methods[i].getName());
					}
				}
				int size = list.size();
				cachedEnvironmentToolkitMethodNames = new String[size];
				for (int i = 0; i < size; i++) {
					cachedEnvironmentToolkitMethodNames[i] = (String) list
							.get(i);
				}
			}
			return cachedEnvironmentToolkitMethodNames;
		}
	}
}
