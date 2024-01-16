package com.vdn.lampbearer.services.light.strategy;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.Light;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.shape.EllipseFactory;
import org.hexworks.zircon.api.shape.EllipseParameters;
import org.hexworks.zircon.api.shape.LineFactory;
import org.hexworks.zircon.api.shape.Shape;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SimpleEllipseLightingStrategy implements LightingStrategy {

    private final Size worldSize;
    private final Map<Position3D, GameBlock> blocks;


    /**
     * {@inheritDoc}
     */
    public HashMap<Position, TileColor> lightBlocks(Light light) {

        var lightMap = new HashMap<Position, TileColor>();

        lightMap.put(light.getPosition(), light.getColorOfTile(
                blocks.get(light.getPosition().toPosition3D(0)),
                light.getPosition()));

        EllipseParameters ellipseParams = new EllipseParameters(light.getPosition(),
                Size.create(light.getRadius(), light.getRadius()));

        Shape visionCircle = EllipseFactory.INSTANCE.buildEllipse(ellipseParams);

        for (Position pointOnCircle : visionCircle.getPositions()) {
            Iterator<Position> lineOfSight = LineFactory.INSTANCE.buildLine(
                    light.getPosition(),
                    pointOnCircle).iterator();

            while (lineOfSight.hasNext()) {
                Position pos = lineOfSight.next();
                if (!worldSize.containsPosition(pos)) break;

                GameBlock block = blocks.get(pos.toPosition3D(0));
                lightMap.put(pos, light.getColorOfTile(block, pos));
                if (!block.isTransparent()) {
                    break;
                }
            }
        }

        return lightMap;
    }
}
