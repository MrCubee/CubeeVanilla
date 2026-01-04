package fr.mrcubee.vanilla.miner;

import fr.mrcubee.vanilla.miner.event.MinerBlockBreakEvent;
import fr.mrcubee.vanilla.nms.BlockNMS;
import fr.mrcubee.vanilla.nms.ItemStackNMS;
import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayDeque;
import java.util.Queue;

public class MinerTask {

    protected final Player player;
    protected final ItemStack itemStack;
    protected final Queue<Block> blockToDestroy;
    private boolean itemBroken;
    private int expLevel;

    protected MinerTask(final Player player, final ItemStack itemStack, final Queue<Block> blockToDestroy) {
        this.player = player;
        this.itemStack = itemStack;
        this.blockToDestroy = blockToDestroy;
        this.itemBroken = false;
        this.expLevel = 0;
    }

    protected MinerTask(final Player player, final ItemStack itemStack) {
        this(player, itemStack, new ArrayDeque<Block>(400));
    }

    protected MinerTask(final MinerTask task) {
        this(task.player, task.itemStack, task.blockToDestroy);
    }

    private boolean breakBlock() {
        final Block block = this.blockToDestroy.poll();
        final Material blockType;
        final MinerBlockBreakEvent event;
        final boolean isPreferredTool;
        final boolean canDrop;

        if (block == null)
            return true;
        event = new MinerBlockBreakEvent(block, this.player, this.itemStack);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return this.blockToDestroy.isEmpty();
        isPreferredTool = block.isPreferredTool(this.itemStack);
        canDrop = !block.getDrops(isPreferredTool ? this.itemStack : null, this.player).isEmpty();
        if (canDrop) {
            this.expLevel += BlockNMS.getExpToDrop(block, this.itemStack);
            if (ItemStackNMS.mineBlock(this.player, block, (isPreferredTool ? this.itemStack : null))) {
                this.itemBroken = true;
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                player.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0.5, 0.5, 0.5), 1);
                return true;
            }
            player.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0.5, 0.5, 0.5), 1);
        }
        return this.blockToDestroy.isEmpty();
    }

    protected boolean update() {
        boolean remove = false;

        for (int i = 0; i < 10 && !(remove = breakBlock()); ++i);
        return remove;
    }

    public void finish() {
        if (!this.itemBroken)
            PlayerUtils.giveItem(this.player, this.itemStack);
        if (this.expLevel > 0)
            this.player.giveExp(this.expLevel);
    }
}
