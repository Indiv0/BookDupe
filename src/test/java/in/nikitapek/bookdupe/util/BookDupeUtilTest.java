package in.nikitapek.bookdupe.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import in.nikitapek.bookdupe.BukkitInitialization;
import in.nikitapek.bookdupe.util.BookDupeUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemFactory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(CraftItemFactory.class)
public class BookDupeUtilTest {
    @BeforeClass
    public static void initializeReflection() throws IllegalAccessException {
        BukkitInitialization.initializeItemMeta();
    }

    @Test
    public void testCreateShapelessRecipe() {
        ShapelessRecipe recipe = BookDupeUtil.createShapelessRecipe(new ItemStack(Material.BOOK_AND_QUILL), new Material[]{
                Material.WRITTEN_BOOK,
                Material.BOOK_AND_QUILL
        });

        Assert.assertEquals(new ItemStack(Material.BOOK_AND_QUILL), recipe.getResult());
        Assert.assertEquals(Arrays.asList(new ItemStack(Material.WRITTEN_BOOK), new ItemStack(Material.BOOK_AND_QUILL)), recipe.getIngredientList());
    }

    @Test
    public void testConstructBook() {
        String authorName = "TestPlayer";
        String bookTitle = "SomeTitle";
        List<String> lore = Arrays.asList("Lorewut");
        List<String> pages = Arrays.asList("A Page!");
        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
        enchantments.put(Enchantment.FIRE_ASPECT, 5);

        // Mock the metadata for a book.
        BookMeta mockMeta = mock(BookMeta.class);

        // Stub all of the relevant methods for the mock book metadata.
        when(mockMeta.getAuthor()).thenReturn(authorName);
       when(mockMeta.getTitle()).thenReturn(bookTitle);
        when(mockMeta.getLore()).thenReturn(lore);
        when(mockMeta.getPages()).thenReturn(pages);
        when(mockMeta.getEnchants()).thenReturn(enchantments);

        // Construct a new book using the constructBook() method.
        ItemStack newBook = BookDupeUtil.constructBook(mockMeta, Material.BOOK_AND_QUILL, true);
        BookMeta newBookMeta = (BookMeta) newBook.getItemMeta();

        // Assert that the book was constructed correctly.
        Assert.assertEquals(authorName, newBookMeta.getAuthor());
        Assert.assertEquals(bookTitle, newBookMeta.getTitle());
        Assert.assertEquals(lore, newBookMeta.getLore());
        Assert.assertEquals(pages, newBookMeta.getPages());
        // TODO: Fix the EnchantmentWrapper class returning null for the enchantment name.
        //Assert.assertEquals(enchantments, newBookMeta.getEnchants());
    }
}
