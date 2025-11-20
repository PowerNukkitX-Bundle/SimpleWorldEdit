package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.object.BlockManager;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.Selection;

import java.util.Map;

public class ReplaceCommand extends PluginCommand<SimpleWorldEdit> {

    public ReplaceCommand() {
        super("replace", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.replace");
        this.setDescription("Replaces blocks in your selection.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("tileName", false, CommandEnum.ENUM_BLOCK),
                CommandParameter.newEnum("replaceTileName", true, CommandEnum.ENUM_BLOCK)
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
                int minX = Math.min(selection.getFirst().getX(), selection.getSecond().getX());
                int minY = Math.min(selection.getFirst().getY(), selection.getSecond().getY());
                int minZ = Math.min(selection.getFirst().getZ(), selection.getSecond().getZ());
                int maxX = Math.max(selection.getFirst().getX(), selection.getSecond().getX());
                int maxY = Math.max(selection.getFirst().getY(), selection.getSecond().getY());
                int maxZ = Math.max(selection.getFirst().getZ(), selection.getSecond().getZ());
                Level level = p.getLevel();
                Block block = list.getResult(0);
                Block replacement = list.getResult(1, block);
                BlockManager manager = new BlockManager(level);
                for(int x = minX; x <= maxX; x++) {
                    for(int y = minY; y <= maxY; y++) {
                        for(int z = minZ; z <= maxZ; z++) {
                            Block current = level.getBlock(x, y, z);
                            if((replacement.getId().equals(block.getId()) && !current.isAir()) || current.getId().equals(block.getId())) {
                                manager.setBlockStateAt(x, y, z, replacement.getBlockState());
                            }
                        }
                    }
                }
                log.addSuccess("§dReplaced " + manager.getBlocks().size() + " blocks in your selection to " + replacement.getId() + ".");
                player.getHistory().add(manager);
                manager.applySubChunkUpdate();
            } else log.addError("§dYour selection is not valid.");
            log.output();
            return 1;
        } else return 0;
    }
}
