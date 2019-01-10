package com.ant.recipes;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import it.sauronsoftware.grab4j.html.HTMLDocument;
import it.sauronsoftware.grab4j.html.HTMLDocumentFactory;
import it.sauronsoftware.grab4j.html.HTMLElement;
import it.sauronsoftware.grab4j.html.HTMLParseException;
import it.sauronsoftware.grab4j.html.HTMLTag;
import it.sauronsoftware.grab4j.html.HTMLText;

public class MarmitonParser {

	public static void parse(HTMLDocument doc, String url) throws Throwable {
		if (doc == null)
		{
			log("ERROR: document is null.");
			return;
		}

		MarmitonParser parser = new MarmitonParser();
		GrabbedRecipe recipe = new GrabbedRecipe();
		recipe.setUrl(url);
		parser.getTitle(doc, recipe);
		//parser.getAuthor(doc, recipe);
		//parser.getNbStars(doc, recipe);
		//parser.getNbVotes(doc, recipe);
		parser.getIngredients(doc, recipe);
		//parser.getPreparation(doc, recipe);
		//parser.getPreparationTime(doc, recipe);
		//parser.getCookingTime(doc, recipe);
		//parser.getType(doc, recipe);
		parser.getPictureUrl(doc, recipe);
		parser.getWorkingOrder(doc, recipe);
		
		GrabbedRecipes.getInstance().addRecipe(recipe);
		
	}

	private HTMLDocument readDocument(String url) throws IOException, HTMLParseException
	{
		HTMLDocument doc = HTMLDocumentFactory.buildDocument(url);
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
	
	private String getTitle(HTMLDocument doc, GrabbedRecipe recipe)
	{
		/*
		 * Title:
		  <div style="cursor: auto;" class="m_bloc m_content_recette hrecipe">
				<div style="cursor: auto;" class="m_bloc_top">&nbsp;</div>
			  <div style="cursor: auto;" class="m_bloc_cadre">
			    	<h1 style="cursor: auto;" class="m_title">
			            <span style="cursor: auto;" class="item">
			                <span style="cursor: auto;" class="fn">Farfalle au gorgonzola</span>
			            </span>
			        </h1>
		 */
		
		HTMLElement titleElt = doc.searchElement(".../h1(class=main-title )");
		if (titleElt != null)
		{
			String title = ((HTMLTag)titleElt).getInnerText();
			log ("Title: " + title);
			recipe.setTitle(title);
			
			return title;
		}
		return null;
	}

	private void getAuthor(HTMLDocument doc, GrabbedRecipe recipe) {
		recipe.setAuthor("Marmiton");
	}

	private void getNbStars(HTMLDocument doc, GrabbedRecipe recipe) {
		/*
		<span class="review hreview-aggregate">
			<span class="rating">
				<span class="average">
					<span class="value-title" title="0">
					</span>
				</span>
				<span class="best">
				</span>
				<span class="worst">
				</span>
				<span class="count">
					<span class="value-title" title="0">
				</span>
			</span>
		*/
		
		HTMLElement averageElt = doc.searchElement(".../span(class=review hreview-aggregate)/span/span(class=average)/span");
		if (averageElt != null)
		{
			String average = ((HTMLTag)averageElt).getAttribute("title");
			log ("Nb stars: " + average);
			recipe.setNbStars(average);
		}
		else
		{
			log("ERROR: could not retrieve nb stars.");
		}
	}

	private void getNbVotes(HTMLDocument doc, GrabbedRecipe recipe) {
		/*
		<span class="review hreview-aggregate">
			<span class="rating">
				<span class="average">
					<span class="value-title" title="0">
					</span>
				</span>
				<span class="best">
				</span>
				<span class="worst">
				</span>
				<span class="count">
					<span class="value-title" title="0">
				</span>
			</span>
		*/
		
		HTMLElement countElt = doc.searchElement(".../span(class=review hreview-aggregate)/span/span(class=count)/span");
		if (countElt != null)
		{
			String count = ((HTMLTag)countElt).getAttribute("title");
			log ("Nb votes: " + count);
			recipe.setNbVotes(count);
		}
		else
		{
			log("ERROR: could not retrieve nb votes.");
		}
		
	}

	private void getIngredients(HTMLDocument doc, GrabbedRecipe recipe) {
		/*
		  <p class="m_content_recette_ingredients">
			<span>
      	Ingr�dients (pour 4 personnes) :
    	</span>
			    - 400 g de farfalle 
			<br></br>
			- 2 cuill�res � soupe de 
			<a class="mrm_al" href="http://www.marmiton.org/Magazine/Diaporamiam_pur-beurre-c-est-meilleur_1.aspx">
			beurre
			</a><br></br>
			- 200 g de gorgonzola
			<br></br>
			- 50 g de pignons
			<br></br>
			- 100 g de parmesan
			<br></br>
			- 
			<a class="mrm_al" href="http://www.marmiton.org/Magazine/Plein-D-Epices_sel_1.aspx">
      	sel
    	</a>
			, 
			<a class="mrm_al" href="http://www.marmiton.org/Magazine/Plein-D-Epices_poivres_1.aspx">
      	poivre
    	</a>
		*/
		
		HTMLElement ingredientArrayElt = doc.searchElement(".../ul(class=recipe-ingredients__list)");
		if (ingredientArrayElt != null)
		{
			HTMLElement[] ingredientElts = ingredientArrayElt.searchElements("/li/div/span(class=ingredient)");
			String ingredientItem = "";
			//boolean first = true;
			for (HTMLElement elt: ingredientElts)
			{
                                ingredientItem = ((HTMLTag)elt).getInnerText();
                                recipe.addIngredient(ingredientItem);
                                /*
				// Item delimiter is <br>:
				if (elt instanceof HTMLTag && "BR".equals(((HTMLTag)elt).getTagName().toUpperCase()))
				{
					// We are at the end of an ingredient:
					recipe.addIngredient(ingredientItem);
					ingredientItem = "";
				}
				else
				{
					if (elt instanceof HTMLText)
					{
						ingredientItem += ((HTMLText)elt).getInnerText();
					}
					else
					{
						ingredientItem += ((HTMLTag)elt).getInnerText();
					}

					if (first)
					{
						recipe.setNbPersons(ingredientItem);
						first = false;
						ingredientItem = "";
					}
				}
                                */
			}
		}
	}
	
	public void getPreparation(HTMLDocument doc, GrabbedRecipe recipe)
	{
		HTMLElement prepElt = doc.searchElement(".../div(class=m_content_recette_todo)");
		if (prepElt != null)
		{
			String prep = ((HTMLTag)prepElt).getInnerText();
			log ("Preparation: " + prep);
			recipe.setPreparation(prep);
		}
		else
		{
			log("ERROR: could not retrieve preparation.");
		}		
	}
	
	public void getPreparationTime(HTMLDocument doc, GrabbedRecipe recipe)
	{
		HTMLElement timeElt = doc.searchElement(".../span(class=preptime)");
		if (timeElt != null)
		{
			String time = ((HTMLTag)timeElt).getInnerText();
			log ("Preparation Time: " + time);
			recipe.setPreparationTime(time);
		}
		else
		{
			log("ERROR: could not retrieve preparation time.");
		}		
	}
	
	public void getCookingTime(HTMLDocument doc, GrabbedRecipe recipe)
	{
		HTMLElement timeElt = doc.searchElement(".../span(class=cooktime)");
		if (timeElt != null)
		{
			String time = ((HTMLTag)timeElt).getInnerText();
			log ("Cooking Time: " + time);
			recipe.setCookingTime(time);
		}
		else
		{
			log("ERROR: could not retrieve cooking time.");
		}		
	}

	private void getWorkingOrder(HTMLDocument doc, GrabbedRecipe recipe) {
		/*
			<ul class="mrtn-tags-list">
				<li class="mrtn-tag">Dessert</li>
				<li class="mrtn-tag"><a class="mrtn-tag--grey" href="https://www.marmiton.org/recettes/selection_vegetarien.aspx">V�g�tarien</a></li>
			</ul>
		*/
		
		HTMLElement woElt = doc.searchElement(".../ul(class=mrtn-tags-list)/li");
		if (woElt != null)
		{
			String wo = ((HTMLTag)woElt).getInnerText();
			log ("WorkingOrder: " + wo);
			recipe.setWorkingOrder(wo);
		}
		else
		{
			log("ERROR: could not retrieve nb votes.");
		}
		
	}
	
	public void getType(HTMLDocument doc, GrabbedRecipe recipe)
	{
		HTMLElement timeElt = doc.searchElement(".../div(class=m_bloc_cadre)/div(class=m_content_recette_breadcrumb)");
		if (timeElt != null)
		{
			String time = ((HTMLTag)timeElt).getInnerText();
			log ("Type: " + time);
			recipe.setType(time);
		}
		else
		{
			log("ERROR: could not retrieve type.");
		}		
	}	
	
	public void getPictureUrl(HTMLDocument doc, GrabbedRecipe recipe)
	{
		HTMLElement pictureElt = doc.searchElement(".../img(id=af-diapo-desktop-0_img)");
		if (pictureElt != null)
		{
			String pictureUrl = ((HTMLTag)pictureElt).getAttribute("src");
			log ("pictureUrl: " + pictureUrl);
			recipe.setPictureUrl(pictureUrl);
		}
	}
	
	public static void log(String txt)
	{
		System.out.println(txt);
	}

	public static boolean isDocumentParsable(HTMLDocument doc) {
		if (doc == null) 
		{
			return false;
		}
		
		// Check ingredients:
		HTMLElement ingredientArrayElt = doc.searchElement(".../ul(class=recipe-ingredients__list)");
		if (ingredientArrayElt == null)
		{
			return false;
		}
		
		// Check Preparation:
		HTMLElement prepElt = doc.searchElement(".../ol(class=recipe-preparation__list)");
		if (prepElt == null)
		{
			return false;
		}
		
		// Ok:
		return true;

	}

	public static boolean isCaptcha(HTMLDocument doc) {
		if (doc == null) return false;

		// <div id="m_captcha" class="captcha" align="center">
		HTMLElement divElt = doc.searchElement(".../div(id=m_captcha)(class=captcha)");
		if (divElt != null)
		{
			return true;
		}

		return false;
	}

}
