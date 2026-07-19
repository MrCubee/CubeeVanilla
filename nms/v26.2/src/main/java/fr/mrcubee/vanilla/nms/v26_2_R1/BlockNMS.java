package fr.mrcubee.vanilla.nms.v26_2_R1;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class BlockNMS extends fr.mrcubee.vanilla.nms.BlockNMS {

    @Override
    protected int nmsGetExpToDrop(final Block block, final ItemStack itemStack) {
        final CraftBlockData craftBlockData;
        final BlockState blockState;
        final ServerLevel serverLevel;
        final BlockPos blockPos;
        final net.minecraft.world.item.ItemStack nmsItemStack;

        if (block == null || itemStack == null)
            return 0;
        craftBlockData = (CraftBlockData) block.getBlockData();
        blockState = craftBlockData.getState();
        serverLevel = ((CraftWorld) block.getWorld()).getHandle();
        blockPos = ((CraftBlock) block).getPosition();
        nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        return blockState.getBlock().getExpDrop(blockState, serverLevel, blockPos, nmsItemStack, true);
    }


}
