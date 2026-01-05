package fr.mrcubee.vanilla.timber;

import fr.mrcubee.vanilla.task.PlayerTask;
import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TimberSearchTask extends TimberTask {

    private static final BlockFace[] BLOCK_FACES = Arrays.asList(
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    ).toArray(BlockFace[]::new);
    private final Set<Block> blockAlreadyCheck;
    private final Queue<Block> blocksToProcess;
    private boolean tree;

    protected TimberSearchTask(final Player player, final ItemStack itemStack, final Block block) {
        super(player, itemStack);
        this.blockAlreadyCheck = new HashSet<Block>(400);
        this.blocksToProcess = new ArrayDeque<Block>(400);
        this.blockAlreadyCheck.add(block);
        this.blocksToProcess.offer(block);
    }

    @Override
    protected boolean update() {
        final Block block = this.blocksToProcess.poll();
        final Material blockMaterial;
        final boolean isLeaves;
        Block relativeBlock;

        if (block == null)
            return true;
        blockMaterial = block.getType();
        isLeaves = Tag.LEAVES.isTagged(blockMaterial) || Tag.WART_BLOCKS.isTagged(blockMaterial);
        if (isLeaves || Tag.LOGS.isTagged(blockMaterial)) {
            player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.BEDROCK));
            this.blockToDestroy.offer(block);
            if (this.blockToDestroy.size() >= Timber.QUEUE_SIZE_LIMIT)
                return true;
            tree = tree || isLeaves;
            for (final BlockFace face : BLOCK_FACES) {
                relativeBlock = block.getRelative(face);
                if (!this.blockAlreadyCheck.contains(relativeBlock)) {
                    this.blockAlreadyCheck.add(relativeBlock);
                    this.blocksToProcess.offer(relativeBlock);
                }
            }
        }
        return this.blocksToProcess.isEmpty();
    }

    @Override
    protected PlayerTask onComplete() {
        for (final Block block : this.blockToDestroy)
            this.player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(block.getType()));
        if (this.tree)
            return new TimberBreakTask(this);
        PlayerUtils.giveItem(this.player, this.itemStack);
        return null;
    }

}
