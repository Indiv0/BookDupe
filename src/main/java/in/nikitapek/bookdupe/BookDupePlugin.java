package in.nikitapek.bookdupe;

import in.nikitapek.bookdupe.events.BookDupeListener;
import in.nikitapek.bookdupe.util.BookDupeConfigurationContext;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.amshulman.mbapi.MbapiPlugin;

public final class BookDupePlugin extends MbapiPlugin {
    @Override
    public void onEnable() {
        registerEventHandler(new BookDupeListener(new BookDupeConfigurationContext(this)));

        final ItemStack result = new ItemStack(Material.BOOK_AND_QUILL);

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

    private void addShapelessRecipe(final ItemStack result, final Material[] materials) {
        // Initializes the recipe to be used to produce the result.
        final ShapelessRecipe recipe = new ShapelessRecipe(result);

        // Adds all of the required materials to the recipe.
        for (final Material material : materials) {
            recipe.addIngredient(material);
        }

        // Adds the recipe to the server's recipe list.
        getServer().addRecipe(recipe);
    }
}
