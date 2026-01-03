package fr.mrcubee.vanilla.timber.listener;

import fr.mrcubee.vanilla.timber.Timber;
import fr.mrcubee.vanilla.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void event(final PlayerQuitEvent event) {
        final ItemStack itemStack = Timber.cancelPlayer(event.getPlayer());

        if (itemStack != null)
            PlayerUtils.giveItem(event.getPlayer(), itemStack);
    }
}
