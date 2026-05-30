package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.math.BlockVector3;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.Selection;
import org.powernukkitx.simpleworldedit.utils.StringFormat;

import java.util.Map;

public class PositionCommand extends PluginCommand<SimpleWorldEdit> {

    public PositionCommand() {
        super("position", SimpleWorldEdit.get());
        this.setAliases(new String[]{"pos", "loc"});
        this.setPermission("simpleworldedit.command.position");
        this.setDescription("Sets a position of your selection.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("postype", new String[]{"first", "second"}),
                CommandParameter.newType("position", true, CommandParamType.POSITION)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            Selection selection = player.getSelection();
            String type = list.getResult(0);
            BlockVector3 position = list.getResult(1, p.getPosition()).asBlockVector3();
            switch (type) {
                case "first" -> selection.setFirst(position);
                case "second" -> selection.setSecond(position);
            }
            log.addSuccess("§dSet " + type + " position to " + StringFormat.format(position) + ".").output();
            return 1;
        } else return 0;
    }
}
