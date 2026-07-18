package fr.mrcubee.vanilla.nms;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockNMS {

    private static final BlockNMS BLOCK_NMS = getNMSInstance();
    
    protected int nmsGetExpToDrop(final Block block, final ItemStack itemStack) {
        return 0;
    }

    private static BlockNMS getNMSInstance() {
        final Class<?> clazz;
        final Class<? extends BlockNMS> blockClass;

        try {
            clazz =  Class.forName("fr.mrcubee.vanilla.nms." + NMS.getNMSVersion() + ".BlockNMS");
            blockClass = clazz.asSubclass(BlockNMS.class);
            return blockClass.getConstructor().newInstance();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return new BlockNMS();
    }

    public static int getExpToDrop(final Block block, final ItemStack itemStack) {
        return BlockNMS.BLOCK_NMS.nmsGetExpToDrop(block, itemStack);
    }    

}
