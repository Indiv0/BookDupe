package in.nikitapek.bookdupe;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import in.nikitapek.bookdupe.events.BookDupeListener;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemFactory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import in.nikitapek.bookdupe.BukkitInitialization;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({CraftItemEvent.class, CraftItemFactory.class})
public class BookDupeListenerTest {
    @BeforeClass
    public static void initializeReflection() throws IllegalAccessException {
        BukkitInitialization.initializeItemMeta();
    }

    @Test
    public void testOnItemCraft() {
        /*
         * Set up the fake CraftItemEvent
         */

        CraftItemEvent mockEvent = PowerMockito.mock(CraftItemEvent.class);

        /*
         * Set up the fake player
         */
        // We are not calling any final methods in this class so we can just use
        // Mockito's normal mock.
        Player mockPlayer = mock(Player.class);

        // Set the player name to "TestPlayer".
        when(mockPlayer.getName()).thenReturn("TestPlayer");

        // When onItemCraft calls event.getWhoClicked(), we want to return the mock player.
        when(mockEvent.getWhoClicked()).thenReturn(mockPlayer);

        /*
         * Set up the fake recipe.
         */
        ItemStack result = new ItemStack(Material.BOOK_AND_QUILL);
        ShapelessRecipe recipe = new ShapelessRecipe(result);
        recipe.addIngredient(Material.WRITTEN_BOOK);
        recipe.addIngredient(Material.INK_SACK);
        recipe.addIngredient(Material.FEATHER);

        // When onItemCraft calls event.getRecipe(), we want to return the mock recipe.
        when(mockEvent.getRecipe()).thenReturn(recipe);

        // Create the listener.
        BookDupeListener listener = new BookDupeListener();

        doReturn("unsign").when(listener).getRecipeName(recipe);

        // Send in the mock event.
        listener.onItemCraft(mockEvent);

        verify(mockEvent).getRecipe();
        // Verify that permissions were checked for the player.
        verify(mockPlayer).hasPermission("bookdupe.enchanted");
    }
}
