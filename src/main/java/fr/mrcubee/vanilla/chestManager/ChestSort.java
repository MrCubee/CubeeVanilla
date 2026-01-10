package fr.mrcubee.vanilla.chestManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChestSort implements Listener {

    @EventHandler
    public void onChestInteract(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ClickType click = event.getClick();
        if (click != ClickType.SHIFT_RIGHT && click != ClickType.SWAP_OFFHAND) return;

        Inventory chest = event.getView().getTopInventory();

        if (chest.getLocation() == null) return;

        event.setCancelled(true);

        if (click == ClickType.SHIFT_RIGHT) {

            List<ItemStack> items = new ArrayList<>();
            for (ItemStack item : chest.getContents()) {
                if (item != null && item.getType().isItem()) {
                    items.add(item.clone());
                }
            }

            items.sort(
                    Comparator.comparing(ItemStack::getType)
                            .thenComparingInt(ItemStack::getAmount)
            );

            chest.clear();
            for (ItemStack item : items) {
                chest.addItem(item);
            }

            player.sendMessage(ChatColor.GREEN + "✔ Coffre trié !");
            return;
        }

        Inventory playerInv = event.getView().getBottomInventory();

        for (int i = 0; i < playerInv.getSize(); i++) {

            ItemStack playerItem = playerInv.getItem(i);
            if (playerItem == null || playerItem.getType().isAir()) continue;

            // 🔍 Vérifie si l’item existe déjà dans le coffre
            boolean hasMatchInChest = false;
            for (ItemStack chestItem : chest.getContents()) {
                if (chestItem != null && chestItem.isSimilar(playerItem)) {
                    hasMatchInChest = true;
                    break;
                }
            }

            if (!hasMatchInChest) continue;

            int remaining = playerItem.getAmount();

            for (int j = 0; j < chest.getSize(); j++) {
                ItemStack chestItem = chest.getItem(j);
                if (chestItem == null) continue;
                if (!chestItem.isSimilar(playerItem)) continue;

                int space = chestItem.getMaxStackSize() - chestItem.getAmount();
                if (space <= 0) continue;

                int toMove = Math.min(space, remaining);
                chestItem.setAmount(chestItem.getAmount() + toMove);
                remaining -= toMove;

                if (remaining <= 0) break;
            }

            if (remaining > 0) {
                for (int j = 0; j < chest.getSize(); j++) {
                    if (chest.getItem(j) != null) continue;

                    ItemStack clone = playerItem.clone();
                    int toMove = Math.min(clone.getMaxStackSize(), remaining);
                    clone.setAmount(toMove);

                    chest.setItem(j, clone);
                    remaining -= toMove;

                    if (remaining <= 0) break;
                }
            }

            if (remaining <= 0) {
                playerInv.setItem(i, null);
            } else {
                playerItem.setAmount(remaining);
            }
        }

        player.updateInventory();
        player.sendMessage(ChatColor.GREEN + "✔ Items correspondants transférés !");
    }
}