package org.powernukkitx.simpleworldedit.history;

import cn.nukkit.block.Block;
import cn.nukkit.level.generator.object.BlockManager;

import java.util.ArrayList;
import java.util.List;

public class History {

    public static Integer HISTORY_SIZE = 32;

    protected final List<BlockManager> entries = new ArrayList<>();

    public void add(BlockManager actions) {
        BlockManager cache = new BlockManager(actions.getLevel());
        for(Block block : actions.getBlocks()) {
            cache.setBlockStateAt(block, block.getLevelBlockState());
        }
        if(entries.size() > HISTORY_SIZE) {
            entries.removeLast();
        }
        entries.addFirst(cache);
    }

    public BlockManager get() {
        return entries.getFirst();
    }

    public void clearFirst() {
        entries.removeFirst();
    }

    public int getSize() {
        return entries.size();
    }

}
