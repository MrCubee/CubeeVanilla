package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.task.PlayerItemTask;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayDeque;
import java.util.Queue;

public abstract class TimberTask extends PlayerItemTask {

    protected final Queue<Block> blockToDestroy;

    protected TimberTask(final Player player, final ItemStack itemStack, final Queue<Block> blockToDestroy) {
        super(player, itemStack);
        this.blockToDestroy = blockToDestroy;
    }

    protected TimberTask(final Player player, final ItemStack itemStack) {
        this(player, itemStack, new ArrayDeque<Block>(400));
    }

    protected TimberTask(final TimberTask task) {
        this(task.player, task.itemStack, task.blockToDestroy);
    }

    protected ItemStack getItemStack() {
        return this.itemStack;
    }

}
