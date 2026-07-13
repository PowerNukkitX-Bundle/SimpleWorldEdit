package org.powernukkitx.simpleworldedit.commands;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.level.structure.Structure;
import org.powernukkitx.simpleworldedit.SimpleWorldEdit;
import org.powernukkitx.simpleworldedit.clipboard.ClipboardEntry;
import org.powernukkitx.simpleworldedit.manager.SWEPlayer;
import org.powernukkitx.simpleworldedit.utils.PlayerManager;
import org.powernukkitx.simpleworldedit.utils.Selection;

public class CopyCommand extends PluginCommand<SimpleWorldEdit> {

    public CopyCommand() {
        super("copy", SimpleWorldEdit.get());
        this.setPermission("simpleworldedit.command.copy");
        this.setDescription("Copy your selection to your clipboard.");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
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
                Structure structure = Structure.create(p.getLevel(), minX, minY, minZ, (maxX - minX) + 1, (maxY - minY) + 1, (maxZ - minZ) + 1);
                ClipboardEntry entry = new ClipboardEntry(structure);
                player.getClipboard().add(entry);
                sender.sendMessage("§dCopied selection to your clipboard.");
            } else sender.sendMessage("§dYour selection is not valid.");
            return true;
        } else return false;
    }
}
