package fr.mrcubee.vanilla.task;

import org.bukkit.entity.Player;

public abstract class PlayerTask {

    protected final Player player;
    protected final int iterationsPerTick;

    public PlayerTask(final Player player, final int iterationsPerTick) {
        this.player = player;
        this.iterationsPerTick = iterationsPerTick;
    }

    public PlayerTask(final Player player) {
        this(player, 10);
    }

    protected void onStart() {

    }

    protected abstract boolean update();

    protected PlayerTask onComplete() {
        return null;
    }

    protected boolean tick() {
        for (int i = 0; i < this.iterationsPerTick; i++) {
            if (update())
                return true;
        }
        return false;
    }

}
