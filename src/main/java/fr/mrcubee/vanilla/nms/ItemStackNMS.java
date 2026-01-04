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
            final CraftPlayer craftPlayer;
            final CraftBlock craftBlock;
            final net.minecraft.world.item.ItemStack nmsItemStack;

            if (player == null || block == null)
                return false;
            craftPlayer = (CraftPlayer) player;
            craftBlock = (CraftBlock) block;
            if (itemStack != null) {
                nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
                nmsItemStack.a(((CraftWorld) block.getWorld()).getHandle(), craftBlock.getNMS(), craftBlock.getPosition(), craftPlayer.getHandle());
                block.breakNaturally(itemStack);
                itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItemStack));
                return nmsItemStack.f();
            } else {
                block.breakNaturally();
                return false;
            }
        }

    }
