package fr.scarex.elevator.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * @author SCAREX
 *
 */
public class TeleportationEntry implements ITeleportationEntry
{
    public String name;
    public ItemStack icon;

    public TeleportationEntry(String name) {
        this.name = name;
    }

    public TeleportationEntry(String name, ItemStack icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack getItemStackIcon() {
        return this.icon;
    }

    @Override
    public int getEnergyConsumed(EntityPlayer player, TileEntity teleporter) {
        return 0;
    }

    @Override
    public void teleportPlayer(EntityPlayer player) {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public TileEntityElevatorController getController() {
        return null;
    }

    @Override
    public boolean doesHotKeysCorresponds(Integer[] keys) {
        return false;
    }
}
