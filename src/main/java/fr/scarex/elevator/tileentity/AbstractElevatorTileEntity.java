package fr.scarex.elevator.tileentity;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public abstract class AbstractElevatorTileEntity extends TileEntity
{
    public NBTTagCompound getWailaNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (this instanceof IInventory) {
            tag.setBoolean("HasCustomName", ((IInventory) this).hasCustomInventoryName());
            if (((IInventory) this).hasCustomInventoryName()) tag.setString("CustomName", ((IInventory) this).getInventoryName());
        }
        return tag;
    }
    
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().getBoolean("HasCustomName")) {
            currenttip.remove(0);
            currenttip.add(0, EnumChatFormatting.WHITE + "" + EnumChatFormatting.ITALIC + accessor.getNBTData().getString("CustomName"));
        }
        return currenttip;
    }

    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public void onNeighborBlockChange(Block block) {}

    public void writeExtraCompound(NBTTagCompound comp) {}

    public void readExtraCompound(NBTTagCompound comp) {}
}
