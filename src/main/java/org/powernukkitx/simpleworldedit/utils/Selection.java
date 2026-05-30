package org.powernukkitx.simpleworldedit.utils;

import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.SimpleAxisAlignedBB;

public class Selection {

    protected BlockVector3 first;
    protected BlockVector3 second;

    public void clear() {
        this.first = null;
        this.second = null;
    }

    public boolean isValid() {
        return first != null && second != null;
    }

    public BlockVector3 getFirst() {
        return first;
    }

    public void setFirst(BlockVector3 first) {
        this.first = first;
    }

    public BlockVector3 getSecond() {
        return second;
    }

    public void setSecond(BlockVector3 second) {
        this.second = second;
    }

    public SimpleAxisAlignedBB getBoundingBox() {
        return new SimpleAxisAlignedBB(first.asVector3(), second.asVector3());
    }

    public void set(AxisAlignedBB boundingBox) {
        this.clear();
        this.setFirst(boundingBox.minCorner().asBlockVector3());
        this.setSecond(boundingBox.maxCorner().asBlockVector3());
    }
}
