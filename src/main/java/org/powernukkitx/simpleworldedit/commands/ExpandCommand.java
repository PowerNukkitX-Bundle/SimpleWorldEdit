package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.DimensionData;
import cn.nukkit.math.SimpleAxisAlignedBB;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.Selection;

import java.util.Map;

public class ExpandCommand extends PluginCommand<SimpleWorldEdit> {

    public ExpandCommand() {
        super("expand", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.expand");
        this.setDescription("Expands your selection");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("direction", false, new String[]{"up", "down", "north", "east", "south", "west", "vert"}),
                CommandParameter.newType("amount", true, CommandParamType.INT)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            Selection selection = player.getSelection();
            if(selection.isValid()) {
                SimpleAxisAlignedBB boundingBox = selection.getBoundingBox();
                String direction = list.getResult(0);
                int amount = list.getResult(1, 0);
                switch (direction) {
                    case "up" -> boundingBox.setMaxY(Math.max(boundingBox.getMaxY() + amount, boundingBox.getMinY()));
                    case "down" -> boundingBox.setMinY(Math.min(boundingBox.getMinY() - amount, boundingBox.getMaxY()));
                    case "north" ->  boundingBox.setMinZ(Math.min(boundingBox.getMinZ() - amount, boundingBox.getMaxZ()));
                    case "south" -> boundingBox.setMaxZ(Math.max(boundingBox.getMaxZ() + amount, boundingBox.getMinZ()));
                    case "west" ->  boundingBox.setMinX(Math.min(boundingBox.getMinX() - amount, boundingBox.getMaxX()));
                    case "east" -> boundingBox.setMaxX(Math.max(boundingBox.getMaxX() + amount, boundingBox.getMinX()));
                    case "vert" -> {
                        DimensionData data = p.getLevel().getDimensionData();
                        boundingBox.setMinY(data.getMinHeight());
                        boundingBox.setMaxY(data.getMaxHeight());
                    }
                }
                selection.set(boundingBox);
                if(direction.equals("vert")) {
                    log.addSuccess("§dExpanded your selection vertical.");
                } else log.addSuccess("§dExpanded your selection " + amount + " blocks " + direction + ".");
            } else log.addError("§dYour selection is not valid.");
            log.output();
            return 1;
        } else return 0;
    }
}
