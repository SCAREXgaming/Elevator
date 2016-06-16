package fr.scarex.elevator.inventory.container;

import fr.scarex.elevator.inventory.container.slot.AbstractSlotFilter;
import fr.scarex.elevator.inventory.container.slot.SlotFilter;
import fr.scarex.elevator.inventory.container.slot.SlotSolidBlock;
import fr.scarex.elevator.tileentity.IWhitelist;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author SCAREX
 *
 */
public class ContainerElevatorBlock extends Container implements IWhitelistContainer
{
    public final TileEntityElevator elevator;
    public final InventoryPlayer playerInv;

    public ContainerElevatorBlock(TileEntityElevator elevator, InventoryPlayer playerInv) {
        this.elevator = elevator;
        this.playerInv = playerInv;

        this.elevator.openInventory();

        this.bindTileSlots();
        this.bindPlayerSlots();
    }

    private void bindPlayerSlots() {
        byte i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.playerInv, j + i * 9 + 9, 8 + j * 18, 94 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.playerInv, i, 8 + i * 18, 152));
        }
    }

    private void bindTileSlots() {
        this.addSlotToContainer(new SlotFilter(this.elevator, 0, 13, 13));
        this.addSlotToContainer(new SlotSolidBlock(this.elevator, 1, 13, 41));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.elevator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = this.getSlot(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index > (this.elevator.getSizeInventory() - 1)) {
                if (this.mergeItemStack(itemstack, 0, 1, false))
                    return itemstack1;
                else if (this.mergeItemStack(itemstack, 1, 2, false))
                    return itemstack1;
                else
                    return null;
            } else {
                if (!this.mergeItemStack(itemstack1, this.elevator.getSizeInventory(), this.inventorySlots.size(), true)) return itemstack;
            }

            if (itemstack1.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();

            if (itemstack1.stackSize == itemstack.stackSize) return null;

            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.elevator.closeInventory();
    }

    @Override
    public ItemStack slotClick(int slot, int data, int action, EntityPlayer player) {
        if (slot >= 0 && slot < this.inventorySlots.size() && this.getSlot(slot) instanceof AbstractSlotFilter && ((AbstractSlotFilter) this.getSlot(slot)).deleteSlotOnClick(data, action, player)) {
            if (player.inventory.getItemStack() != null && this.getSlot(slot).isItemValid(player.inventory.getItemStack()))
                this.getSlot(slot).putStack(player.inventory.getItemStack());
            else
                this.getSlot(slot).putStack(null);
            return null;
        }
        return super.slotClick(slot, data, action, player);
    }

    @Override
    public IWhitelist getTileEntityWhitelist() {
        return this.elevator;
    }
}
