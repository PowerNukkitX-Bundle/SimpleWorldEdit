package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.clipboard.ClipboardEntry;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

import java.util.Map;

public class RotateCommand extends PluginCommand<SimpleWorldEdit> {

    public RotateCommand() {
        super("rotate", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.rotate");
        this.setDescription("Rotates your selected clipboard entry.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("rotation", CommandParamType.FLOAT)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            Clipboard clipboard = player.getClipboard();
            if(clipboard.getSize() != 0) {
                ClipboardEntry entry = clipboard.get();
                float rotation = list.getResult(0);
                entry.rotate(rotation);
                log.addSuccess("§dRotated your clipboard.");
            } else log.addError("§dYour clipboard is empty!");
            log.output();
            return 1;
        } else return 0;
    }
}
