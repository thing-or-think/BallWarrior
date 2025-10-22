package game.core;

import entity.Brick;
import entity.ManaOrb;

public class DefaultOrbSpawner implements OrbSpawner {

    private final double spawnChance;
    private final int manaAmount;

    public DefaultOrbSpawner(double spawnChance, int manaAmount) {
        this.spawnChance = spawnChance;
        this.manaAmount = manaAmount;
    }

    public DefaultOrbSpawner() {
        this(0.3, 10);
    }

    @Override
    public ManaOrb trySpawn(Brick brick) {
        if (brick == null || !brick.isDestroyed()) return null;

        if (Math.random() <= spawnChance) {
            return new ManaOrb((int) brick.getX(), (int) brick.getY(), manaAmount);
        }

        return null;
    }
}
