package fr.mrcubee.vanilla.nms.v1_21_R7;

import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class BlockNMS extends fr.mrcubee.vanilla.nms.BlockNMS {

    @Override
    protected int nmsGetExpToDrop(final Block block, final ItemStack itemStack) {
        final CraftBlock craftBlock;
        final IBlockData nmsData;
        final net.minecraft.world.level.block.Block nmsBlock;
        final net.minecraft.world.item.ItemStack nmsItemStack;

        if (block == null || itemStack == null)
            return 0;
        craftBlock = (CraftBlock) block;
        nmsData = craftBlock.getNMS();
        nmsBlock = nmsData.b();
        nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsBlock.getExpDrop(nmsData, ((CraftWorld) block.getWorld()).getHandle(), craftBlock.getPosition(),
                nmsItemStack, true);
    }

}
