package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.CubeeVanillaPlugin;
import fr.mrcubee.vanilla.timber.listener.BlockBreakListener;
import fr.mrcubee.vanilla.timber.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Timber {

    protected static final int QUEUE_SIZE_LIMIT = 4000;

    private static final Listener[] LISTENERS = new Listener[]{
            new BlockBreakListener(),
            new PlayerQuitListener()
    };

    private static BukkitTask timberUpdateTask;
    private static final Map<Player, TimberTask> PLAYER_TASK = new HashMap<Player, TimberTask>();

    public static void enable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        Bukkit.getScheduler().runTaskTimer(cubeeVanillaPlugin, Timber::update, 0L, 0L);
        for (final Listener listener : LISTENERS)
            pluginManager.registerEvents(listener, cubeeVanillaPlugin);
    }

    public static void disable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        Timber.timberUpdateTask.cancel();
        Timber.timberUpdateTask = null;
        BlockBreakEvent.getHandlerList().unregister(LISTENERS[0]);
        PlayerQuitEvent.getHandlerList().unregister(LISTENERS[1]);
    }

    public static boolean executePlayer(final Player player, final Block block) {
        final ItemStack itemStack;
        final Material blockType;

        if (player == null || block == null)
            return false;
        itemStack = player.getInventory().getItemInMainHand();
        blockType = block.getType();
        if (!player.isSneaking())
            return false;
        if (Timber.PLAYER_TASK.containsKey(player))
            return false;
        if (itemStack == null || !itemStack.getType().name().endsWith("_AXE"))
            return false;
        if (!Tag.LOGS.isTagged(blockType) && !Tag.LEAVES.isTagged(blockType) && !Tag.WART_BLOCKS.isTagged(blockType))
            return false;
        player.getInventory().setItemInMainHand(null);
        Timber.PLAYER_TASK.put(player, new TimberSearchTask(player, itemStack, block));
        return true;
    }

    public static ItemStack cancelPlayer(final Player player) {
        final TimberTask task;

        if (player == null)
            return null;
        task = Timber.PLAYER_TASK.remove(player);
        return task != null ? task.itemStack : null;
    }

    private static void update() {
        final Iterator<Map.Entry<Player, TimberTask>> iterator;
        Map.Entry<Player, TimberTask> entry;
        TimberTask timberTask;
        TimberTask newTask;

        if (Timber.PLAYER_TASK.isEmpty())
            return;
        iterator = Timber.PLAYER_TASK.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            timberTask = entry.getValue();
            if (timberTask == null) {
                iterator.remove();
                continue;
            }
            if (!timberTask.update())
                continue;
            newTask = timberTask.newTask();
            if (newTask == null)
                iterator.remove();
            entry.setValue(newTask);
        }
    }
}