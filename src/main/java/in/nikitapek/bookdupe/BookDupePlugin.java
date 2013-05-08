package in.nikitapek.bookdupe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import in.nikitapek.bookdupe.events.BookDupeListener;
import in.nikitapek.bookdupe.util.BookDupeConfigurationContext;

import com.amshulman.mbapi.MbapiPlugin;

public class BookDupePlugin extends MbapiPlugin {
    @Override
    public void onEnable() {
        BookDupeConfigurationContext configurationContext = new BookDupeConfigurationContext(this);

        registerEventHandler(new BookDupeListener(configurationContext));

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
