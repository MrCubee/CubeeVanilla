package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.CubeeVanillaPlugin;
import fr.mrcubee.vanilla.timber.listener.BlockBreakListener;
import fr.mrcubee.vanilla.timber.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class Timber {

    protected static final Set<Material> WOOD_MATERIALS = Arrays.stream(Material.values())
            .filter(material -> material.name().endsWith("_WOOD"))
            .collect(Collectors.toSet());
    protected static final Set<Material> LEAVES_MATERIALS = Arrays.stream(Material.values())
            .filter(material -> material.name().endsWith("_LEAVES"))
            .collect(Collectors.toSet());
    protected static final int QUEUE_SIZE_LIMIT = 1000;

    private static final BlockBreakListener BLOCK_BREAK_LISTENER = new BlockBreakListener();
    private static final PlayerQuitListener PLAYER_QUIT_LISTENER = new PlayerQuitListener();

    private static BukkitTask timberUpdateTask;
    private static final Map<Player, TimberTask> PLAYER_TASK = new HashMap<Player, TimberTask>();

    public static void enable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        Bukkit.getScheduler().runTaskTimer(cubeeVanillaPlugin, Timber::update, 0L, 0L);
        Bukkit.getPluginManager().registerEvents(BLOCK_BREAK_LISTENER, cubeeVanillaPlugin);
        Bukkit.getPluginManager().registerEvents(PLAYER_QUIT_LISTENER, cubeeVanillaPlugin);
    }

    public static void disable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        Timber.timberUpdateTask.cancel();
        Timber.timberUpdateTask = null;
        BlockBreakEvent.getHandlerList().unregister(BLOCK_BREAK_LISTENER);
        PlayerQuitEvent.getHandlerList().unregister(PLAYER_QUIT_LISTENER);
    }

    public static boolean executePlayer(final Player player, final Block block) {
        final ItemStack itemStack;

        if (player == null || block == null)
            return false;
        itemStack = player.getInventory().getItemInMainHand();
        if (!player.isSneaking())
            return false;
        if (Timber.PLAYER_TASK.containsKey(player))
            return false;
        if (itemStack == null || !itemStack.getType().name().endsWith("_AXE"))
            return false;
        if (!WOOD_MATERIALS.contains(block.getType()))
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

    public static void update() {
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
