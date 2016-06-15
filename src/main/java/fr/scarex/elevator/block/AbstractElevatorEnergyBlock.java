package fr.scarex.elevator.block;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.scarex.elevator.block.itemblock.ItemBlockEnergy;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

/**
 * @author SCAREX
 *
 */
public abstract class AbstractElevatorEnergyBlock extends AbstractBlockDismantleable
{
    protected AbstractElevatorEnergyBlock(Material m) {
        super(m);
        this.setHardness(15.0F);
        this.setResistance(25.0F);
    }

    @Override
    public Class<? extends ItemBlock> getItemBlock() {
        return ItemBlockEnergy.class;
    }
}
