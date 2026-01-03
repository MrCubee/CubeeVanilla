package fr.mrcubee.vanilla.nms;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackNMS {

    public static boolean mineBlock(final Player player, final Block block, final ItemStack itemStack) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final CraftBlock craftBlock = (CraftBlock) block;
        final net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        nmsItemStack.a(((CraftWorld) block.getWorld()).getHandle(), craftBlock.getNMS(), craftBlock.getPosition(), craftPlayer.getHandle());
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItemStack));

        return nmsItemStack.f();
    }

}
