package fr.scarex.elevator.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author SCAREX
 *
 */
public abstract class AbstractElevatorTileEntityEnergy extends AbstractElevatorTileEntity implements IEnergyHandler
{
    protected final EnergyStorage storage;

    public AbstractElevatorTileEntityEnergy(int capacity, int input, int output) {
        this.storage = new EnergyStorage(capacity, input, output);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection d) {
        return true;
    }

    @Override
    public int extractEnergy(ForgeDirection d, int amount, boolean simulate) {
        return storage.extractEnergy(amount, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection arg0) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection arg0) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return storage.receiveEnergy(arg1, arg2);
    }

    @Override
    public void readFromNBT(NBTTagCompound comp) {
        super.readFromNBT(comp);
        this.storage.readFromNBT(comp);
    }

    @Override
    public void writeToNBT(NBTTagCompound comp) {
        super.writeToNBT(comp);
        this.storage.writeToNBT(comp);
    }

    @Override
    public void writeExtraCompound(NBTTagCompound comp) {
        this.storage.writeToNBT(comp);
    }

    @Override
    public void readExtraCompound(NBTTagCompound comp) {
        this.storage.readFromNBT(comp);
    }
}
