package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class TimberSearchTask extends TimberTask {

    private static final BlockFace[] BLOCK_FACES = Arrays.stream(BlockFace.values())
            .filter(face -> face != BlockFace.SELF).toArray(BlockFace[]::new);

    private final Queue<Block> blocksToProcess;
    private boolean tree;

    protected TimberSearchTask(final Player player, final ItemStack itemStack, final Block block) {
        super(player, itemStack);
        this.blocksToProcess = new ArrayDeque<Block>(400);
        this.blocksToProcess.add(block);
    }

    public boolean update() {
        final Block block = this.blocksToProcess.poll();
        final Material blockMaterial = block.getType();
        final boolean isLeaves = Timber.LEAVES_MATERIALS.contains(blockMaterial);

        if (isLeaves || Timber.WOOD_MATERIALS.contains(blockMaterial)) {
            this.blockToDestroy.offer(block);
            if (this.blockToDestroy.size() >= Timber.QUEUE_SIZE_LIMIT)
                return true;
            tree = tree || isLeaves;
            for (final BlockFace face : BLOCK_FACES)
                this.blocksToProcess.offer(block.getRelative(face));
        }
        return this.blocksToProcess.isEmpty();
    }

    @Override
    public TimberTask newTask() {
        if (this.tree)
            return new TimberBreakTask(this);
        PlayerUtils.giveItem(this.player, this.itemStack);
        return null;
    }
}
