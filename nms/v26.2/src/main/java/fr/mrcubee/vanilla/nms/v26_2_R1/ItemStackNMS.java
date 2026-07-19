package fr.mrcubee.vanilla.nms.v26_2_R1;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class ItemStackNMS extends fr.mrcubee.vanilla.nms.ItemStackNMS {

    @Override
    protected boolean nmsMineBlock(final Player player, final Block block, final ItemStack itemStack) {
        final BlockState nmsBlockState;
        final ServerLevel serverLevel;
        final BlockPos blockPos;
        final ServerPlayer serverPlayer;
        final net.minecraft.world.item.ItemStack nmsItemStack;

        if (player == null || block == null)
            return false;
        serverLevel = ((CraftWorld) block.getWorld()).getHandle();
        nmsBlockState = ((CraftBlockData) block.getBlockData()).getState();
        blockPos = ((CraftBlock) block).getPosition();
        serverPlayer = ((CraftPlayer) player).getHandle();
        if (itemStack != null) {
            nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
            nmsItemStack.mineBlock(serverLevel, nmsBlockState, blockPos, serverPlayer);
            block.breakNaturally(itemStack);
            itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItemStack));
            return nmsItemStack.isEmpty();
        } else {
            block.breakNaturally();
            return false;
        }
    }

}