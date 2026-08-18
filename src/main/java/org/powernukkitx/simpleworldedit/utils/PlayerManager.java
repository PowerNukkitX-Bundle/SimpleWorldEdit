package org.powernukkitx.simpleworldedit.utils;

import org.powernukkitx.Player;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.entity.EntityLevelChangeEvent;
import org.powernukkitx.event.player.PlayerJoinEvent;
import org.powernukkitx.event.player.PlayerQuitEvent;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;

import java.util.HashMap;

public class PlayerManager implements Listener {

    protected final static HashMap<Player, SWEPlayer> PLAYERS = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PLAYERS.remove(event.getPlayer());
    }

    @EventHandler
    public void onLevelSwitch(EntityLevelChangeEvent event) {
        if(event.getEntity() instanceof Player player) {
            get(player).getSelection().clear();
        }
    }

    public static SWEPlayer get(Player player) {
        return PLAYERS.computeIfAbsent(player, SWEPlayer::new);
    }

}
