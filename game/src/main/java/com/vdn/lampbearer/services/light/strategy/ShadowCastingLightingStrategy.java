package com.vdn.lampbearer.services.light.strategy;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.Light;
import com.vdn.lampbearer.utils.PositionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ShadowCastingLightingStrategy implements LightingStrategy {

    private final Size worldSize;
    private final Map<Position3D, GameBlock> blocks;

    private HashMap<Position, TileColor> lightMap;
    private Light light;


    /**
     * {@inheritDoc}
     */
    public HashMap<Position, TileColor> lightBlocks(Light light) {

        this.lightMap = new HashMap<>();
        this.light = light;

        lightMap.put(
                light.getPosition(),
                light.getColorOfTile(
                        blocks.get(light.getPosition().toPosition3D(0)),
                        light.getPosition()
                )
        );

        for (PositionUtils.Octant octant : light.getDirection().getOctants()) {
            switch (octant) {
                case FIRST:
                    castLight(1, 1.0f, 0.0f, 0, -1, 1, 0);
                    break;
                case SECOND:
                    castLight(1, 1.0f, 0.0f, -1, 0, 0, 1);
                    break;
                case THIRD:
                    castLight(1, 1.0f, 0.0f, 1, 0, 0, 1);
                    break;
                case FOURTH:
                    castLight(1, 1.0f, 0.0f, 0, 1, 1, 0);
                    break;
                case FIFTH:
                    castLight(1, 1.0f, 0.0f, 0, 1, -1, 0);
                    break;
                case SIXTH:
                    castLight(1, 1.0f, 0.0f, 1, 0, 0, -1);
                    break;
                case SEVENTH:
                    castLight(1, 1.0f, 0.0f, -1, 0, 0, -1);
                    break;
                case EIGHTH:
                    castLight(1, 1.0f, 0.0f, 0, -1, -1, 0);
                    break;
            }
        }

        return lightMap;
    }


    private void castLight(int row, float start, float end, int xx, int xy, int yx, int yy) {
        if (start < end) return;

        float newStart = 0.0f;

        boolean blocked = false;
        var currentPos = Position.create(0, 0);
        for (int distance = row; distance <= light.getRadius() && !blocked; distance++) {
            int deltaY = -distance;
            for (int deltaX = -distance; deltaX <= 0; deltaX++) {
                currentPos = currentPos
                        .withX(light.getPosition().getX() + deltaX * xx + deltaY * xy)
                        .withY(light.getPosition().getY() + deltaX * yx + deltaY * yy);

                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);

                if (!(worldSize.containsPosition(currentPos)) || start < rightSlope) {
                    continue;
                } else if (end > leftSlope) {
                    break;
                }

                //check if it's within the lightable area and light if needed
                if (getDistance(currentPos) <= light.getRadius()) {
                    lightMap.put(
                            currentPos,
                            light.getColorOfTile(
                                    blocks.get(currentPos.toPosition3D(0)),
                                    currentPos
                            )
                    );
                }

                if (blocked) { //previous cell was a blocking one
                    if (isOpaque(currentPos)) {//hit a wall
                        newStart = rightSlope;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (isOpaque(currentPos) && distance < light.getRadius()) {
                        //hit a wall within sight line
                        blocked = true;
                        castLight(distance + 1, start, leftSlope, xx, xy, yx, yy);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }


    private boolean isOpaque(Position pos) {
        return !blocks.get(pos.toPosition3D(0)).isTransparent();
    }


    private double getDistance(Position pos) {
        return PositionUtils.getDistance(light.getPosition(), pos);
    }
}
