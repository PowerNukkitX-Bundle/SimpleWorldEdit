package org.powernukkitx.simpleworldedit;

import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import org.powernukkitx.simpleworldedit.commands.*;
import org.powernukkitx.simpleworldedit.listener.CommandAliasListener;
import org.powernukkitx.simpleworldedit.listener.WandListener;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;

public class SimpleWorldEdit extends PluginBase {

    private static SimpleWorldEdit INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        registerEvents();
        registerCommands();
    }

    public static SimpleWorldEdit get() {
        return INSTANCE;
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerManager(), this);
        pluginManager.registerEvents(new WandListener(), this);
        pluginManager.registerEvents(new CommandAliasListener(), this);
    }

    private void registerCommands() {
        CommandMap commandMap = getServer().getCommandMap();
        commandMap.register("position", new PositionCommand());
        commandMap.register("copy", new CopyCommand());
        commandMap.register("paste", new PasteCommand());
        commandMap.register("rotate", new RotateCommand());
        commandMap.register("structure", new StructureCommand());
        commandMap.register("clipboard", new ClipboardCommand());
        commandMap.register("undo", new UndoCommand());
        commandMap.register("set", new SetCommand());
        commandMap.register("replace", new ReplaceCommand());
        commandMap.register("biome", new BiomeCommand());
        commandMap.register("expand", new ExpandCommand());
        commandMap.register("floodfill", new FloodfillCommand());
    }
}
