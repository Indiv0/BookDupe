package in.nikitapek.bookdupe;

import com.amshulman.mbapi.MbapiPlugin;
import in.nikitapek.bookdupe.events.BookDupeListener;

public final class BookDupePlugin extends MbapiPlugin {
    @Override
    public void onEnable() {
        registerEventHandler(new BookDupeListener());
        super.onEnable();
    }
}
