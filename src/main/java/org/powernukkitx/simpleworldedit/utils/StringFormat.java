package org.powernukkitx.simpleworldedit.utils;

import cn.nukkit.math.BlockVector3;

public class StringFormat {

    public static String format(Object object) {
        return switch (object) {
            case BlockVector3 vector -> "X: " + vector.x + ", Y: " + vector.y + ", Z: " + vector.z;
            default -> object.toString();
        };
    }

}
