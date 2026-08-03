package org.powernukkitx.simpleworldedit.commands;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.utils.CommandLogger;
import org.powernukkitx.level.Position;
import org.powernukkitx.level.generator.object.BlockManager;
import org.powernukkitx.level.structure.Structure;
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
                Structure structure = entry.compile();
                structure.preparePlace(position, manager);
                player.getHistory().add(manager);
                structure.place(position, true, manager);
                log.addSuccess("§dPasted your clipboard to " + StringFormat.format(position.asBlockVector3()) + ".");
            } else log.addError("§dYour clipboard is empty!");
            log.output();
            return 1;
        } else return 0;
    }
}
