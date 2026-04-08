package fr.mrcubee.vanilla.miner.listener;

import fr.mrcubee.vanilla.miner.Miner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void event(final BlockBreakEvent event) {
        if (Miner.executePlayer(event.getPlayer(), event.getBlock()))
            event.setCancelled(true);
    }

}
