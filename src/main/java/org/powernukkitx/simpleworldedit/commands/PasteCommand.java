package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.object.BlockManager;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.clipboard.ClipboardEntry;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.StringFormat;

import java.util.Map;

public class PasteCommand extends PluginCommand<SimpleWorldEdit> {

    public PasteCommand() {
        super("paste", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.paste");
        this.setDescription("Pastes your selected clipboard entry.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("position", true, CommandParamType.POSITION)
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
                Position position = list.getResult(0, p.getPosition());
                BlockManager manager = new BlockManager(p.getLevel());
                entry.compile().preparePlace(position, manager);
                player.getHistory().add(manager);
                manager.applySubChunkUpdate();
                log.addSuccess("§dPasted your clipboard to " + StringFormat.format(position.asBlockVector3()) + ".");
            } else log.addError("§dYour clipboard is empty!");
            log.output();
            return 1;
        } else return 0;
    }
}
