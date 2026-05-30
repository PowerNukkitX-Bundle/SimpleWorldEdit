package org.powernukkitx.simpleworldedit.utils;

import cn.nukkit.block.BlockState;
import cn.nukkit.level.structure.Structure;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.StructureRotationUtil;
import org.cloudburstmc.protocol.bedrock.data.structure.Rotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SWEStructure extends Structure {

    public SWEStructure(BlockState[][][][] blockStates, Map<Vector3, CompoundTag> blockEntities, List<CompoundTag> entities, int sizeX, int sizeY, int sizeZ, int x, int y, int z) {
        super(blockStates, blockEntities, entities, sizeX, sizeY, sizeZ, x, y, z);
    }

    public Structure rotate(double angle) {
        Rotation rotation = getRotationFromDegrees(angle);
        double theta = Math.toRadians(angle);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        int sizeX = getSizeX();
        int sizeY = getSizeY();
        int sizeZ = getSizeZ();

        double cx = (sizeX - 1) / 2.0;
        double cz = (sizeZ - 1) / 2.0;

        double minRX = Double.POSITIVE_INFINITY, maxRX = Double.NEGATIVE_INFINITY;
        double minRZ = Double.POSITIVE_INFINITY, maxRZ = Double.NEGATIVE_INFINITY;

        for (int sx : new int[]{0, sizeX - 1}) {
            for (int sz : new int[]{0, sizeZ - 1}) {
                double relX = sx - cx;
                double relZ = sz - cz;

                double rx = relX * cos - relZ * sin;
                double rz = relX * sin + relZ * cos;

                if (rx < minRX) minRX = rx;
                if (rx > maxRX) maxRX = rx;
                if (rz < minRZ) minRZ = rz;
                if (rz > maxRZ) maxRZ = rz;
            }
        }

        int newSizeX = (int) Math.ceil(maxRX - minRX) + 1;
        int newSizeZ = (int) Math.ceil(maxRZ - minRZ) + 1;
        int newSizeY = sizeY;

        BlockState[][][][] rotatedStates = new BlockState[2][newSizeX][sizeY][newSizeZ];
        Map<Vector3, CompoundTag> rotatedBlockEntities = new HashMap<>();

        for (int layer = 0; layer < 2; layer++) {
            for (int y = 0; y < sizeY; y++) {
                for (int nx = 0; nx < newSizeX; nx++) {
                    for (int nz = 0; nz < newSizeZ; nz++) {

                        double worldX = minRX + nx;
                        double worldZ = minRZ + nz;

                        double srcRelX = worldX * cos + worldZ * sin;
                        double srcRelZ = -worldX * sin + worldZ * cos;

                        double srcXf = srcRelX + cx;
                        double srcZf = srcRelZ + cz;

                        int sx = (int) Math.round(srcXf);
                        int sz = (int) Math.round(srcZf);

                        if (sx >= 0 && sx < sizeX && sz >= 0 && sz < sizeZ) {
                            rotatedStates[layer][nx][y][nz] = rotate(rotation, getBlockStates()[layer][sx][y][sz]);
                        }
                    }
                }
            }
        }

        for (var entry : getBlockEntities().entrySet()) {
            Vector3 pos = entry.getKey();
            CompoundTag nbt = entry.getValue();

            double relX = pos.x - cx;
            double relZ = pos.z - cz;

            double rx = relX * cos - relZ * sin;
            double rz = relX * sin + relZ * cos;

            int nx = (int) Math.round(rx - minRX);
            int nz = (int) Math.round(rz - minRZ);
            int ny = (int) pos.y;

            if (nx >= 0 && nx < newSizeX && nz >= 0 && nz < newSizeZ) {
                rotatedBlockEntities.put(new Vector3(nx, ny, nz), nbt);
            }
        }

        List<CompoundTag> rotatedEntities = new ArrayList<>(getEntities());

        return new Structure(rotatedStates, rotatedBlockEntities, rotatedEntities,
                newSizeX, newSizeY, newSizeZ, getX(), getY(), getZ());
    }

    public static Rotation getRotationFromDegrees(double degrees) {
        double normalized = (((degrees % 360) + 360) % 360);
        int quadrant = (int) Math.floor((normalized + 45) / 90) % 4;
        return switch (quadrant) {
            case 1 -> Rotation.ROTATE_90;
            case 2 -> Rotation.ROTATE_180;
            case 3 -> Rotation.ROTATE_270;
            default -> Rotation.NONE;
        };
    }

    protected BlockState rotate(Rotation rotation, BlockState state) {
        return switch (rotation) {
            case ROTATE_90 -> StructureRotationUtil.clockwise90(state);
            case ROTATE_180 -> StructureRotationUtil.clockwise180(state);
            case ROTATE_270 -> StructureRotationUtil.counterclockwise90(state);
            default -> state;
        };
    }

}
