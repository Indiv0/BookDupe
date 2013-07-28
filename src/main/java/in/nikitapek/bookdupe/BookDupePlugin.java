package in.nikitapek.bookdupe;

import in.nikitapek.bookdupe.events.BookDupeListener;

import com.amshulman.mbapi.MbapiPlugin;

public final class BookDupePlugin extends MbapiPlugin {
    @Override
    public void onEnable() {
        registerEventHandler(new BookDupeListener());
        super.onEnable();
    }
}
