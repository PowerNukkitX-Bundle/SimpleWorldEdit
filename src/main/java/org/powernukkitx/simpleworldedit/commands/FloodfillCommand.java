package org.powernukkitx.simpleworldedit.commands;

import org.powernukkitx.Player;
import org.powernukkitx.block.Block;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.data.CommandEnum;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.utils.CommandLogger;
import org.powernukkitx.level.Level;
import org.powernukkitx.level.generator.object.BlockManager;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.history.History;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

import java.util.*;

public class FloodfillCommand extends PluginCommand<SimpleWorldEdit> {

    public FloodfillCommand() {
        super("floodfill", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.floodfill");
        this.setDescription("Fill holes using floodfill.");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("tileName", false, CommandEnum.ENUM_BLOCK),
                CommandParameter.newType("maxRadius", true, CommandParamType.INT)
        });
        this.enableParamTree();
    }

    private final int[][] DIRS = {
            { 1, 0},
            {-1, 0},
            { 0, 1},
            { 0,-1}
    };

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player p) {
            SWEPlayer player = PlayerManager.get(p);
            BlockState block = ((Block) list.getResult(0)).getBlockState();
            int maxRadius = list.getResult(1, 64);
            Level level = p.getLevel();
            BlockManager manager = new BlockManager(level);
            manager.setBlockStateAt(0,0,0, block);

            int startX = p.getFloorX();
            int startY = p.getFloorY();
            int startZ = p.getFloorZ();

            Queue<long[]> queue = new ArrayDeque<>();
            Set<Long> visited = new HashSet<>();

            long encode = (((long)startX & 0x3FFFFFF) << 26) | ((long)startZ & 0x3FFFFFF);
            queue.add(new long[]{startX, startZ});
            visited.add(encode);

            while(!queue.isEmpty()) {
                long[] pos = queue.poll();
                int x = (int) pos[0];
                int z = (int) pos[1];

                Block current = level.getBlock(x, startY, z);

                if(!current.isAir()) {
                    continue;
                }

                manager.setBlockStateAt(x, startY, z, block);

                for(int[] d : DIRS) {
                    int nx = x + d[0];
                    int nz = z + d[1];

                    if(Math.abs(nx - startX) + Math.abs(nz - startZ) > maxRadius) {
                        continue;
                    }

                    long key = (((long) nx & 0x3FFFFFF) << 26) | ((long) nz & 0x3FFFFFF);
                    if(visited.contains(key)) continue;

                    visited.add(key);
                    queue.add(new long[]{nx, nz});
                }
            }

            History history = player.getHistory();
            log.addSuccess("§dReplaced " + manager.getBlocks().size() + " blocks with " + block.getIdentifier() + ".");
            history.add(manager);
            manager.applySubChunkUpdate();
            log.output();
            return 1;
        } else return 0;
    }
}
