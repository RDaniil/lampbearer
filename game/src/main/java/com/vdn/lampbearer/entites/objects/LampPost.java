package com.vdn.lampbearer.entites.objects;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.services.light.Light;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

@Getter
public class LampPost extends AbstractEntity {

    @Setter(AccessLevel.NONE)
    private Light light;


    public LampPost(Position3D position) {
        super(position);

        BlockType type = BlockType.LAMPPOST;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setName(block.getName());
        setDescription(block.getDescription());
        setTile(TileRepository.getTile(type));
        light = new CircleLight(position, 14, TileColor.fromString("#ffba37"));
    }


    @Override
    public LampPost clone() {
        LampPost clone = (LampPost) super.clone();
        clone.light = light.clone();
        return clone;
    }
}
