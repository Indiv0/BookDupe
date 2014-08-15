package in.nikitapek.bookdupe;

import in.nikitapek.bookdupe.events.BookDupeListener;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemFactory;

import org.bukkit.event.inventory.CraftItemEvent;
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
         * Setup the fake CraftItemEvent
         */

        CraftItemEvent mockEvent = PowerMockito.mock(CraftItemEvent.class);

        // Create the listener.
        BookDupeListener listener = new BookDupeListener();
    }
}
