package fr.mrcubee.vanilla.timber.listener;

import fr.mrcubee.vanilla.timber.Timber;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void event(final BlockBreakEvent event) {
        if (Timber.executePlayer(event.getPlayer(), event.getBlock()))
            event.setCancelled(true);
    }
}
