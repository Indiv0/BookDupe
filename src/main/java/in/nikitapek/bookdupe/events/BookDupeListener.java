package in.nikitapek.bookdupe.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import in.nikitapek.bookdupe.util.BookDupeUtil;

public final class BookDupeListener implements Listener {
    private final Map<String, Recipe> recipes = new HashMap<>();

    public BookDupeListener() {
        recipes.put("duplicate", BookDupeUtil.createShapelessRecipe(new ItemStack(Material.BOOK_AND_QUILL), new Material[]{
                Material.WRITTEN_BOOK,
                Material.BOOK_AND_QUILL
        }));
        recipes.put("create", BookDupeUtil.createShapelessRecipe(new ItemStack(Material.BOOK_AND_QUILL), new Material[]{
                Material.WRITTEN_BOOK,
                Material.INK_SACK,
                Material.FEATHER,
                Material.BOOK
        }));
        recipes.put("unsign", BookDupeUtil.createShapelessRecipe(new ItemStack(Material.BOOK_AND_QUILL), new Material[]{
                Material.WRITTEN_BOOK,
                Material.INK_SACK,
                Material.FEATHER
        }));

        // Add the recipes to the server's recipe list.
        for (Recipe recipe : recipes.values()) {
            Bukkit.getServer().addRecipe(recipe);
        }
    }

    @EventHandler
    public void onItemCraft(final CraftItemEvent event) {
        final CraftingInventory craftingInventory = event.getInventory();
        final Recipe recipe = event.getRecipe();
        final Player player = (Player) event.getWhoClicked();
        final PlayerInventory playerInventory = player.getInventory();
        final String recipeName = getRecipeName(recipe);
        final String playerName = player.getName();

        // If the recipe did not match any BookDupe recipe, it is irrelevant.
        if (recipeName == null) {
            return;
        }

        // Get the ItemStack and the BookMeta of the book in the recipe.
        final ItemStack initialBook = craftingInventory.getItem(craftingInventory.first(Material.WRITTEN_BOOK));
        final BookMeta book = (BookMeta) initialBook.getItemMeta();
        final String author = book.getAuthor();

        // If the book has enchantments, check to see whether or not the player is allowed to interact with enchanted books.
        if (!player.hasPermission("bookdupe.enchanted") && !initialBook.getEnchantments().isEmpty()) {
            event.setCancelled(true);
            return;
        }

        switch (recipeName) {
            case "unsign":
                if (author.equals(playerName) ? !player.hasPermission("bookdupe.unsign.self") : !player.hasPermission("bookdupe.unsign.others")) {
                    event.setCancelled(true);
                    return;
                }
                event.setCurrentItem(BookDupeUtil.constructBook(book, Material.BOOK_AND_QUILL, player.hasPermission("bookdupe.enchanted.transfer")));
                break;
            case "duplicate":
                if (author.equals(playerName) ? !player.hasPermission("bookdupe.copy.self") : !player.hasPermission("bookdupe.copy.others")) {
                    event.setCancelled(true);
                    return;
                }

                // Ensure that only two (the ingredient and the result) BOOK_AND_QUILL are in the crafting matrix.
                if (craftingInventory.all(Material.BOOK_AND_QUILL).size() != 2) {
                    return;
                }

                // Adds the original book to the player's inventory.
                playerInventory.addItem(initialBook);

                // Sets the result of the craft to the copied book.
                event.setCurrentItem(BookDupeUtil.constructBook(book, Material.WRITTEN_BOOK, player.hasPermission("bookdupe.enchanted.transfer")));
                break;
            case "create":
                if (author.equals(playerName) ? !player.hasPermission("bookdupe.copy.self") : !player.hasPermission("bookdupe.copy.others")) {
                    event.setCancelled(true);
                    return;
                }

                // If the player regularly clicked (singular craft).
                if (!event.isShiftClick()) {
                    // Adds the original book to the player's inventory.
                    playerInventory.addItem(BookDupeUtil.constructBook(book, Material.WRITTEN_BOOK, player.hasPermission("bookdupe.enchanted.transfer")));
                } else {
                    final Map<Integer, Integer> itemsLeft = new HashMap<>();

                    // Gets the indexes of each ingredient in the recipe.
                    final int inkSackIndex = craftingInventory.first(Material.INK_SACK);
                    final int featherIndex = craftingInventory.first(Material.FEATHER);
                    final int bookIndex = craftingInventory.first(Material.BOOK);

                    // Stores the amount of each ingredient in the crafting matrix.
                    itemsLeft.put(inkSackIndex, craftingInventory.getItem(inkSackIndex).getAmount());
                    itemsLeft.put(featherIndex, craftingInventory.getItem(featherIndex).getAmount());
                    itemsLeft.put(bookIndex, craftingInventory.getItem(bookIndex).getAmount());

                    // Get amount of the ingredient of which there is the least to determine how long to loop over the ingredients.
                    final int lowestAmount = Collections.min(itemsLeft.values());

                    // Store the amount of each ingredient that will remain after the crafting process.
                    for (Map.Entry<Integer, Integer> ingredient : itemsLeft.entrySet()) {
                        final int remainingAmount = ingredient.getValue() - lowestAmount;
                        //ingredient.setValue(remainingAmount);

                        if (remainingAmount == 0) {
                            craftingInventory.clear(ingredient.getKey());
                            continue;
                        }

                        craftingInventory.getItem(ingredient.getKey()).setAmount(remainingAmount);
                    }

                    // Creates a HashMap to store items which do not fit into the player's inventory.
                    final Map<Integer, ItemStack> leftOver = new HashMap<>();

                    // Adds the new books to the player's inventory.
                    for (int i = 0; i < lowestAmount; i++) {
                        leftOver.putAll((playerInventory.addItem(BookDupeUtil.constructBook(book, Material.WRITTEN_BOOK, player.hasPermission("book.enchanted.transfer")))));

                        if (leftOver.isEmpty()) {
                            continue;
                        }

                        final Location loc = player.getLocation();
                        final ItemStack item = BookDupeUtil.constructBook(book, Material.WRITTEN_BOOK, player.hasPermission("book.enchanted.transfer"));
                        player.getWorld().dropItem(loc, item);
                    }
                }

                // Sets the result of the craft to the copied book.
                event.setCurrentItem(initialBook);
                break;
        }
    }

    private String getRecipeName(Recipe recipe) {
        // All BookDupe recipes are shapeless, so a non-shapeless recipe is not a valid BookDupe recipe.
        if (!(recipe instanceof ShapelessRecipe)) {
            return null;
        }

        final ShapelessRecipe recipe1 = (ShapelessRecipe) recipe;

        recipe:
        for (Entry<String, Recipe> entry : recipes.entrySet()) {
            // Retrieves the BookDupe recipe the possible recipe is being compared against.
            ShapelessRecipe recipe2 = (ShapelessRecipe) entry.getValue();

            // If they do not have the same result, they are not the same recipe.
            if (!recipe1.getResult().equals(recipe2.getResult())) {
                continue;
            }

            // If they do not have the same amount of ingredients, they are not the same recipe.
            if (recipe1.getIngredientList().size() != recipe2.getIngredientList().size()) {
                continue;
            }

            List<ItemStack> find = recipe1.getIngredientList();
            List<ItemStack> compare = recipe2.getIngredientList();

            // Ensures that any ingredient in the potential recipe's ingredient list exists in the actual recipe's ingredient list.
            for (ItemStack ingredient : compare) {
                if (!find.remove(ingredient)) {
                    continue recipe;
                }
            }

            // If any ingredients in the potential recipe are not also in the actual recipe, then they are not the same recipe.
            if (!find.isEmpty()) {
                continue;
            }

            return entry.getKey();
        }

        return null;
    }
}
