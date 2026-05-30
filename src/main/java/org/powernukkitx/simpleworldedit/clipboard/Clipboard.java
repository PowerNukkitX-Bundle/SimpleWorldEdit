package org.powernukkitx.simpleworldedit.clipboard;

import java.util.ArrayList;
import java.util.List;

public class Clipboard {

    public static Integer CLIPBOARD_SIZE = 32;

    protected int selectedItem;
    protected final List<ClipboardEntry> entries = new ArrayList<>();

    public void add(ClipboardEntry entry) {
        if(entries.size() > CLIPBOARD_SIZE) {
            clearLast();
        }
        entries.addFirst(entry);
        selectedItem = 0;
    }

    public ClipboardEntry select(int slot) {
        this.selectedItem = slot;
        return get();
    }

    public ClipboardEntry get() {
        return entries.get(selectedItem);
    }

    public void clearLast() {
        entries.removeLast();
    }

    public int getSize() {
        return entries.size();
    }
}
