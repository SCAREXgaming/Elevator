package fr.scarex.elevator.block;

import java.util.ArrayList;

import cofh.api.block.IDismantleable;
import cofh.core.util.CoreUtils;
import fr.scarex.elevator.tileentity.AbstractElevatorTileEntity;
import fr.scarex.elevator.tileentity.IDropInventory;
import fr.scarex.elevator.tileentity.IOwneable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public abstract class AbstractBlockDismantleable extends AbstractElevatorBlock implements IDismantleable
{
    protected AbstractBlockDismantleable(Material m) {
        super(m);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer p, World world, int x, int y, int z, boolean arg5) {
        return dismantleBlock(p, getItemStackTag(world, x, y, z), world, x, y, z, arg5, false);
    }

    public NBTTagCompound getItemStackTag(World world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof AbstractElevatorTileEntity) {
            NBTTagCompound comp = new NBTTagCompound();
            ((AbstractElevatorTileEntity) world.getTileEntity(x, y, z)).writeExtraCompound(comp);
            return comp;
        }
        return null;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return dismantleBlock(null, getItemStackTag(world, x, y, z), world, x, y, z, false, true);
    }

    public ArrayList<ItemStack> dismantleBlock(EntityPlayer paramEntityPlayer, NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2) {
        TileEntity localTileEntity = paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
        int i = paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3);

        ItemStack localItemStack = new ItemStack(this, 1, i);
        if (paramNBTTagCompound != null) {
            localItemStack.setTagCompound(paramNBTTagCompound);
        }
        if (!paramBoolean2) {
            paramWorld.setBlockToAir(paramInt1, paramInt2, paramInt3);
            if (!paramBoolean1) {
                float f = 0.3F;
                double d1 = paramWorld.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                double d2 = paramWorld.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                double d3 = paramWorld.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                EntityItem localEntityItem = new EntityItem(paramWorld, paramInt1 + d1, paramInt2 + d2, paramInt3 + d3, localItemStack);
                localEntityItem.delayBeforeCanPickup = 10;
                paramWorld.spawnEntityInWorld(localEntityItem);
                if (paramEntityPlayer != null) CoreUtils.dismantleLog(paramEntityPlayer.getCommandSenderName(), this, i, paramInt1, paramInt2, paramInt3);
            }
        }
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(localItemStack);
        return localArrayList;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if (tileentity instanceof IInventory && (tileentity instanceof IDropInventory ? ((IDropInventory) tileentity).shouldDropItems() : true)) {
            IInventory inv = (IInventory) tileentity;
            for (int i1 = 0; i1 < inv.getSizeInventory(); ++i1) {
                ItemStack itemstack = inv.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem)) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                    }
                }
            }
            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        Item item = Item.getItemFromBlock(this);
        if (item == null) return null;
        int i = world.getBlockMetadata(x, y, z);
        ItemStack stack = new ItemStack(item, 1, i);
        stack.setTagCompound(getItemStackTag(world, x, y, z));

        return stack;
    }

    @Override
    public boolean canDismantle(EntityPlayer p, World world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) instanceof IOwneable ? ((IOwneable) world.getTileEntity(x, y, z)).isOwner(p.getUniqueID()) : true;
    }
}
