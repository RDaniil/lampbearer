package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.entites.behavior.npc.general.NonPlayerCharacterBehavior;
import org.hexworks.zircon.api.data.Position3D;

/**
 * An actor who is NPC
 */
public abstract class NonPlayerCharacter extends Actor<NonPlayerCharacterBehavior> {

    public NonPlayerCharacter(Position3D position) {
        super(position);
    }
}
