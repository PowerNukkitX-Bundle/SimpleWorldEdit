package org.powernukkitx.simpleworldedit.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.ItemWoodenAxe;
import cn.nukkit.math.BlockVector3;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.StringFormat;

public class WandListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        SWEPlayer player = PlayerManager.get(p);
        if(event.getItem() instanceof ItemWoodenAxe) {
            if(p.hasPermission("simpleworldedit.wand")) {
                if(p.isCreative()) {
                    BlockVector3 position = event.getBlock().asBlockVector3();
                    player.getSelection().setFirst(position);
                    p.sendMessage("§dSet first position to " + StringFormat.format(position) + ".");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        SWEPlayer player = PlayerManager.get(p);
        if(event.getItem() instanceof ItemWoodenAxe) {
            if(p.hasPermission("simpleworldedit.wand")) {
                if(p.isCreative()) {
                    PlayerInteractEvent.Action action = event.getAction();
                     if(action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                         BlockVector3 position = event.getBlock().asBlockVector3();
                         player.getSelection().setSecond(position);
                        p.sendMessage("§dSet second position to " + StringFormat.format(position) + ".");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


}
