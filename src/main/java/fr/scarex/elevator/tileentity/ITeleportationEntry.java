package fr.scarex.elevator.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * @author SCAREX
 *
 */
public interface ITeleportationEntry
{
    public String getName();
    
    public ItemStack getItemStackIcon();
    
    public int getEnergyConsumed(EntityPlayer player, TileEntity teleporter);
    
    public void teleportPlayer(EntityPlayer player);
    
    public boolean isUseableByPlayer(EntityPlayer player);
    
    public TileEntityElevatorController getController();
    
    public boolean doesHotKeysCorresponds(Integer[] keys);
}
