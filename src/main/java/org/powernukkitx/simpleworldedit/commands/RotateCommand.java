package org.powernukkitx.simpleworldedit.commands;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.utils.CommandLogger;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
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
