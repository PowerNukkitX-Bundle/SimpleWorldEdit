package org.powernukkitx.simpleworldedit.manager;

import org.powernukkitx.Player;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.history.History;
import org.powernukkitx.simpleworldedit.utils.Selection;

public class SWEPlayer {


    protected final Player player;

    protected final Selection selection;
    protected final Clipboard clipboard;
    protected final History history;

    public SWEPlayer(Player player) {
        this.player = player;
        this.selection = new Selection();
        this.clipboard = new Clipboard();
        this.history = new History();
    }

    public Player getPlayer() {
        return player;
    }

    public Selection getSelection() {
        return selection;
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

    public History getHistory() {
        return history;
    }
}
