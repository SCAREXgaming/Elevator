package fr.scarex.elevator.inventory.container.slot;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author SCAREX
 *
 */
public class SlotSolidBlock extends AbstractSlotFilter
{
    public SlotSolidBlock(IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public void putStack(ItemStack stack) {
        if (stack != null) stack.stackSize = 1;
        super.putStack(stack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null ? Block.getBlockFromItem(stack.getItem()) != Blocks.air && Block.getBlockFromItem(stack.getItem()).isOpaqueCube() : false;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        this.inventory.setInventorySlotContents(this.slotNumber, null);
        return null;
    }
}
