package fr.mrcubee.vanilla.timber.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;

public class TimberBlockBreakEvent extends BlockExpEvent implements Cancellable {

    private final Player player;
    private final ItemStack itemStack;
    private boolean cancelled;

    public TimberBlockBreakEvent(final Block block, final Player player, final ItemStack itemStack) {
        super(block, 0);
        this.cancelled = false;
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getUseItem() {
        return this.itemStack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
