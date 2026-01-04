package fr.mrcubee.vanilla.miner;

import fr.mrcubee.vanilla.CubeeVanillaPlugin;
import fr.mrcubee.vanilla.miner.listener.BlockBreakListener;
import fr.mrcubee.vanilla.miner.listener.PlayerInteractListener;
import fr.mrcubee.vanilla.miner.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Miner {

    private static final Listener[] LISTENERS = new Listener[] {
            new BlockBreakListener(),
            new PlayerInteractListener(),
            new PlayerQuitListener()
    };

    private static BukkitTask timberUpdateTask;
    private static final Map<Player, Long> PLAYER_SELECTION_TIMEOUT = new HashMap<Player, Long>();
    private static final Map<Player, Selection> PLAYER_SELECTION = new HashMap<Player, Selection>();
    private static final Map<Player, MinerTask> PLAYER_TASK = new HashMap<Player, MinerTask>();

    public static void enable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        Bukkit.getScheduler().runTaskTimer(cubeeVanillaPlugin, Miner::update, 0L, 0L);
        for (final Listener listener : LISTENERS)
            pluginManager.registerEvents(listener, cubeeVanillaPlugin);
    }

    public static void disable(final CubeeVanillaPlugin cubeeVanillaPlugin) {
        Miner.timberUpdateTask.cancel();
        Miner.timberUpdateTask = null;
        BlockBreakEvent.getHandlerList().unregister(LISTENERS[0]);
        PlayerInteractEvent.getHandlerList().unregister(LISTENERS[1]);
        PlayerQuitEvent.getHandlerList().unregister(LISTENERS[2]);
    }

    public static boolean executePosSet(final Player player, final Block block, final Action action) {
        final ItemStack itemStack;
        final Selection selection;

        if (player == null || block == null || action == null)
            return false;
        itemStack = player.getInventory().getItemInMainHand();
        if (!player.isSneaking())
            return false;
        if (itemStack == null || itemStack.getType() != Material.STICK)
            return false;
        selection = PLAYER_SELECTION.computeIfAbsent(player, keyPlayer -> new Selection());
        switch (action) {
            case RIGHT_CLICK_BLOCK:
                selection.setRightPos(block.getLocation());
                Miner.PLAYER_SELECTION_TIMEOUT.put(player, System.currentTimeMillis() + 5000);
                player.sendMessage(ChatColor.GRAY + "Right pos set.");
                break;
            case LEFT_CLICK_BLOCK:
                selection.setLeftPos(block.getLocation());
                Miner.PLAYER_SELECTION_TIMEOUT.put(player, System.currentTimeMillis() + 5000);
                player.sendMessage(ChatColor.GRAY + "Left pos set.");
                break;
        }
        return true;
    }

    public static void resetSelection(final Player player) {
        if (player == null)
            return;
        player.sendMessage(ChatColor.GRAY + "Reset selection.");
        Miner.PLAYER_SELECTION.remove(player);
        Miner.PLAYER_SELECTION_TIMEOUT.remove(player);
    }

    public static boolean executePlayer(final Player player, final Block block) {
        final ItemStack itemStack;
        final Selection selection;

        if (player == null || block == null)
            return false;
        itemStack = player.getInventory().getItemInMainHand();
        if (!player.isSneaking())
            return false;
        if (Miner.PLAYER_TASK.containsKey(player))
            return false;
        if (itemStack == null || !itemStack.getType().name().endsWith("_PICKAXE"))
            return false;
        selection = Miner.PLAYER_SELECTION.get(player);
        if (selection == null || !selection.isReady() || !selection.isInside(block.getLocation()))
            return false;
        player.getInventory().setItemInMainHand(null);
        resetSelection(player);
        Miner.PLAYER_TASK.put(player, new MinerTask(player, itemStack, selection.getBlocks()));
        return true;
    }

    public static ItemStack cancelPlayer(final Player player) {
        final MinerTask task;

        if (player == null)
            return null;
        Miner.PLAYER_SELECTION.remove(player);
        task = Miner.PLAYER_TASK.remove(player);
        return task != null ? task.itemStack : null;
    }

    private static void minerUpdate() {
        final Iterator<Map.Entry<Player, MinerTask>> iterator;
        Map.Entry<Player, MinerTask> entry;
        MinerTask minerTask;

        if (Miner.PLAYER_TASK.isEmpty())
            return;
        iterator = Miner.PLAYER_TASK.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            minerTask = entry.getValue();
            if (minerTask == null || minerTask.update()) {
                iterator.remove();
                minerTask.finish();
            }
        }
    }

    private static void selectTimeoutUpdate() {
        final Iterator<Map.Entry<Player, Long>> iterator;
        Map.Entry<Player, Long> entry;
        Long timeout;

        if (Miner.PLAYER_TASK.isEmpty())
            return;
        iterator = Miner.PLAYER_SELECTION_TIMEOUT.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            timeout = entry.getValue();
            if (timeout == null || System.currentTimeMillis() >= timeout) {
                iterator.remove();
                resetSelection(entry.getKey());
            }
        }
    }

    private static void update() {
        minerUpdate();
        selectTimeoutUpdate();
    }
}
