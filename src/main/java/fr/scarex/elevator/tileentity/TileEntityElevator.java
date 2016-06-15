package fr.scarex.elevator.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

/**
 * @author SCAREX
 *
 */
public class TileEntityElevator extends AbstractElevatorTileEntity implements ITeleportationEntry, IDropInventory, IWhitelist, IOwneable, IAccessible
{
    protected UUID owner;
    protected String telName;
    private int numPlayersUsing;
    protected boolean autoOpen = true;
    protected boolean whitelist;
    protected List<UUID> playerList = new ArrayList<UUID>();
    protected int hotKey = 0;

    @Override
    public void readFromNBT(NBTTagCompound comp) {
        super.readFromNBT(comp);
        this.owner = UUID.fromString(comp.getString("OwnerUUID"));
        if (comp.hasKey("TelName", Constants.NBT.TAG_STRING)) this.telName = comp.getString("TelName");
        this.autoOpen = comp.getBoolean("AutoOpen");

        NBTTagList nbtList = comp.getTagList("Content", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtList.tagCount(); i++) {
            NBTTagCompound comp1 = nbtList.getCompoundTagAt(i);
            this.content[comp1.getByte("Slot")] = ItemStack.loadItemStackFromNBT(comp1);
        }

        this.whitelist = comp.getBoolean("Whitelist");
        NBTTagList nbtPlayerList = comp.getTagList("PlayerList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtPlayerList.tagCount(); i++) {
            NBTTagCompound compPlayer = nbtPlayerList.getCompoundTagAt(i);
            this.playerList.add(new UUID(compPlayer.getLong("UUIDMost"), compPlayer.getLong("UUIDLeast")));
        }

        this.hotKey = comp.getInteger("HotKey");
    }

    @Override
    public void writeToNBT(NBTTagCompound comp) {
        super.writeToNBT(comp);
        comp.setString("OwnerUUID", this.owner.toString());
        if (this.telName != null && !this.telName.isEmpty()) comp.setString("TelName", this.telName);
        comp.setBoolean("AutoOpen", this.autoOpen);

        NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < this.content.length; i++) {
            if (this.content[i] != null) {
                NBTTagCompound comp1 = new NBTTagCompound();
                comp1.setByte("Slot", (byte) i);
                this.content[i].writeToNBT(comp1);
                nbtList.appendTag(comp1);
            }
        }
        comp.setTag("Content", nbtList);

        comp.setBoolean("Whitelist", this.whitelist);
        NBTTagList nbtPlayerList = new NBTTagList();
        for (UUID uuid : this.playerList) {
            NBTTagCompound compPlayer = new NBTTagCompound();
            compPlayer.setLong("UUIDMost", uuid.getMostSignificantBits());
            compPlayer.setLong("UUIDLeast", uuid.getLeastSignificantBits());
            nbtPlayerList.appendTag(compPlayer);
        }
        comp.setTag("PlayerList", nbtPlayerList);

        if (this.hotKey != 0) comp.setInteger("HotKey", this.hotKey);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound comp = new NBTTagCompound();
        this.writeToNBT(comp);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, comp);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound comp = pkt.func_148857_g();
        this.readFromNBT(comp);
    }

    /**
     * @return the owner
     */
    public UUID getOwner() {
        return owner;
    }

    @Override
    public boolean isOwner(UUID uuid) {
        return this.owner.equals(uuid);
    }

    @Override
    public boolean canAccess(EntityPlayer player) {
        if (this.getController() == null) return false;
        if (!this.isUseableByPlayer(player)) return false;
        UUID u = player.getUniqueID();
        if (this.isOwner(u)) return true;
        if (this.getController().canAccess(player)) {
            for (UUID uuid : this.playerList) {
                if (uuid.equals(u)) return this.whitelist;
            }
            return !this.whitelist;
        }
        return false;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return this.telName != null && !this.telName.isEmpty() ? this.telName : "(" + this.xCoord + "," + this.yCoord + "," + this.zCoord + ")";
    }

    public void setName(String name) {
        this.telName = name;
    }

    @Override
    public ItemStack getItemStackIcon() {
        return this.content[0];
    }

    @Override
    public int getEnergyConsumed(EntityPlayer player, TileEntity teleporter) {
        return Math.abs(teleporter.yCoord - this.yCoord) * 10;
    }

    @Override
    public void teleportPlayer(EntityPlayer player) {
        player.setPositionAndUpdate(this.xCoord + .5D, this.yCoord + 1, this.zCoord + .5D);
    }

    @Override
    public TileEntityElevatorController getController() {
        for (int i = 0; i < 256; i++) {
            if (i != this.yCoord && this.worldObj.getTileEntity(this.xCoord, i, this.zCoord) instanceof TileEntityElevatorController) return (TileEntityElevatorController) this.worldObj.getTileEntity(this.xCoord, i, this.zCoord);
        }
        return null;
    }

    /**
     * @return the autoOpen
     */
    public boolean isAutoOpen() {
        return autoOpen;
    }

    /**
     * @param autoOpen
     *            the autoOpen to set
     */
    public void setAutoOpen(boolean autoOpen) {
        this.autoOpen = autoOpen;
    }

    /**
     * @return the whitelist
     */
    @Override
    public boolean isWhitelist() {
        return whitelist;
    }

    /**
     * @param whitelist
     *            the whitelist to set
     */
    @Override
    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * @return the playerList
     */
    @Override
    public List<UUID> getPlayerList() {
        return playerList;
    }

    @Override
    public void addPlayerToList(UUID uuid) {
        if (!this.playerList.contains(uuid)) this.playerList.add(uuid);
    }

    @Override
    public void removePlayer(int index) {
        this.playerList.remove(index);
    }

    @Override
    public void removePlayer(UUID uuid) {
        this.playerList.remove(uuid);
    }

    /**
     * @return the hotKey
     */
    public int getHotKey() {
        return hotKey;
    }

    /**
     * @param hotKey
     *            the hotKey to set
     */
    public void setHotKey(int hotKey) {
        this.hotKey = hotKey;
    }

    @Override
    public boolean doesHotKeysCorresponds(Integer[] keys) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].intValue() == this.getHotKey()) return true;
        }
        return false;
    }

    protected ItemStack[] content = new ItemStack[2];

    @Override
    public int getSizeInventory() {
        return content.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.content[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (this.getStackInSlot(index) != null) {
            ItemStack stack;
            if (this.getStackInSlot(index).stackSize <= amount) {
                stack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
            } else {
                stack = this.getStackInSlot(index).splitStack(amount);
                this.markDirty();
            }
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.content[index] = stack;
        this.markDirty();
        if (this.worldObj != null) this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public String getInventoryName() {
        return this.getName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
        if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;

        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }

    @Override
    public void closeInventory() {
        --this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldDropItems() {
        return false;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (this.getStackInSlot(1) != null) return this.getStackInSlot(1);
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public void writeExtraCompound(NBTTagCompound comp) {
        super.writeExtraCompound(comp);

        if (this.telName != null && !this.telName.isEmpty()) comp.setString("TelName", this.telName);
        comp.setBoolean("AutoOpen", this.autoOpen);

        NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < this.content.length; i++) {
            if (this.content[i] != null) {
                NBTTagCompound comp1 = new NBTTagCompound();
                comp1.setByte("Slot", (byte) i);
                this.content[i].writeToNBT(comp1);
                nbtList.appendTag(comp1);
            }
        }
        comp.setTag("Content", nbtList);

        comp.setBoolean("Whitelist", this.whitelist);
        NBTTagList nbtPlayerList = new NBTTagList();
        for (UUID uuid : this.playerList) {
            NBTTagCompound compPlayer = new NBTTagCompound();
            compPlayer.setLong("UUIDMost", uuid.getMostSignificantBits());
            compPlayer.setLong("UUIDLeast", uuid.getLeastSignificantBits());
            nbtPlayerList.appendTag(compPlayer);
        }
        comp.setTag("PlayerList", nbtPlayerList);

        if (this.hotKey != 0) comp.setInteger("HotKey", this.hotKey);
    }

    @Override
    public void readExtraCompound(NBTTagCompound comp) {
        super.readExtraCompound(comp);

        if (comp.hasKey("TelName", Constants.NBT.TAG_STRING)) this.telName = comp.getString("TelName");
        this.autoOpen = comp.getBoolean("AutoOpen");

        NBTTagList nbtList = comp.getTagList("Content", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtList.tagCount(); i++) {
            NBTTagCompound comp1 = nbtList.getCompoundTagAt(i);
            this.content[comp1.getByte("Slot")] = ItemStack.loadItemStackFromNBT(comp1);
        }

        this.whitelist = comp.getBoolean("Whitelist");
        NBTTagList nbtPlayerList = comp.getTagList("PlayerList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtPlayerList.tagCount(); i++) {
            NBTTagCompound compPlayer = nbtPlayerList.getCompoundTagAt(i);
            this.playerList.add(new UUID(compPlayer.getLong("UUIDMost"), compPlayer.getLong("UUIDLeast")));
        }

        this.hotKey = comp.getInteger("HotKey");
    }
}
