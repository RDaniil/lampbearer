package com.vdn.lampbearer.views;

public enum BlockType {
    PLAYER,
    GROUND,
    GROUND_TRAIL,
    GRASS,
    GRASS_SMALL,
    GRASS_TRAIL,
    ROCK,
    SIMPLE_ZOMBIE,
    H_CLOSED_DOOR,
    V_CLOSED_DOOR,
    OPENED_DOOR,
    UR_CORNER,
    BR_CORNER,
    UL_CORNER,
    BL_CORNER,
    H_WALL,
    V_WALL,
    H_WALL_THICK,
    V_WALL_THICK,
    FIRST_AID_KIT,
    STONE,
    H_GLASS,
    V_GLASS,
    UNSEEN,
    EMPTY,
    H_WALL_THICK_B_JOINT,
    H_WALL_THICK_T_JOINT,
    V_WALL_THICK_L_JOINT,
    V_WALL_THICK_R_JOINT,
    LAMPPOST,
    LANTERN,
    OIL_BOTTLE,
    TREE_LG,
    TREE_Y,
    TREE_G,
    TREE_R,
    SMALL_TREE_LG,
    SMALL_TREE_Y,
    SMALL_TREE_G,
    SMALL_TREE_R,
    PINE,
    BUSH_Y,
    BUSH_LG,
    ;


    public static boolean isTrail(BlockType type) {
        return BlockType.GRASS_TRAIL.equals(type) || BlockType.GROUND_TRAIL.equals(type);
    }
}
