package fr.scarex.elevator.block;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.tileentity.IOwneable;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class ElevatorControllerBlock extends AbstractElevatorEnergyBlock
{
    protected ElevatorControllerBlock() {
        super(Material.rock);
    }

    @Override
    public String getName() {
        return "ElevatorBlockController";
    }

    @Override
    public Class getTileEntityClass() {
        return TileEntityElevatorController.class;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityElevatorController();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevatorController) ((TileEntityElevatorController) world.getTileEntity(x, y, z)).setOwner(player.getUniqueID());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem() == null && ((IOwneable) world.getTileEntity(x, y, z)).isOwner(player.getUniqueID())) {
            player.openGui(Elevator.INSTANCE, 0, world, x, y, z);
            return true;
        }
        return false;
    }
}
