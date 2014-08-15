package in.nikitapek.bookdupe;

import org.bukkit.event.inventory.CraftItemEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CraftItemEvent.class)
public class BookDupeListenerTest {
    @Test
    public void testOnItemCraft() {
        /*
         * Setup the fake CraftItemEvent
         */

        CraftItemEvent mockEvent = PowerMockito.mock(CraftItemEvent.class);
    }
}
