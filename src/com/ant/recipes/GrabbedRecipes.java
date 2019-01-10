package com.ant.recipes;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GrabbedRecipes {

	/**
	 * List of recipes.
	 */
	private ArrayList<GrabbedRecipe> recipes = new ArrayList<GrabbedRecipe>();
	
	private static GrabbedRecipes singleton = new GrabbedRecipes();
	
	private GrabbedRecipes()
	{
		
	}
	
	public static GrabbedRecipes getInstance()
	{
		return singleton;
	}
	
	public void addRecipe(GrabbedRecipe r)
	{
		this.recipes.add(r);
	}
	
	public ArrayList<GrabbedRecipe> getRecipes()
	{
		return this.recipes;
	}

	public void saveState(Document outputDoc) {
		// Add main node to output XML:
	  Element rootElement = outputDoc.createElement("Recipes");
	  outputDoc.appendChild(rootElement);

	  ArrayList<GrabbedRecipe> recipes = getRecipes();
	  for(GrabbedRecipe recipe: recipes)
	  {
			recipe.write(outputDoc, rootElement);
	  }
	}

	public void loadState(Document inputDoc) {

		NodeList recipesNodes = inputDoc.getElementsByTagName("Recipes");
		Element recipes = (Element)recipesNodes.item(0);
		
		NodeList recipeNodes = recipes.getElementsByTagName("Recipe");
		GrabbedRecipe grabbedRecipe;
		for (int i = 0; i < recipeNodes.getLength(); i++)
		{
			grabbedRecipe = new GrabbedRecipe();
			grabbedRecipe.read((Element)recipeNodes.item(i));
			this.recipes.add(grabbedRecipe);
		}
	}
}
