package com.github.Indiv0.BookDupe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BookDupe extends JavaPlugin {
	
	public final ItemCraftListener blockListener = new ItemCraftListener();
	
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(this.blockListener, this);
		
		ItemStack craftable = new ItemStack(Material.FEATHER);
		ShapelessRecipe recipe = new ShapelessRecipe(craftable);
		recipe.addIngredient(Material.WRITTEN_BOOK);
		recipe.addIngredient(Material.BOOK_AND_QUILL);
		
		getServer().addRecipe(recipe);
		
		getLogger().info("BookDupe has initialized successfully.");
	}
	 
	public void onDisable(){ 
	 
	}
}