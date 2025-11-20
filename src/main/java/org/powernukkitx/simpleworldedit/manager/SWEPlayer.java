package org.powernukkitx.simpleworldedit.manager;

import cn.nukkit.Player;
import lombok.Getter;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.history.History;
import org.powernukkitx.simpleworldedit.utils.Selection;

@Getter
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
}
