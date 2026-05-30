package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

import java.util.Map;

public class ClipboardCommand extends PluginCommand<SimpleWorldEdit> {

    public ClipboardCommand() {
        super("clipboard", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.clipboard");
        this.setDescription("Select a different entry in your selection.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("operator", false, new String[]{"clear", "select"}),
                CommandParameter.newType("count", true, CommandParamType.INT)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            Clipboard clipboard = player.getClipboard();
            String type = list.getResult(0);
            switch (type)  {
                case "clear" -> {
                    Integer count = Math.abs(Math.min(list.getResult(1, clipboard.getSize()), clipboard.getSize()));
                    for(int i = 0; i < count; i++) {
                        clipboard.clearLast();
                    }
                    log.addSuccess("§dRemoved " + count + " entries from your clipboard.");
                }
                case "select" -> {
                    Integer count = Math.abs(list.getResult(1, 0));
                    if(count < clipboard.getSize()) {
                        clipboard.select(count);
                        log.addSuccess("§dYou selected the " + count + "th entry in your clipboard.");
                    } else log.addError("§dYou dont have " + count + " entries in your clipboard.");
                }
            }
            log.output();
            return 1;
        } else return 0;
    }
}
