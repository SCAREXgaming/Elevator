package fr.scarex.elevator.tileentity;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public interface IAccessible
{
    public boolean canAccess(EntityPlayer player);
}
