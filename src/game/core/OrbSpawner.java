package game.core;

import entity.Brick;
import entity.ManaOrb;

public interface OrbSpawner {
    ManaOrb trySpawn(Brick brick);
}
