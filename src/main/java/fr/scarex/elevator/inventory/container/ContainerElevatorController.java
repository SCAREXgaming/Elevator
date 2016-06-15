package fr.scarex.elevator.inventory.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.scarex.elevator.client.inventory.GuiElevatorController;
import fr.scarex.elevator.tileentity.IWhitelist;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author SCAREX
 *
 */
public class ContainerElevatorController extends Container implements IWhitelistContainer
{
    protected TileEntityElevatorController tile;
    protected boolean fromController;

    public ContainerElevatorController(TileEntityElevatorController tile, boolean fromController) {
        this.tile = tile;
        this.fromController = fromController;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return null;
    }

    @Override
    public IWhitelist getTileEntityWhitelist() {
        return this.tile;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (this.fromController) {
            for (int i = 0; i < this.crafters.size(); i++) {
                ((ICrafting) this.crafters.get(i)).sendProgressBarUpdate(this, 0, ((TileEntityElevatorController) this.tile).getEnergyStored(ForgeDirection.UNKNOWN));
                ((ICrafting) this.crafters.get(i)).sendProgressBarUpdate(this, 1, ((TileEntityElevatorController) this.tile).getMaxEnergyStored(ForgeDirection.UNKNOWN));
            }
        }
    }

    @Override
    public void updateProgressBar(int slot, int value) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiElevatorController) {
            switch (slot) {
            case 0:
                ((GuiElevatorController) Minecraft.getMinecraft().currentScreen).updateEnergyStored(value);
                break;
            case 1:
                ((GuiElevatorController) Minecraft.getMinecraft().currentScreen).updateMaxEnergyStored(value);
                break;
            }
        }
    }
}
