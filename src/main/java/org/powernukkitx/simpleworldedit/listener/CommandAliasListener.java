package org.powernukkitx.simpleworldedit.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandAliasListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String name = event.getMessage();
        String command = switch (name) {
            case "//1",
                 "//pos1",
                 "/first" -> "pos first";
            case "//2",
                 "//pos2",
                 "/second"-> "pos second";
            default -> null;
        };
        if(command != null) {
            Server.getInstance().executeCommand(player, command);
            if(Server.getInstance().getCommandMap().getCommand(name.replaceFirst("/", "")) == null) {
                event.setCancelled();
            }
        }
    }

}
