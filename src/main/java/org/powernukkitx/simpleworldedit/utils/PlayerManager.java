package org.powernukkitx.simpleworldedit.utils;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;

import java.util.HashMap;

public class PlayerManager implements Listener {

    protected final static HashMap<Player, SWEPlayer> PLAYERS = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PLAYERS.put(player, new SWEPlayer(player));
    }

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
        return PLAYERS.get(player);
    }

}
