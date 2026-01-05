package fr.mrcubee.vanilla.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PlayerTickSingleExecutor implements AutoCloseable {

    private BukkitTask bukkitTask;
    private Map<Player, PlayerTask> playerTasks;

    public PlayerTickSingleExecutor() {
        this.bukkitTask = null;
        this.playerTasks = new HashMap<Player, PlayerTask>();
    }

    public PlayerTickSingleExecutor(final Plugin plugin) {
        this();
        start(plugin);
    }

    public boolean submit(final PlayerTask task) {
        if (task == null || task.player == null || !task.player.isOnline() || this.playerTasks.containsKey(task.player))
            return false;
        task.onStart();
        this.playerTasks.put(task.player, task);
        return true;
    }

    public PlayerTask cancel(final Player player) {
        final PlayerTask task;
        if (player == null)
            return null;
        task = this.playerTasks.remove(player);
        if (task == null)
            return null;
        task.onComplete();
        return task;
    }

    public Set<PlayerTask> cancelAll() {
        final Set<PlayerTask> tasks = new HashSet<PlayerTask>(this.playerTasks.values());

        for (final PlayerTask task : tasks) {
            if (task != null)
                task.onComplete();
        }
        this.playerTasks.clear();
        return tasks;
    }

    private void tick() {
        final Iterator<Map.Entry<Player, PlayerTask>> iterator;
        Map.Entry<Player, PlayerTask> entry;
        PlayerTask playerTask;

        if (this.playerTasks.isEmpty())
            return;
        iterator = this.playerTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            playerTask = entry.getValue();
            if (playerTask == null)
                iterator.remove();
            else if (playerTask.tick()) {
                iterator.remove();
                submit(playerTask.onComplete());
            }
        }
    }

    public void start(final Plugin plugin) {
        if (plugin != null && this.bukkitTask == null)
            Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, 0L);
    }

    public void stop() {
        if (this.bukkitTask != null)
            this.bukkitTask.cancel();
    }

    @Override
    public void close() {
        stop();
    }

}
