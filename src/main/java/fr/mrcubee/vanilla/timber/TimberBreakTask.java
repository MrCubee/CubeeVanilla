package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.nms.ItemStackNMS;
import fr.mrcubee.vanilla.timber.event.TimberBlockBreakEvent;
import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;

public class TimberBreakTask extends TimberTask {

    private boolean itemBroken;

    public TimberBreakTask(final TimberSearchTask task) {
        super(task);
        this.itemBroken = false;
    }

    private boolean breakTree() {
        final Block block = this.blockToDestroy.poll();
        final Material blockType;
        final TimberBlockBreakEvent event;

        if (block == null)
            return true;
        blockType = block.getType();
        event = new TimberBlockBreakEvent(block, this.player, this.itemStack);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return this.blockToDestroy.isEmpty();
        if (ItemStackNMS.mineBlock(this.player, block,
                (Tag.LEAVES.isTagged(blockType) || Tag.WART_BLOCKS.isTagged(blockType))
                        ? null
                        : this.itemStack)) {
            this.itemBroken = true;
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            player.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0.5, 0.5, 0.5), 1);
            return true;
        }
        player.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0.5, 0.5, 0.5), 1);
        return this.blockToDestroy.isEmpty();
    }

    @Override
    protected boolean update() {
        boolean remove = false;

        for (int i = 0; i < 10 && !(remove = breakTree()); ++i);
        return remove;
    }

    @Override
    public TimberTask newTask() {
        if (this.itemBroken)
            return null;
        PlayerUtils.giveItem(this.player, this.itemStack);
        return null;
    }
}
