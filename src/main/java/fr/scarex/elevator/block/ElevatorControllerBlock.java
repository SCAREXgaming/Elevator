package fr.scarex.elevator.block;

import cofh.thermalexpansion.block.simple.BlockFrame;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.tileentity.IOwneable;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author SCAREX
 *
 */
public class ElevatorControllerBlock extends AbstractElevatorEnergyBlock
{
    public IIcon[] icons = new IIcon[5];

    protected ElevatorControllerBlock() {
        super(Material.rock);
    }

    @Override
    public String getName() {
        return "ElevatorBlockController";
    }

    @Override
    public void registerCrafts() {
        GameRegistry.addRecipe(new ShapedOreRecipe(this, "XYX", "YZY", "XYX", 'X', "ingotEnderium", 'Y', "dustRedstone", 'Z', BlockFrame.frameMachineResonant));
    }

    @Override
    public Class getTileEntityClass() {
        return TileEntityElevatorController.class;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityElevatorController();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevatorController) ((TileEntityElevatorController) world.getTileEntity(x, y, z)).setOwner(player.getUniqueID());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem() == null && ((IOwneable) world.getTileEntity(x, y, z)).isOwner(player.getUniqueID())) {
            player.openGui(Elevator.INSTANCE, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityElevatorController && ((TileEntityElevatorController) world.getTileEntity(x, y, z)).getEnergyStored(ForgeDirection.getOrientation(side)) > 0)
            return this.icons[side == 0 ? 0 : (side == 1 ? 2 : 4)];
        else
            return this.getIcon(side, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side == 0 || side == 1 ? side : 3];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_bottom");
        this.icons[1] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_top_off");
        this.icons[2] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_top_on");
        this.icons[3] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_off");
        this.icons[4] = reg.registerIcon(Elevator.MODID + ":" + this.getName() + "_side_on");
    }
}
