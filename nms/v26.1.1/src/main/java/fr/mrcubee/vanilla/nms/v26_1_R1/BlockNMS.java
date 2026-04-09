package fr.mrcubee.vanilla.nms.v26_1_R1;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.level.block.state.BlockState;

public class BlockNMS extends fr.mrcubee.vanilla.nms.BlockNMS {

    @Override
    protected int nmsGetExpToDrop(final Block block, final ItemStack itemStack) {
        final CraftBlock craftBlock;
        final BlockState nmsData;
        final net.minecraft.world.level.block.Block nmsBlock;
        final net.minecraft.world.item.ItemStack nmsItemStack;

        if (block == null || itemStack == null)
            return 0;
        craftBlock = (CraftBlock) block;
        nmsData = craftBlock.getNMS();
        nmsBlock = nmsData.getBlock();
        nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsBlock.getExpDrop(nmsData, ((CraftWorld) block.getWorld()).getHandle(), craftBlock.getPosition(),
                nmsItemStack, true);
    }

}
