package fr.scarex.elevator.tileentity;

import net.minecraft.inventory.IInventory;

/**
 * @author SCAREX
 *
 */
public interface IDropInventory extends IInventory
{
    public boolean shouldDropItems();
}
