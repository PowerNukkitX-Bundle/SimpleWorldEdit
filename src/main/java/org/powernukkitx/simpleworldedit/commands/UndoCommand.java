package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.generator.object.BlockManager;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.history.History;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

import java.util.Map;

public class UndoCommand extends PluginCommand<SimpleWorldEdit> {

    public UndoCommand() {
        super("undo", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.undo");
        this.setDescription("Undo your latest block changes.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("amount", true, CommandParamType.INT)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            History history = player.getHistory();
            Integer amount = Math.min(list.getResult(0, 1), history.getSize());
            for(int i = 0; i < amount; i++) {
                BlockManager manager = history.get();
                manager.applySubChunkUpdate();
                history.clearFirst();
            }
            log.addSuccess("§dUndid your last " + amount + " changes.");
            log.output();
            return 1;
        } else return 0;
    }
}
