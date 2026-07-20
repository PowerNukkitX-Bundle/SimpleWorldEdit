package org.powernukkitx.simpleworldedit.clipboard;

import org.cloudburstmc.protocol.bedrock.data.payload.structure.Rotation;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.BlockStructureVoid;
import org.powernukkitx.level.structure.Structure;
import org.powernukkitx.math.Vector3;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.utils.StructureRotationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClipboardEntry {

    private static final BlockState VOID = BlockStructureVoid.PROPERTIES.getDefaultState();

    private float rotationXZ;
    private final Structure structure;

    public ClipboardEntry(Structure structure) {
        this.structure = structure;
    }

    public void rotate(float rotation) {
        this.rotationXZ += rotation;
    }

    public Structure compile() {
        return rotate();
    }

    protected Structure rotate() {
        float normalized = (((rotationXZ % 360) + 360) % 360);

        int quadrant = (int) Math.floor((normalized + 45) / 90) % 4;

        Rotation rotation = switch (quadrant) {
            case 1 -> Rotation.ROTATE_90;
            case 2 -> Rotation.ROTATE_180;
            case 3 -> Rotation.ROTATE_270;
            default -> Rotation.NONE;
        };

        double theta = Math.toRadians(normalized);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        int sizeX = structure.getSizeX();
        int sizeY = structure.getSizeY();
        int sizeZ = structure.getSizeZ();

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

                        if (sx < 0 || sx >= sizeX || sz < 0 || sz >= sizeZ)
                            continue;

                        BlockState original = structure.getBlockStates()[layer][sx][y][sz];
                        if (original == null)
                            continue;

                        BlockState rotatedState = switch (rotation) {
                            case ROTATE_90 -> StructureRotationUtil.clockwise90(original);
                            case ROTATE_180 -> StructureRotationUtil.clockwise180(original);
                            case ROTATE_270 -> StructureRotationUtil.counterclockwise90(original);
                            default -> original;
                        };

                        rotatedStates[layer][nx][y][nz] = rotatedState;
                    }
                }
            }
        }

        for(int l = 0; l < rotatedStates.length; l++) {
            for(int x = 0; x < rotatedStates[l].length; x++) {
                for(int y = 0; y < rotatedStates[l][x].length; y++) {
                    for(int z = 0; z < rotatedStates[l][x][y].length; z++) {
                        if(rotatedStates[l][x][y][z] == null) {
                            rotatedStates[l][x][y][z] = VOID;
                        }
                    }
                }
            }
        }


        for (var entry : structure.getBlockEntities().entrySet()) {
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

        List<CompoundTag> rotatedEntities = new ArrayList<>(structure.getEntities());

        return new Structure(
                rotatedStates,
                rotatedBlockEntities,
                rotatedEntities,
                newSizeX, sizeY, newSizeZ,
                0, 0, 0
        );
    }

}
