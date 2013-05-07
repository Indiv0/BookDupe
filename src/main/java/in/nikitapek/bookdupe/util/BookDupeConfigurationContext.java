package in.nikitapek.bookdupe.util;

import com.amshulman.mbapi.MbapiPlugin;
import com.amshulman.mbapi.util.ConfigurationContext;

public class BookDupeConfigurationContext extends ConfigurationContext {
    // Stores whether or not pumpkins are currently spreading.
    public final boolean allowIllegalEnchants;
    public final boolean allowIllegalEnchantTransfer;

    public BookDupeConfigurationContext(MbapiPlugin plugin) {
        super(plugin);

        plugin.saveDefaultConfig();

        // Retrieves whether or not illegal enchantments are allowed on books at all.
        allowIllegalEnchants = plugin.getConfig().getBoolean("allowIllegalEnchants", false);

        // Retrieves whether or not the transfer of illegal enchants from a source book to a copied book is allowed.
        allowIllegalEnchantTransfer = plugin.getConfig().getBoolean("allowIllegalEnchantTransfer", false);
    }
}
