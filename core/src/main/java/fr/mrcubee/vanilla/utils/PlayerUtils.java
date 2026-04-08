package fr.mrcubee.vanilla.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerUtils {

    public static void giveItem(final Player player, final ItemStack... items) {
        final HashMap<Integer, org.bukkit.inventory.ItemStack> leftOvers;

        if (player == null || items == null)
            return;
        leftOvers = player.getInventory().addItem(items);
        if (leftOvers.isEmpty())
            return;
        for (final ItemStack itemStack : leftOvers.values())
            player.getWorld().dropItem(player.getLocation(), itemStack);
    }

}
