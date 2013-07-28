package in.nikitapek.bookdupe.events;

import com.amshulman.mbapi.MbapiPlugin;
import in.nikitapek.bookdupe.BookDupePlugin;
import in.nikitapek.bookdupe.util.BookDupeConfigurationContext;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;

public final class BookDupeListener implements Listener {
    private final Map<String, Recipe> recipes = new HashMap<>();

    private final boolean allowIllegalEnchants;
    private final boolean allowIllegalEnchantTransfer;

    public BookDupeListener(final BookDupeConfigurationContext configurationContext) {
        this.allowIllegalEnchants = configurationContext.allowIllegalEnchants;
        this.allowIllegalEnchantTransfer = configurationContext.allowIllegalEnchantTransfer;

        addShapelessRecipe("duplicate", new ItemStack(Material.BOOK_AND_QUILL), new Material[] {
                Material.WRITTEN_BOOK,
                Material.BOOK_AND_QUILL
        });
        addShapelessRecipe("create", new ItemStack(Material.BOOK_AND_QUILL), new Material[] {
                Material.WRITTEN_BOOK,
                Material.INK_SACK,
                Material.FEATHER,
                Material.BOOK
        });
        addShapelessRecipe("unsign", new ItemStack(Material.BOOK_AND_QUILL), new Material[] {
                Material.WRITTEN_BOOK,
                Material.INK_SACK,
                Material.FEATHER
        });
    }

    @EventHandler
    public void onItemCraft(final CraftItemEvent event) {
        final CraftingInventory craftingInventory = event.getInventory();
        final Recipe recipe = event.getRecipe();

        // All BookDupe recipes are hapeless, so a non-shapeless recipe is irrelevant.
        // The recipe is also irrelevant if it is not stored in the recipes Map.
        if (!(recipe instanceof ShapelessRecipe) || !recipes.containsValue(recipe)) {
            return;
        }

        // If the player does not have permissions to copy books, cancels the
        // event.
        if (!event.getWhoClicked().hasPermission("bookdupe.use")) {
            event.setCancelled(true);
            return;
        }

        // ItemStack represention of the book to be cloned.
        final ItemStack initialBook = craftingInventory.getItem(craftingInventory.first(Material.WRITTEN_BOOK));

        // Gets the BookMeta data of the book.
        final BookMeta book = (BookMeta) initialBook.getItemMeta();

        // If the player does not have permission to copy any book
        // and the book was not written by the player, do not allow
        // the player to copy the book.
        if (!event.getWhoClicked().hasPermission("bookdupe.any")
                && !book.getAuthor().equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            return;
        }

        // If the book has enchantments, check to see whether or not they're
        // allowed.
        if (!initialBook.getEnchantments().isEmpty() && !allowIllegalEnchants) {
            event.setCancelled(true);
            return;
        }

        // Get the player's inventory.
        final PlayerInventory playerInventory = event.getWhoClicked().getInventory();

        // Gets the index of the first INK_SACK in the recipe.
        final int inkSackIndex = craftingInventory.first(Material.INK_SACK);
        // Gets the index of the first FEATHER in the recipe.
        final int featherIndex = craftingInventory.first(Material.FEATHER);
        // Gets the index of the first BOOK in the recipe.
        final int bookIndex = craftingInventory.first(Material.BOOK);

        if (recipe.equals(recipes.get("unsign"))) {
            event.setCurrentItem(getNewBook(initialBook, Material.BOOK_AND_QUILL));
        } else if (recipe.equals(recipes.get("duplicate"))) {
            // Check only one BOOK_AND_QUILL is in the crafting matrix.
            if (craftingInventory.all(Material.BOOK_AND_QUILL).size() != 2) {
                return;
            }

            // Adds the original book to the player's inventory.
            playerInventory.addItem(initialBook);

            // Sets the result of the craft to the copied books.
            event.setCurrentItem(getNewBook(initialBook, Material.WRITTEN_BOOK));
        } else if (recipe.equals(recipe.equals(recipes.get("create")))) {
            // Handle a non BOOK_AND_QUILL based recipe.
            // If the player regularly clicked (singular craft).
            if (!event.isShiftClick()) {
                // Adds the original book to the player's inventory.
                playerInventory.addItem(getNewBook(initialBook, Material.WRITTEN_BOOK));
            } else {
                // Gets the amount of INK_SACK in the crafting matrix.
                final int inkSackAmount = craftingInventory.getItem(inkSackIndex).getAmount();
                // Gets the amount of FEATHER in the crafting matrix.
                final int featherAmount = craftingInventory.getItem(featherIndex).getAmount();
                // Gets the amount of BOOK in the crafting matrix.
                final int bookAmount = craftingInventory.getItem(bookIndex).getAmount();

                int lowestAmount = 0;

                // Get the ingredient of which there is the least and loop until
                // that ingredient no longer exists.
                if (inkSackAmount < featherAmount && inkSackAmount < bookAmount) {
                    lowestAmount = inkSackAmount;
                }
                // Otherwise check if the crafting inventory contains less
                // FEATHER than any other ingredient.
                if (featherAmount < inkSackAmount && featherAmount < bookAmount) {
                    lowestAmount = featherAmount;
                    // Otherwise the crafting inventory contains less BOOK than
                    // any
                    // other ingredient.
                } else {
                    lowestAmount = bookAmount;
                }

                // Loops through crafting matrix reducing item amounts
                // one-by-one.
                int itemsLeft = 0;

                itemsLeft = craftingInventory.getItem(inkSackIndex).getAmount()
                        - lowestAmount;

                if (itemsLeft != 0) {
                    craftingInventory.getItem(inkSackIndex).setAmount(itemsLeft);
                } else {
                    craftingInventory.clear(inkSackIndex);
                }

                itemsLeft = craftingInventory.getItem(featherIndex).getAmount()
                        - lowestAmount;

                if (itemsLeft != 0) {
                    craftingInventory.getItem(featherIndex).setAmount(itemsLeft);
                } else {
                    craftingInventory.clear(featherIndex);
                }

                itemsLeft = craftingInventory.getItem(bookIndex).getAmount()
                        - lowestAmount;

                if (itemsLeft != 0) {
                    craftingInventory.getItem(bookIndex).setAmount(itemsLeft);
                } else {
                    craftingInventory.clear(bookIndex);
                }

                // Creates a HashMap to store items which do not fit into the
                // player's inventory.
                final HashMap<Integer, ItemStack> leftOver = new HashMap<Integer, ItemStack>();

                // Adds the new books to the player's inventory.
                for (int i = 0; i < lowestAmount; i++) {
                    leftOver.putAll((playerInventory.addItem(getNewBook(initialBook, Material.WRITTEN_BOOK))));

                    if (leftOver.isEmpty()) {
                        continue;
                    }

                    final Location loc = event.getWhoClicked().getLocation();
                    final ItemStack item = getNewBook(initialBook, Material.WRITTEN_BOOK);
                    event.getWhoClicked().getWorld().dropItem(loc, item);
                }
            }

            // Sets the result of the craft to the copied books.
            event.setCurrentItem(initialBook);
        }
    }

    private ItemStack getNewBook(final ItemStack previousBook, final Material bookType) {
        if (bookType == null || (bookType != Material.WRITTEN_BOOK && bookType != Material.BOOK_AND_QUILL)) {
            throw new IllegalArgumentException();
        }
        // Creates the new book to be returned.
        final ItemStack newBook = new ItemStack(bookType);

        // Retrieves the BookMeta data.
        final BookMeta newBookMeta = (BookMeta) newBook.getItemMeta();
        final BookMeta previousBookMeta = (BookMeta) previousBook.getItemMeta();

        // Transfers the author, title, and pages to the new tag.
        newBookMeta.setAuthor(previousBookMeta.getAuthor());
        newBookMeta.setTitle(previousBookMeta.getTitle());
        newBookMeta.setLore(previousBookMeta.getLore());
        newBookMeta.setPages(previousBookMeta.getPages());

        // If the transfer of enchantments is allowed, transfers them.
        if (allowIllegalEnchantTransfer && previousBookMeta.hasEnchants()) {
            newBookMeta.getEnchants().putAll(previousBookMeta.getEnchants());
        }

        newBook.setItemMeta(newBookMeta);
        return newBook;
    }

    private void addShapelessRecipe(final String name, final ItemStack result, final Material[] materials) {
        // Initializes the recipe to be used to produce the result.
        final ShapelessRecipe recipe = new ShapelessRecipe(result);

        // Adds all of the required materials to the recipe.
        for (final Material material : materials) {
            recipe.addIngredient(material);
        }

        // Adds the recipe to the server's recipe list.
        Bukkit.getServer().addRecipe(recipe);
        recipes.put(name, recipe);
    }
}
