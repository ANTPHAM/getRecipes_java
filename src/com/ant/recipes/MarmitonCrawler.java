package com.ant.recipes;

import it.sauronsoftware.grab4j.html.HTMLDocument;
import it.sauronsoftware.grab4j.html.HTMLDocumentFactory;
import it.sauronsoftware.grab4j.html.HTMLElement;
import it.sauronsoftware.grab4j.html.HTMLLink;
import it.sauronsoftware.grab4j.html.HTMLParseException;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MarmitonCrawler extends Thread {
	// private static String startUrl = "http://www.marmiton.org";
	private static String startUrl = "https://www.marmiton.org/recettes/recherche.aspx?type=all&aqt=desserts";
	
	private Boolean isRunning = false;
	private static Queue<String> urlsToProcess = new LinkedList<String>();
	private static Queue<String> urlsProcessed = new LinkedList<String>();
	private int threadId = -1;

	public static void init()
	{
		Grabber.log("MarmitonCrawler: initializing");

		if ( ! loadState())
		{
			Grabber.log("MarmitonCrawler: initializing: adding first URL");
			
			// Start scanning from beginning:
			urlsToProcess.add(startUrl);
		}
	}

	public MarmitonCrawler(int threadId)
	{
		this.threadId = threadId;
	}
	
  @Override
  public void run() {
	Grabber.log("Starting thread");
	  
  	String urlToProcess = null;
  	int nbPages = 0;
    while (this.isRunning) {
      try 
      {
      	synchronized(urlsToProcess)
      	{
      		if (urlsToProcess.size() == 0)
      		{
      			// We are done:
      			break;
      		}
      		
      		// Take next URL:
			urlToProcess = urlsToProcess.poll();
			Grabber.log("MarmitonCrawler.run: Thread " + this.threadId + ": nb URLS to process=" + urlsToProcess.size() + ". Nb processed=" + urlsProcessed.size() + ". Nb recipes=" + GrabbedRecipes.getInstance().getRecipes().size() + ". Url=" + urlToProcess);
      	}
      	if (urlToProcess != null)
      	{
      		try
      		{
	      		boolean ok = this.processUrl(urlToProcess);
	      		if (ok)
	      		{
	          	synchronized(urlsToProcess)
	          	{
	          		urlsProcessed.add(urlToProcess);
	          	}
	      		}
      		}
      		catch (IsCaptchaException ex2)
      		{
      			// We need to stop as the website sends a captcha page:
				synchronized(urlsToProcess)
				{
					urlsToProcess.add(urlToProcess);
				}
      			Grabber.log("MarmitonCrawler.run: Thread " + this.threadId + ": captcha page detected. Exiting");
      			
      			this.isRunning = false;
      			
      			Toolkit.getDefaultToolkit().beep();
      		}
      	}
      	/*
        try
        {
        	sleep(10);
        } 
        catch (InterruptedException ie)
        { }
        */
        nbPages++;
        if (nbPages > 30000)
        {
        	this.isRunning = false;
        }
      } catch (Exception e) {
      	Grabber.log("MarmitonCrawler.run: Thread " + this.threadId + ": uncaught exception: " + e.getMessage());
      	this.isRunning = false; // Exit
      }
    }
    this.isRunning = false;
		Grabber.log("MarmitonCrawler.run: Thread " + this.threadId + ": execution completed. You may need to press enter to save.");
  }

	private boolean processUrl(String urlToProcess) throws IsCaptchaException {
		try
		{
			Grabber.log("processUrl: " + urlToProcess);
			if (urlToProcess.contains("#") && ( ! urlToProcess.endsWith("#")))
			{
				return false;
			}
			
			HTMLDocument doc = readDocument(urlToProcess);
			if (MarmitonParser.isCaptcha(doc))
			{
				throw new IsCaptchaException();
			}
			ArrayList<String> urlsOfDocument = this.getUrlsOfDocument(doc);
			
			if (urlsOfDocument !=null)
			{
				synchronized(urlsToProcess)
				{
			
					for(String urlOfDoc: urlsOfDocument)
					{
						if (urlsProcessed.contains(urlOfDoc) || urlsToProcess.contains(urlOfDoc))
						{
							// Already processed or already planned to be processed:
							continue;
						}

						//Grabber.log("found next Url: " + urlOfDoc);
						// Plan for processing:
						urlsToProcess.add(urlOfDoc);
					}
      	}		
			}

			if (MarmitonParser.isDocumentParsable(doc))
			{
				try
				{
					// Parse current doc:
					MarmitonParser.parse(doc, urlToProcess);
				}
				catch (Throwable t)
				{
					Grabber.log("MarmitonCrawler.processUrl: Thread " + this.threadId + ": ERROR: unable to process '" + urlToProcess + "': exception " + t.getMessage());
				}
			}
			
			return true;
		}
		catch (IsCaptchaException ex1)
		{
			throw ex1;
		}
		catch (Exception ex)
		{
			Grabber.log("MarmitonCrawler.processUrl: Thread " + this.threadId + ": exception ", ex);
			return false;
		}
	}

	private ArrayList<String> getUrlsOfDocument(HTMLDocument doc) {
		ArrayList<String> result = new ArrayList<String>();
		
		Grabber.log("Entering getUrlsOfDocument...");
		
		HTMLElement[] linkElts = doc.searchElements("html/body/.../a");
		if (linkElts != null)
		{
			Grabber.log("getUrlsOfDocument: before for");
			Grabber.log("nb elements: " + doc.getElementCount());
			Grabber.log("linkElts.Count: " + linkElts.length);
			displayElements(doc);
			String url;
			for (HTMLElement elt: linkElts)
			{
				if (elt instanceof HTMLLink)
				{
					url = ((HTMLLink)elt).getLinkURL();
					if (url == null) continue;
					//Grabber.log("getUrlsOfDocument: found url " + url);
					url = url.toLowerCase();
					if (url.startsWith("javascript:")) continue;
					//if (url.startsWith("https://")) continue;
					if (url.startsWith("http://") && ( ! url.contains("://www.marmiton.org"))) continue;
					if (url.contains("#") && ( ! url.endsWith("#"))) continue;
					
					result.add(url);
				}
			}
		}

		return result;
	}

	private void displayElements(HTMLDocument document)
	{
		for (int i = 0; i < document.getElementCount(); i++) {
			HTMLElement el = document.getElement(i);
			//Grabber.log("found element: " + el.getInnerText());
			//Grabber.log("found element: " + el.getTagName());
			Grabber.log("found element: " + el.toString());
		}
	}
	private HTMLDocument readDocument(String url) throws IOException, HTMLParseException
	{
		HTMLDocument doc = HTMLDocumentFactory.buildDocument(url);
		return doc;
	}

	public void setIsRunning(boolean b) {
		this.isRunning = b;
	}

	public boolean getIsRunning() {
		return this.isRunning;
	}
	
	public static void saveState()
	{
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try
		{
			// Local path to saved files:
			fileOut =  new FileOutputStream(Grabber.ROOT_OUTPUT_PATH + "assets\\currentState.xml");
			out =  new ObjectOutputStream(fileOut);
			out.writeObject(urlsToProcess);
			out.writeObject(urlsProcessed);
		}
		catch (Exception ex)
		{
			Grabber.log("MarmitonCrawler.saveState: ERROR: " + ex.getMessage());
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (Exception ex2)
				{
					Grabber.log("MarmitonCrawler.saveState: ERROR: closing out: " + ex2.getMessage());
				}
			}
			if (fileOut != null)
			{
				try
				{
					fileOut.close();
				}
				catch (Exception ex3)
				{
					Grabber.log("MarmitonCrawler.saveState: ERROR: closing fileOut: " + ex3.getMessage());
				}
			}
		}
	}
	
	public static boolean loadState()
	{
		java.io.FileInputStream fileIn = null;
		java.io.ObjectInputStream in = null;
		try
		{
			if ((new File(Grabber.ROOT_OUTPUT_PATH + "assets\\currentState.xml")).exists())
			{
				Grabber.log("MarmitonCrawler.loadState: current state file found");
				fileIn = new java.io.FileInputStream(Grabber.ROOT_OUTPUT_PATH + "assets\\currentState.xml");
				in = new java.io.ObjectInputStream(fileIn);
				urlsToProcess = (Queue<String>)in.readObject();
				urlsProcessed = (Queue<String>)in.readObject();
				Grabber.log("MarmitonCrawler.loadState: returning true");
				return true;
			}
			Grabber.log("MarmitonCrawler.loadState: current state file NOT found. returning false");
			return false;
		}
		catch (Exception ex)
		{
			Grabber.log("MarmitonCrawler.loadState: ERROR: " + ex.getMessage());
			return false;
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception ex2)
				{
					Grabber.log("MarmitonCrawler.loadState: ERROR: closing out: " + ex2.getMessage());
				}
			}
			if (fileIn != null)
			{
				try
				{
					fileIn.close();
				}
				catch (Exception ex3)
				{
					Grabber.log("MarmitonCrawler.loadState: ERROR: closing fileOut: " + ex3.getMessage());
				}
			}
		}
	}

}
