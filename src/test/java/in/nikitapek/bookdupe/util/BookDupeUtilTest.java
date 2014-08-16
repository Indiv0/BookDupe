package in.nikitapek.bookdupe.util;

import in.nikitapek.bookdupe.BukkitInitialization;
import in.nikitapek.bookdupe.util.BookDupeUtil;

import java.util.Arrays;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemFactory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.bukkit.Bukkit;

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
}
