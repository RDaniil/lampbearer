package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.entites.interfaces.Updatable;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;

/**
 * Круглая вспышка света. Нужна чтобы создавать вспышки, например при выстреле из оружия
 */
public class CircleSparkLight extends CircleLight implements Updatable {

    private final int sparkDuration;
    private int sparkDurationLeft;


    public CircleSparkLight(Position position, int radius, TileColor color, int sparkDuration) {
        super(position, radius, color);
        this.sparkDuration = sparkDuration;
        this.sparkDurationLeft = sparkDuration;
    }


    public static void createNow(
            GameContext context, Position position, int radius, TileColor color,
            int sparkDuration) {
        var spark = new CircleSparkLight(position, radius, color, sparkDuration);
        context.getWorld().addToSchedule(spark);
    }


    @Override
    public int getTime() {
        return 0;
    }


    @Override
    public void update(GameContext context) {
        if (isFirstUpdate()) {
            context.getWorld().addStaticLight(this);
            context.getWorld().updateLighting();
        }
        if (sparkDurationLeft == 0) {
            context.getWorld().removeStaticLight(this);
            context.getWorld().removeFromSchedule(this);
            context.getWorld().updateLighting();
        }
        sparkDurationLeft--;
    }


    private boolean isFirstUpdate() {
        return sparkDuration == sparkDurationLeft;
    }


    @Override
    public boolean needUpdate() {
        return true;
    }
}
