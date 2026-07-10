package org.powernukkitx.simpleworldedit.listener;

import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.player.PlayerCommandPreprocessEvent;

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
