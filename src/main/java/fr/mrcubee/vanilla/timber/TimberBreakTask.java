package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.nms.ItemStackNMS;
import fr.mrcubee.vanilla.timber.event.TimberBlockBreakEvent;
import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TimberBreakTask extends TimberTask {

    private boolean itemBroken;

    public TimberBreakTask(final TimberSearchTask task) {
        super(task);
        this.itemBroken = false;
    }

    @Override
    public boolean update() {
        final Block block = this.blockToDestroy.poll();
        final TimberBlockBreakEvent event;

        if (block == null)
            return true;
        event = new TimberBlockBreakEvent(block, this.player, this.itemStack);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return this.blockToDestroy.isEmpty();
        if (ItemStackNMS.mineBlock(this.player, block, this.itemStack)) {
            this.itemBroken = true;
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            return true;
        }
        return this.blockToDestroy.isEmpty();
    }

    @Override
    public TimberTask newTask() {
        if (this.itemBroken)
            return null;
        PlayerUtils.giveItem(this.player, this.itemStack);
        return null;
    }
}
