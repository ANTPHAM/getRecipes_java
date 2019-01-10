package it.sauronsoftware.grab4j.example1;

import java.io.File;
import java.net.URL;

import it.sauronsoftware.grab4j.WebGrabber;

public class Example1 {

	public static void main(String[] args) throws Throwable {
		URL pageUrl = new File("example1.html").toURL();
		File jsLogicFile = new File("example1.js");
		Object res = WebGrabber.grab(pageUrl, jsLogicFile);
		System.out.println(res);
	}

}
