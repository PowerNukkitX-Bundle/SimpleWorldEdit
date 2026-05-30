package org.powernukkitx.simpleworldedit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.registry.Registries;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.Selection;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BiomeCommand extends PluginCommand<SimpleWorldEdit> {

    public BiomeCommand() {
        super("biome", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.biome");
        this.setDescription("Changes the biome in your selection.");
        this.commandParameters.clear();
        String[] biomeNames = Registries.BIOME.getBiomeDefinitions().stream()
                .map(definition -> Registries.BIOME.getFromBiomeStringList(definition.left()))
                .filter(Objects::nonNull)
                .filter(name -> !name.isBlank())
                .distinct()
                .toArray(String[]::new);
        Arrays.sort(biomeNames);
        this.commandParameters.put("default", new CommandParameter[]{
                biomeNames.length == 0
                        ? CommandParameter.newType("biome", false, org.cloudburstmc.protocol.bedrock.data.command.CommandParamType.RAW_TEXT)
                        : CommandParameter.newEnum("biome", false, biomeNames)
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
                String name = list.getResult(0);
                int biomeId = Registries.BIOME.getBiomeId(name);
                ObjectArraySet<IChunk> chunks = new ObjectArraySet<>();
                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        IChunk chunk = level.getChunk(x >> 4, z >> 4);
                        for (int y = minY; y <= maxY; y++) {
                            chunk.setBiomeId(x & 15, y, z & 15, biomeId);
                        }
                        chunks.add(chunk);
                    }
                }

                for (IChunk chunk : chunks) {
                    for (Player player1 : level.getPlayers().values()) {
                        level.requestChunk(chunk.getX(), chunk.getZ(), player1);
                    }
                }
                log.addSuccess("§dChanged the biome in your selection to" + name + ".");
            } else log.addError("§dYour selection is not valid.");
            log.output();
            return 1;
        } else return 0;
    }
}
