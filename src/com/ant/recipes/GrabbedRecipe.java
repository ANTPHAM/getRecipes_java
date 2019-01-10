package com.aubert.recipes;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GrabbedRecipe {

	private String title = null;
	private String author = null;
	private String url = null;
	private String nbStars = null;
	private String nbVotes = null;
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	private String nbPersons = null;
	private String prepTime = null;
	private String cookTime = null;
	private String recipeType = null;
	private String preparation = null;
	private String pictureUrl = null;
	private String workingOrder = null;
	
	public GrabbedRecipe()
	{	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getNbStars() {
		return nbStars;
	}

	public void setNbStars(String nbStars) {
		this.nbStars = nbStars;
	}

	public String getNbVotes() {
		return nbVotes;
	}

	public void setNbVotes(String nbVotes) {
		this.nbVotes = nbVotes;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getWorkingOrder() {
		return workingOrder;
	}

	public void setWorkingOrder(String workingOrder) {
		this.workingOrder = workingOrder;
	}

	public void write(Document doc, Element mainElement)
	{
	  Element rootElement = doc.createElement("Recipe");
	  mainElement.appendChild(rootElement);

	  // Author element
	  Element authorElt = doc.createElement("Author");
	  if (this.author != null)
	  {
	  	authorElt.appendChild(doc.createTextNode(this.author));
	  }
	  rootElement.appendChild(authorElt);

	  // URL element
	  Element urlElt = doc.createElement("Url");
	  if (this.url != null)
	  {
	  	urlElt.appendChild(doc.createTextNode(this.url));
	  }
	  rootElement.appendChild(urlElt);
	  
	  // Title element
	  Element titleElt = doc.createElement("Title");
	  if (this.title != null)
	  {
	  	titleElt.appendChild(doc.createTextNode(this.title));
	  }
	  rootElement.appendChild(titleElt);
          
	  /*
	  // Type element
	  Element typeElt = doc.createElement("Type");
	  if (this.recipeType != null)
	  {
	  	typeElt.appendChild(doc.createTextNode(this.recipeType));
	  }
	  rootElement.appendChild(typeElt);

	  // Nb stars element
	  Element nbStarsElt = doc.createElement("NbStars");
	  if (this.nbStars != null)
	  {
	  	nbStarsElt.appendChild(doc.createTextNode(this.nbStars));
	  }
	  rootElement.appendChild(nbStarsElt);

	  // Nb votes element
	  Element nbVotesElt = doc.createElement("NbVotes");
	  if (this.nbVotes != null)
	  {
	  	nbVotesElt.appendChild(doc.createTextNode(this.nbVotes));
	  }
	  rootElement.appendChild(nbVotesElt);

	  // Nb Persons element
	  Element nbPersonsElt = doc.createElement("NbPersons");
	  if (this.nbPersons != null)
	  {
	  	nbPersonsElt.appendChild(doc.createTextNode(this.nbPersons));
	  }
	  rootElement.appendChild(nbPersonsElt);

	  // Preparation Time element
	  Element prepTimeElt = doc.createElement("PrepTime");
	  if (this.prepTime != null)
	  {
	  	prepTimeElt.appendChild(doc.createTextNode(this.prepTime));
	  }
	  rootElement.appendChild(prepTimeElt);

	  // Cooking Time element
	  Element cookTimeElt = doc.createElement("CookTime");
	  if (this.cookTime != null)
	  {
	  	cookTimeElt.appendChild(doc.createTextNode(this.cookTime));
	  }
	  rootElement.appendChild(cookTimeElt);
	  */
	  // Picture URL element
	  Element pictureUrlElt = doc.createElement("PictureUrl");
	  if (this.pictureUrl != null)
	  {
	  	pictureUrlElt.appendChild(doc.createTextNode(this.pictureUrl));
	  }
	  rootElement.appendChild(pictureUrlElt);	
          
	  // Working Order element
	  Element woElt = doc.createElement("WorkingOrder");
	  if (this.workingOrder != null)
	  {
	  	woElt.appendChild(doc.createTextNode(this.workingOrder));
	  }
	  rootElement.appendChild(woElt);

	  // Ingredients
	  Element ingredientsElt = doc.createElement("Ingredients");
	  if (this.ingredients != null)
	  {
		  Element ingredientElt, amountElt, kindElt;
		  String amount;
		  String kind;
	  	for (Ingredient ing: this.ingredients)
	  	{
	  		if (ing == null) continue;
	  		amount = ing.amount;
	  		kind = ing.kind;
  			ingredientElt = doc.createElement("Ingredient");
	  		if (amount != null && amount.length() > 0)
	  		{
	  			amountElt = doc.createElement("Amount");
	  			amountElt.appendChild(doc.createTextNode(amount));
		  		ingredientElt.appendChild(amountElt);
	  		}
	  		if (kind != null && kind.length() > 0)
	  		{
	  			kindElt = doc.createElement("Kind");
	  			kindElt.appendChild(doc.createTextNode(kind));
		  		ingredientElt.appendChild(kindElt);
	  		}
	  		ingredientsElt.appendChild(ingredientElt);
	  	}
	  }
	  rootElement.appendChild(ingredientsElt);

          /*
	  // Preparation element
	  Element prepElt = doc.createElement("Preparation");
	  if (this.preparation != null)
	  {
	  	prepElt.appendChild(doc.createTextNode(this.preparation));
	  }
	  rootElement.appendChild(prepElt);	  
          */
	}

	public void addIngredient(String ingredientItem) {
		Grabber.log ("ingredientItem: " + ingredientItem);
		String item = ingredientItem.replace(',', ' ').replace("-", " ").replace("\r\n", " ").replace("\n", " ");
		item = item.trim();
		if (item != null && item.length() > 0)
		{
			Ingredient ing = new Ingredient();
			int dePos = item.indexOf(" de ");
			if (dePos > -1)
			{
				// We have an amount:
				ing.amount = item.substring(0, dePos);
				ing.kind = item.substring(dePos + 4, item.length());
			}
			else
			{
				// Check if we have a number first (like "3 oeufs"):
				int spacePos = item.indexOf(" ");
				boolean ok = false;
				if (spacePos > 0)
				{
					String possibleAmount = item.substring(0, spacePos);
					int possibleAmountAsInt = -1;
					try
					{
						possibleAmountAsInt = Integer.parseInt(possibleAmount);

						ing.amount = possibleAmount;
						ing.kind = item.substring(spacePos + 1);
						ok = true;
					}
					catch (Exception ex)
					{
						ok = false;
					}
				}
				
				if (!ok)
				{
					// No amount:
					ing.kind = item;
				}
			}
			this.ingredients.add(ing);
		}
	}

	public void setNbPersons(String ingredientItem) {
		Grabber.log ("Nb persons: " + ingredientItem);
		ingredientItem = ingredientItem.replace(',', ' ').replace('-', ' ').replace("\r\n", " ").replace("\n", " ").replace(':', ' ');
		this.nbPersons = ingredientItem.trim();
	}

	class Ingredient
	{
		public String amount = null;
		public String kind = null;
	}

	public void setPreparation(String prep) {
		this.preparation = prep;
	}
	
	public String getPreparation()
	{
		return this.preparation;
	}

	public void setPreparationTime(String time) {
		this.prepTime = time;
	}

	public void setCookingTime(String time) {
		this.cookTime = time;
	}

	public void setType(String type) {
		this.recipeType = type;
	}

	public void read(Element xmlNode) {
		if (xmlNode == null) return;
		NodeList childNodes = xmlNode.getChildNodes();
		Element currentElement;
		Node node;
		for (int i = 0; i < childNodes.getLength(); i++)
		{
			node = childNodes.item(i);
			if ( ! (node instanceof Element)) continue;
			currentElement = (Element)node;
			
			if ("Author".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.author = currentElement.getFirstChild().getNodeValue();
			}
			else if ("Url".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.url = currentElement.getFirstChild().getNodeValue();
			}
			else if ("Title".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.title = currentElement.getFirstChild().getNodeValue();
			}
			else if ("Type".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.recipeType = currentElement.getFirstChild().getNodeValue();
			}
			else if ("NbStars".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.nbStars = currentElement.getFirstChild().getNodeValue();
			}
			else if ("NbVotes".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.nbVotes = currentElement.getFirstChild().getNodeValue();
			}
			else if ("NbPersons".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.nbPersons = currentElement.getFirstChild().getNodeValue();
			}
			else if ("PrepTime".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.prepTime = currentElement.getFirstChild().getNodeValue();
			}
			else if ("CookTime".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.cookTime = currentElement.getFirstChild().getNodeValue();
			}
			else if ("PictureUrl".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.pictureUrl = currentElement.getFirstChild().getNodeValue();
			}
			else if ("WorkingOrder".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.workingOrder = currentElement.getFirstChild().getNodeValue();
			}
			else if ("Preparation".equals(currentElement.getNodeName()) && currentElement.getChildNodes().getLength() > 0)
			{
				this.preparation = currentElement.getFirstChild().getNodeValue();
			}
			else if ("Ingredients".equals(currentElement.getNodeName()))
			{
				/*
		    <Ingredients>
		      <Ingredient>
		        <Amount>2 feuilles</Amount>
		        <Kind>brick</Kind>
		      </Ingredient>
	      */				
				NodeList ingredientNodes = currentElement.getElementsByTagName("Ingredient");
				Ingredient ingredient;
				Element ingredientNode;
				for (int j = 0; j < ingredientNodes.getLength(); j++)
				{
					ingredient = new Ingredient();
					ingredientNode = (Element)ingredientNodes.item(j);
					NodeList amountNodes = ingredientNode.getElementsByTagName("Amount");
					if (amountNodes != null && amountNodes.getLength() > 0)
					{
						if (amountNodes.item(0).getChildNodes().getLength() > 0)
						{
							ingredient.amount = amountNodes.item(0).getFirstChild().getNodeValue();
						}
					}

					NodeList kindNodes = ingredientNode.getElementsByTagName("Kind");
					if (kindNodes != null && kindNodes.getLength() > 0)
					{
						if (kindNodes.item(0).getChildNodes().getLength() > 0)
						{
							ingredient.kind = kindNodes.item(0).getFirstChild().getNodeValue();
						}
					}

					this.ingredients.add(ingredient);
				}
			}
		}
		
	}
}
