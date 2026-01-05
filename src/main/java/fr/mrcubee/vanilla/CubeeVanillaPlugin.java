package fr.mrcubee.vanilla;

import fr.mrcubee.vanilla.miner.Miner;
import fr.mrcubee.vanilla.timber.Timber;
import org.bukkit.plugin.java.JavaPlugin;

public class CubeeVanillaPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        Timber.enable(this);
        Miner.enable(this);
    }

    @Override
    public void onDisable() {
        Timber.disable();
        Miner.disable();
    }
}
