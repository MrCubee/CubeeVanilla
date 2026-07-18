package fr.mrcubee.vanilla.nms;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackNMS {

    private static final ItemStackNMS ITEM_STACK_NMS = getNMSInstance();


    protected boolean nmsMineBlock(final Player player, final Block block, final ItemStack itemStack) {
        return false;
    }

    private static ItemStackNMS getNMSInstance() {
        final Class<?> clazz;
        final Class<? extends ItemStackNMS> itemStackClass;

        try {
            clazz =  Class.forName("fr.mrcubee.vanilla.nms." + NMS.getNMSVersion() + ".ItemStackNMS");
            itemStackClass = clazz.asSubclass(ItemStackNMS.class);
            return itemStackClass.getConstructor().newInstance();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return new ItemStackNMS();
    }

    public static boolean mineBlock(final Player player, final Block block, final ItemStack itemStack) {
        return ItemStackNMS.ITEM_STACK_NMS.nmsMineBlock(player, block, itemStack);
    }

}