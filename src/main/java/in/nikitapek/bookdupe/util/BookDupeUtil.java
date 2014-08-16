package in.nikitapek.bookdupe.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class BookDupeUtil {
    private BookDupeUtil() { }

    public static ShapelessRecipe createShapelessRecipe(ItemStack result, Material[] materials) {
        // Initializes the recipe to be used to produce the result.
        ShapelessRecipe recipe = new ShapelessRecipe(result);

        // Adds all of the required materials to the recipe.
        for (Material material : materials) {
            recipe.addIngredient(material);
        }

        return recipe;
    }
}
