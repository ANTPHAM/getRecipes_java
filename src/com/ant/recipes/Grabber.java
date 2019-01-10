package com.aubert.recipes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Grabber {

	public static String ROOT_OUTPUT_PATH = "F:\\temp\\ML\\recette_Marmiton\\";
	private static String OUTPUT_FILE_PATH = ROOT_OUTPUT_PATH + "assets\\output.xml";
	private static int NB_THREADS = 5;
	
	public static void main(String[] args) throws Throwable {
		
		Grabber grabber = new Grabber();
		grabber.grab();
	}

	private void grab() {
		
		try
		{
			// Load state:
			log("Loading...");
			Document inputDoc = createInput(OUTPUT_FILE_PATH);

			GrabbedRecipes.getInstance().loadState(inputDoc);

			MarmitonCrawler.init();
			
			ArrayList<MarmitonCrawler> threads = new ArrayList<MarmitonCrawler>();
			MarmitonCrawler currentThread;
			for (int currentThreadNb = 0; currentThreadNb < NB_THREADS; currentThreadNb++)
			{
				currentThread = new MarmitonCrawler(currentThreadNb);
				
				// Start Crawler:
				currentThread.setIsRunning(true);
				currentThread.start();
				
				threads.add(currentThread);
			}
			
			// Wait for manual interrupt:
			System.in.read();
			
			for (int currentThreadNb = 0; currentThreadNb < NB_THREADS; currentThreadNb++)
			{
				currentThread = threads.get(currentThreadNb);
				
				if (currentThread.getIsRunning())
				{
					// Ask the thread to stop:
					currentThread.setIsRunning(false);
					boolean retry = true;
					while (retry) {
						try {
							currentThread.join();
							retry = false;
						} catch (InterruptedException e) {
							// We will try it again and again...
						}
					}
				}
			}
			
			log("Saving...");
			
			// Save state of crawler:
			MarmitonCrawler.saveState();

			// Write resulting output:
			Document outputDoc = createOutput();
			GrabbedRecipes.getInstance().saveState(outputDoc);
			
			String filePath = OUTPUT_FILE_PATH;
			finalizeOutput(filePath, outputDoc);
			
			log("Program ended.");
		}
		catch (Exception ex)
		{
			log("Grabber.grab: unhandled exception: ", ex);
		}
	}

	private Document createInput(String inputFilePath) throws SAXException, IOException, ParserConfigurationException
	{
	  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder docBuilder = docFactory. newDocumentBuilder();

	  //root elements
	  Document doc = docBuilder.parse(inputFilePath);
		return doc;
	}
	
	private Document createOutput() throws ParserConfigurationException
	{
	  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	  //root elements
	  Document doc = docBuilder.newDocument();
		return doc;
	}
	
	private void finalizeOutput(String filePath, Document outputDoc) throws TransformerException
	{
	  //write the content into xml file
	  TransformerFactory transformerFactory = TransformerFactory.newInstance();
	  Transformer transformer = transformerFactory.newTransformer();
	  transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	  transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	  DOMSource source = new DOMSource(outputDoc);

	  StreamResult result =  new StreamResult(new File(filePath));
	  transformer.transform(source, result);
	}
	
	public static void log(String txt)
	{
		System.out.println(txt);
	}

	public static void log(String txt, Throwable t)
	{
		System.out.println(txt + getStackTrace(t));
	}

  public static String getStackTrace(Throwable aThrowable) {
  	if (aThrowable == null) return null;
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
  }
	
}
