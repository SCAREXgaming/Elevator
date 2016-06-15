package fr.scarex.elevator.waila;

import java.util.List;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.block.AbstractElevatorBlock;
import fr.scarex.elevator.tileentity.AbstractElevatorTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class ElevatorWailaCompat implements IWailaDataProvider
{
    public static final ElevatorWailaCompat INSTANCE = new ElevatorWailaCompat();

    public static void load(IWailaRegistrar registrar) {
        registrar.registerStackProvider(INSTANCE, AbstractElevatorBlock.class);
        registrar.registerHeadProvider(INSTANCE, AbstractElevatorBlock.class);
        registrar.registerBodyProvider(INSTANCE, AbstractElevatorBlock.class);
        registrar.registerTailProvider(INSTANCE, AbstractElevatorBlock.class);
        registrar.registerNBTProvider(INSTANCE, AbstractElevatorBlock.class);

        Elevator.LOG.info("Waila compatibility loaded");
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof AbstractElevatorTileEntity) return ((AbstractElevatorTileEntity) accessor.getTileEntity()).getWailaStack(accessor, config);
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof AbstractElevatorTileEntity) currenttip = ((AbstractElevatorTileEntity) accessor.getTileEntity()).getWailaHead(itemStack, currenttip, accessor, config);
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof AbstractElevatorTileEntity) currenttip = ((AbstractElevatorTileEntity) accessor.getTileEntity()).getWailaBody(itemStack, currenttip, accessor, config);
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof AbstractElevatorTileEntity) currenttip = ((AbstractElevatorTileEntity) accessor.getTileEntity()).getWailaTail(itemStack, currenttip, accessor, config);
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (te instanceof AbstractElevatorTileEntity) tag = ((AbstractElevatorTileEntity) te).getWailaNBTData(player, te, tag, world, x, y, z);
        return tag;
    }
}
