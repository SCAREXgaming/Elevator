package fr.scarex.elevator.block;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.tileentity.IAccessible;
import fr.scarex.elevator.tileentity.IOwneable;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author SCAREX
 *
 */
public class ElevatorBlock extends AbstractBlockDismantleable
{
    public IIcon[] icons = new IIcon[2];

    protected ElevatorBlock() {
        super(Material.rock);
        this.setHardness(15.0F);
        this.setResistance(25.0F);
    }

    @Override
    public String getName() {
        return "ElevatorBlock";
    }

    @Override
    public Class getTileEntityClass() {
        return TileEntityElevator.class;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityElevator();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevator) ((TileEntityElevator) world.getTileEntity(x, y, z)).setOwner(player.getUniqueID());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking() && ((IOwneable) world.getTileEntity(x, y, z)).isOwner(player.getUniqueID())) {
                player.openGui(Elevator.INSTANCE, 1, world, x, y, z);
                return true;
            } else if (((IAccessible) world.getTileEntity(x, y, z)).canAccess(player)) {
                player.openGui(Elevator.INSTANCE, 0, world, x, y, z);
                return true;
            }
        }
        return false;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1) != null && Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()) != Blocks.air)
            return Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()).getIcon(side, ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItemDamage());
        else
            return this.icons[side == 1 ? 0 : 1];
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side == 1 ? 0 : 1];
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote && entity instanceof EntityPlayer && world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).isAutoOpen() && ((IAccessible) world.getTileEntity(x, y, z)).canAccess((EntityPlayer) entity)) ((EntityPlayer) entity).openGui(Elevator.INSTANCE, 0, world, x, y, z);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_up");
        this.icons[1] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side");
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1) != null && Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()) != Blocks.air)
            return Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()).colorMultiplier(world, x, y, z);
        else
            return super.colorMultiplier(world, x, y, z);
    }
}
