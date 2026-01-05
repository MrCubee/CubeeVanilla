package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.CubeeVanillaPlugin;
import fr.mrcubee.vanilla.task.PlayerTickSingleExecutor;
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

public class Timber {

    protected static final int QUEUE_SIZE_LIMIT = 4000;

    private static final Listener[] LISTENERS = new Listener[]{
            new BlockBreakListener(),
            new PlayerQuitListener()
    };

    private static final PlayerTickSingleExecutor PLAYER_TASK_EXECUTOR = new PlayerTickSingleExecutor();

    public static void enable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        final PluginManager pluginManager;

        if (cubeeVanillaPlugin == null)
            return;
        pluginManager = Bukkit.getPluginManager();
        for (final Listener listener : LISTENERS)
            pluginManager.registerEvents(listener, cubeeVanillaPlugin);
        Timber.PLAYER_TASK_EXECUTOR.start(cubeeVanillaPlugin);
    }

    public static void disable() {
        Timber.PLAYER_TASK_EXECUTOR.stop();
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
        if (itemStack == null || !itemStack.getType().name().endsWith("_AXE"))
            return false;
        if (!Tag.LOGS.isTagged(blockType) && !Tag.LEAVES.isTagged(blockType) && !Tag.WART_BLOCKS.isTagged(blockType))
            return false;
        if (!Timber.PLAYER_TASK_EXECUTOR.submit(new TimberSearchTask(player, itemStack, block)))
            return false;
        player.getInventory().setItemInMainHand(null);
        return true;
    }

    public static ItemStack cancelPlayer(final Player player) {
        final TimberTask task;

        if (player == null)
            return null;
        task = (TimberTask) Timber.PLAYER_TASK_EXECUTOR.cancel(player);
        return task != null ? task.getItemStack() : null;
    }

}