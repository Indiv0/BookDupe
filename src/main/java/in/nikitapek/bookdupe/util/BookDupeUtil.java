package in.nikitapek.bookdupe.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;

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

    public static ItemStack constructBook(BookMeta previousBookMeta, Material bookType, boolean transferEnchants) {
        // Ensure that the expected resulting material type is, in fact, a book.
        assert!(bookType == Material.WRITTEN_BOOK || bookType == Material.BOOK_AND_QUILL);

        // Creates the new book to be returned.
        ItemStack newBook = new ItemStack(bookType);

        // Retrieves the BookMeta data.
        BookMeta newBookMeta = (BookMeta) newBook.getItemMeta();

        // Transfers the author, title, and pages to the new tag.
        newBookMeta.setAuthor(previousBookMeta.getAuthor());
        newBookMeta.setTitle(previousBookMeta.getTitle());
        newBookMeta.setLore(previousBookMeta.getLore());
        newBookMeta.setPages(previousBookMeta.getPages());

        // If the transfer of enchantments is allowed, transfers them.
        if (transferEnchants && previousBookMeta.hasEnchants()) {
            newBookMeta.getEnchants().putAll(previousBookMeta.getEnchants());
        }

        newBook.setItemMeta(newBookMeta);

        return newBook;
    }
}
