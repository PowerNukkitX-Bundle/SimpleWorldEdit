package org.powernukkitx.simpleworldedit.utils;

import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.SimpleAxisAlignedBB;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public SimpleAxisAlignedBB getBoundingBox() {
        return new SimpleAxisAlignedBB(first.asVector3(), second.asVector3());
    }

    public void set(AxisAlignedBB boundingBox) {
        this.clear();
        this.setFirst(boundingBox.minCorner().asBlockVector3());
        this.setSecond(boundingBox.maxCorner().asBlockVector3());
    }
}
