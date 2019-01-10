package it.sauronsoftware.grab4j.example2;

import java.io.File;
import java.net.URL;

import it.sauronsoftware.grab4j.WebGrabber;

public class Example2 {

	public static void main(String[] args) throws Throwable {
		URL pageUrl = new URL("http://www.sauronsoftware.it/carlopelliccia/");
		File jsLogicFile = new File("example2.js");
		Object[] items = (Object[]) WebGrabber.grab(pageUrl, jsLogicFile);
		for (int i = 0; i < items.length; i++) {
			if (i > 0) {
				System.out.println();
			}
			Item item = (Item) items[i];
			System.out.println("TITLE:    " + item.getTitle());
			System.out.println("CONTENTS: " + item.getContents());
		}
	}

}
