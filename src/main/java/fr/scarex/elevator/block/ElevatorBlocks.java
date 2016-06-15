package fr.scarex.elevator.block;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author SCAREX
 *
 */
public final class ElevatorBlocks
{
    public static HashMap<Class, AbstractElevatorBlock> blockMap = new HashMap<Class, AbstractElevatorBlock>();

    public static void preInit() {
        addBlock(new ElevatorControllerBlock());
        addBlock(new ElevatorBlock());

        for (Entry<Class, AbstractElevatorBlock> e : blockMap.entrySet()) {
            e.getValue().init();
        }
    }

    public static void init() {
        for (Entry<Class, AbstractElevatorBlock> e : blockMap.entrySet()) {
            e.getValue().register();
        }
    }

    public static void postInit() {
        for (Entry<Class, AbstractElevatorBlock> e : blockMap.entrySet()) {
            e.getValue().registerCrafts();
        }
    }

    private static void addBlock(AbstractElevatorBlock b) {
        ElevatorBlocks.blockMap.put(b.getClass(), b);
    }
}
