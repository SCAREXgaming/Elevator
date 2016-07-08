package fr.scarex.elevator.block;

import cofh.thermalexpansion.block.simple.BlockFrame;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author SCAREX
 *
 */
public class ElevatorBlock extends AbstractBlockDismantleable
{
    public IIcon[] icons = new IIcon[8];

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
    public void registerCrafts() {
        GameRegistry.addRecipe(new ShapedOreRecipe(this, "XYX", "YZY", "XYX", 'X', Items.ender_pearl, 'Y', "dustRedstone", 'Z', BlockFrame.frameMachineResonant));
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

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1) != null && Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()) != Blocks.air)
            return Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()).getIcon(side, ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItemDamage());
        else if (world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).getController() != null)
            return this.icons[4 + (side == 4 || side == 5 ? 2 : (side == 3 || side == 2 ? 3 : side))];
        else
            return this.getIcon(side, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side == 4 || side == 5 ? 2 : (side == 3 || side == 2 ? 3 : side)];
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote && entity instanceof EntityPlayer && world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).isAutoOpen() && ((IAccessible) world.getTileEntity(x, y, z)).canAccess((EntityPlayer) entity)) ((EntityPlayer) entity).openGui(Elevator.INSTANCE, 0, world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_bottom_off");
        this.icons[1] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_top_off");
        this.icons[2] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_off");
        this.icons[3] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_off_nowindow");
        this.icons[4] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_bottom_on");
        this.icons[5] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_top_on");
        this.icons[6] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_on");
        this.icons[7] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_on_nowindow");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevator && ((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1) != null && Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()) != Blocks.air)
            return Block.getBlockFromItem(((TileEntityElevator) world.getTileEntity(x, y, z)).getStackInSlot(1).getItem()).colorMultiplier(world, x, y, z);
        else
            return super.colorMultiplier(world, x, y, z);
    }
}
