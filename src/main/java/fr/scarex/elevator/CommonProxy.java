package fr.scarex.elevator;

import cpw.mods.fml.common.network.IGuiHandler;
import fr.scarex.elevator.client.inventory.GuiElevatorBlock;
import fr.scarex.elevator.client.inventory.GuiElevatorController;
import fr.scarex.elevator.client.inventory.GuiPlayerList;
import fr.scarex.elevator.inventory.container.ContainerElevatorBlock;
import fr.scarex.elevator.inventory.container.ContainerElevatorController;
import fr.scarex.elevator.inventory.container.ContainerUseless;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class CommonProxy implements IGuiHandler
{
    public void registerRender() {}
    
    public void registerHandlers() {}

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case 0:
            return new ContainerElevatorController(world.getTileEntity(x, y, z) instanceof TileEntityElevatorController ? (TileEntityElevatorController) world.getTileEntity(x, y, z) : ((ITeleportationEntry) world.getTileEntity(x, y, z)).getController(), world.getTileEntity(x, y, z) instanceof TileEntityElevatorController);
        case 1:
            return new ContainerElevatorBlock((TileEntityElevator) world.getTileEntity(x, y, z), player.inventory);
        }
        return new ContainerUseless();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case 0:
            return new GuiElevatorController(x, y, z, world.getTileEntity(x, y, z) instanceof TileEntityElevatorController);
        case 1:
            return new GuiElevatorBlock(player.inventory, (TileEntityElevator) world.getTileEntity(x, y, z));
        case 2:
            return new GuiPlayerList(x, y, z);
        }
        return null;
    }
}
