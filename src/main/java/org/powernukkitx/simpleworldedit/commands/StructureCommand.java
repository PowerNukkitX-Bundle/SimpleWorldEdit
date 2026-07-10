package org.powernukkitx.simpleworldedit.commands;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.utils.CommandLogger;
import org.powernukkitx.level.structure.Structure;
import org.powernukkitx.level.structure.StructureAPI;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.clipboard.Clipboard;
import org.powernukkitx.simpleworldedit.clipboard.ClipboardEntry;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

import java.util.Map;

public class StructureCommand extends PluginCommand<SimpleWorldEdit> {

    public StructureCommand() {
        super("structure", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.structure");
        this.setDescription("Save or load structures.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("operation", false, new String[]{"save", "load"}),
                CommandParameter.newType("structure", false, CommandParamType.RAW_TEXT)
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
            String name = list.getResult(1);
            switch (type)  {
                case "save" -> {
                    if(clipboard.getSize() != 0) {
                        ClipboardEntry entry = clipboard.get();
                        StructureAPI.save(entry.compile(), name);
                        log.addSuccess("§dSaved the structure " + name);
                    } else log.addError("§dYour clipboard is empty!");
                }
                case "load" -> {
                    if(StructureAPI.exists(name)) {
                        Structure structure = StructureAPI.load(name);
                        clipboard.add(new ClipboardEntry(structure));
                        log.addSuccess("§dLoaded structure " + name + " into your clipboard.");
                    } else log.addError("§dThere is no structure called " + name);
                }
            }
            log.output();
            return 1;
        } else return 0;
    }
}
