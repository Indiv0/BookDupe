package in.nikitapek.bookdupe;

import in.nikitapek.bookdupe.events.BookDupeListener;
import in.nikitapek.bookdupe.util.BookDupeConfigurationContext;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.amshulman.mbapi.MbapiPlugin;

import java.util.HashMap;
import java.util.Map;

public final class BookDupePlugin extends MbapiPlugin {
    @Override
    public void onEnable() {
        registerEventHandler(new BookDupeListener(new BookDupeConfigurationContext(this)));

        super.onEnable();
    }
}
