package fr.mrcubee.vanilla.miner.listener;

import fr.mrcubee.vanilla.miner.Miner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void event(final PlayerInteractEvent event) {
        if (Miner.executePosSet(event.getPlayer(), event.getClickedBlock(), event.getAction()))
            event.setCancelled(true);
    }

}
