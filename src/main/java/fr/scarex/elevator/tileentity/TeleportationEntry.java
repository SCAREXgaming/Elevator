package fr.scarex.elevator.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

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
    public int getDistance(TileEntityElevatorController controller) {
        return 0;
    }

    @Override
    public void teleportPlayer(EntityPlayerMP player) {}

    @Override
    public ItemStack getItemStackIcon() {
        return this.icon;
    }
}
