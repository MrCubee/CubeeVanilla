package fr.mrcubee.vanilla.miner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayDeque;
import java.util.Queue;

public class Selection {

    private Location leftPos;
    private Location rightPos;

    public boolean isReady() {
        return this.leftPos != null && this.rightPos != null
                && this.leftPos.getWorld() != null
                && this.rightPos.getWorld() != null
                && this.leftPos.getWorld().equals(this.rightPos.getWorld());
    }

    public void setLeftPos(final Location loc) {
        this.leftPos = loc;
    }

    public void setRightPos(final Location loc) {
        this.rightPos = loc;
    }

    public boolean isInside(final Location location) {
        final Location minPos;
        final Location maxPos;

        if (!isReady())
            return false;
        minPos = getMinPos();
        maxPos = getMaxPos();
        return  location.getX() >= minPos.getX() && location.getX() <= maxPos.getX()
                && location.getY() >= minPos.getY() && location.getY() <= maxPos.getY()
                && location.getZ() >= minPos.getZ() && location.getZ() <= maxPos.getZ();
    }

    public Location getMinPos() {
        if (!isReady())
            return null;
        return new Location(this.leftPos.getWorld(),
                Math.min(this.leftPos.getX(), this.rightPos.getX()),
                Math.min(this.leftPos.getY(), this.rightPos.getY()),
                Math.min(this.leftPos.getZ(), this.rightPos.getZ())
        );
    }

    public Location getMaxPos() {
        if (!isReady())
            return null;
        return new Location(this.leftPos.getWorld(),
                Math.max(this.leftPos.getX(), this.rightPos.getX()),
                Math.max(this.leftPos.getY(), this.rightPos.getY()),
                Math.max(this.leftPos.getZ(), this.rightPos.getZ())
        );
    }

    private void addLevelBlocks(final Queue<Block> blocks, final Location min, final Location max, final int blockY) {
        final Location current = min.clone();
        Block currentBlock;

        current.setY(blockY);
        for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
            current.setZ(z);
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                current.setX(x);
                currentBlock = current.getBlock();
                if (currentBlock.getType() != Material.AIR)
                    blocks.offer(currentBlock);
            }
        }
    }

    public Queue<Block> getBlocks() {
        final Queue<Block> blocks;
        final Location minPos;
        final Location maxPos;

        if (!isReady())
            return null;
        minPos = getMinPos();
        maxPos = getMaxPos();
        blocks = new ArrayDeque<Block>(
                (maxPos.getBlockX() - minPos.getBlockX() + 1)
                * (maxPos.getBlockY() - minPos.getBlockY() + 1)
                * (maxPos.getBlockZ() - minPos.getBlockZ() + 1)
        );
        for (int y = maxPos.getBlockY(); y >= minPos.getBlockY(); --y)
            addLevelBlocks(blocks, minPos, maxPos, y);
        return blocks;
    }
}
