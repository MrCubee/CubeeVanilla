package fr.mrcubee.vanilla.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerItemTask extends PlayerTask {

    protected final ItemStack itemStack;

    public PlayerItemTask(final Player player, final int iterationsPerTick, final ItemStack itemStack) {
        super(player, iterationsPerTick);
        this.itemStack = itemStack;
    }

    public PlayerItemTask(final Player player, final ItemStack itemStack) {
        super(player);
        this.itemStack = itemStack;
    }

}
