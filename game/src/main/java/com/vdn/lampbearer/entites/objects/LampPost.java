package com.vdn.lampbearer.entites.objects;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.services.light.Light;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.Getter;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

@Getter
public class LampPost extends AbstractEntity {

    private final Light light;


    public LampPost(Position3D position) {
        setName("Lamp post");
        setTile(TileRepository.getTile(BlockTypes.LAMPPOST));
        setPosition(position);
        light = new CircleLight(position, 14, TileColor.fromString("#ffba37"));
    }
}
