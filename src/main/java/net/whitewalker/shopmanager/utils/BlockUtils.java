package net.whitewalker.shopmanager.utils;


import org.bukkit.Material;

public class BlockUtils {

    public static boolean isItem(Material material) {
        switch (material) {
            case AIR:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
            case BED_BLOCK:
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case DOUBLE_STEP:
            case FIRE:
            case REDSTONE_WIRE:
            case CROPS:
            case BURNING_FURNACE:
            case SIGN_POST:
            case WOODEN_DOOR:
            case WALL_SIGN:
            case GLOWING_REDSTONE_ORE:
            case REDSTONE_TORCH_OFF:
            case SUGAR_CANE_BLOCK:
            case CAKE_BLOCK:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case NETHER_WARTS:
            case BREWING_STAND:
            case CAULDRON:
            case ENDER_PORTAL:
            case WOOD_DOUBLE_STEP:
            case COCOA:
            case TRIPWIRE:
            case FLOWER_POT:
            case CARROT:
            case POTATO:
            case SKULL:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case STANDING_BANNER:
            case WALL_BANNER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case PURPUR_DOUBLE_SLAB:
            case BEETROOT_BLOCK:
            case END_GATEWAY:
            case REDSTONE_LAMP_ON:
            case PORTAL:
            case DOUBLE_STONE_SLAB2:
            case FROSTED_ICE:
            case IRON_DOOR_BLOCK:

                // It's an item but want to filter it just to be safe
            case COMMAND:
                return false;
            default:
                return true;
        }
    }

}
