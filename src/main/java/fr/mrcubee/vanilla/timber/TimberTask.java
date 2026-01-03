package fr.mrcubee.vanilla.timber;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayDeque;
import java.util.Queue;

public abstract class TimberTask {

    protected final Player player;
    protected final ItemStack itemStack;
    protected final Queue<Block> blockToDestroy;

    protected TimberTask(final Player player, final ItemStack itemStack, final Queue<Block> blockToDestroy) {
        this.player = player;
        this.itemStack = itemStack;
        this.blockToDestroy = blockToDestroy;
    }

    protected TimberTask(final Player player, final ItemStack itemStack) {
        this(player, itemStack, new ArrayDeque<Block>(400));
    }

    protected TimberTask(final TimberTask task) {
        this(task.player, task.itemStack, task.blockToDestroy);
    }

    public abstract boolean update();

    public abstract TimberTask newTask();

}
