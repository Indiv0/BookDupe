/**
 *
 * @author Indivisible0
 */
package com.github.Indiv0.BookDupe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Indiv0.util.ConfigUtil;
import com.github.Indiv0.util.ListenerUtil;
import com.github.Indiv0.util.LogUtil;
import com.github.Indiv0.util.MetricsUtil;

public class BookDupe extends JavaPlugin {
    @Override
    public void onLoad() {
        // Initialize all utilities.
        LogUtil.initialize(this);
        ConfigUtil.initialize(this);
        MetricsUtil.initialize(this);
        ListenerUtil.initialize(this);
    }

    @Override
    public void onEnable() {
        // Registers the ItemCraftListener with the PluginManager.
        ListenerUtil.registerListener(new ItemCraftListener(this));

        // Create two recipes to serve as the basis of the plugin.
        ItemStack result = new ItemStack(Material.BOOK_AND_QUILL);

        addShapelessRecipe(result, new Material[] {
                Material.WRITTEN_BOOK,
                Material.BOOK_AND_QUILL
        });
        addShapelessRecipe(result, new Material[] {
                Material.WRITTEN_BOOK,
                Material.INK_SACK,
                Material.FEATHER,
                Material.BOOK
        });
    }

    private void addShapelessRecipe(ItemStack result, Material[] materials) {
        // Initializes the recipe to be used to produce the result.
        ShapelessRecipe recipe = new ShapelessRecipe(result);

        // Adds all of the required materials to the recipe.
        for (Material material : materials)
            recipe.addIngredient(material);

        // Adds the recipe to the server's recipe list.
        getServer().addRecipe(recipe);
    }
}