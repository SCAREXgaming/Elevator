package fr.scarex.elevator.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.Constants;

/**
 * @author SCAREX
 *
 */
public class TileEntityElevatorController extends AbstractElevatorTileEntityEnergy implements IWhitelist, IOwneable, IAccessible
{
    protected UUID owner;
    public List<ITeleportationEntry> telEntries;
    protected boolean whitelist;
    protected List<UUID> playerList = new ArrayList<UUID>();

    public TileEntityElevatorController() {
        super(1000, 1000, 1000);
    }

    @Override
    public void readFromNBT(NBTTagCompound comp) {
        super.readFromNBT(comp);
        this.owner = UUID.fromString(comp.getString("OwnerUUID"));

        this.whitelist = comp.getBoolean("Whitelist");
        NBTTagList nbtPlayerList = comp.getTagList("PlayerList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtPlayerList.tagCount(); i++) {
            NBTTagCompound compPlayer = nbtPlayerList.getCompoundTagAt(i);
            this.playerList.add(new UUID(compPlayer.getLong("UUIDMost"), compPlayer.getLong("UUIDLeast")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound comp) {
        super.writeToNBT(comp);
        comp.setString("OwnerUUID", this.owner.toString());

        comp.setBoolean("Whitelist", this.whitelist);
        NBTTagList nbtPlayerList = new NBTTagList();
        for (UUID uuid : this.playerList) {
            NBTTagCompound compPlayer = new NBTTagCompound();
            compPlayer.setLong("UUIDMost", uuid.getMostSignificantBits());
            compPlayer.setLong("UUIDLeast", uuid.getLeastSignificantBits());
            nbtPlayerList.appendTag(compPlayer);
        }
        comp.setTag("PlayerList", nbtPlayerList);
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
        UUID u = player.getUniqueID();
        if (this.isOwner(u)) return true;
        for (UUID uuid : this.playerList) {
            if (uuid.equals(u)) return this.whitelist;
        }
        return !this.whitelist;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<ITeleportationEntry> getTeleportationList(EntityPlayer player) {
        if (this.worldObj.isRemote) return null;
        if (this.canAccess(player)) {
            ArrayList<ITeleportationEntry> list = new ArrayList<ITeleportationEntry>();
            for (int i = 0; i < 256; i++) {
                TileEntity tile = this.worldObj.getTileEntity(this.xCoord, i, this.zCoord);
                if (tile instanceof ITeleportationEntry && (tile instanceof IOwneable ? ((IOwneable) tile).isOwner(this.getOwner()) : true) && (tile instanceof IAccessible ? ((IAccessible) tile).canAccess(player) : true)) list.add((ITeleportationEntry) tile);
            }
            return list;
        }
        return null;
    }

    public void teleportPlayer(EntityPlayer player, int index, TileEntity teleporter) {
        ITeleportationEntry entry = this.getTeleportationList(player).get(index);
        int e;
        if (this.storage.getEnergyStored() >= (e = entry.getEnergyConsumed(player, teleporter))) {
            this.storage.extractEnergy(e, false);
            entry.teleportPlayer(player);
        } else {
            player.addChatMessage(new ChatComponentTranslation("elevator.chat.notEnoughEnergy"));
        }
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

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public void processPlayerKeys(EntityPlayer player, TileEntity teleporter, Integer[] keys) {
        List<ITeleportationEntry> entries = this.getTeleportationList(player);
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).doesHotKeysCorresponds(keys)) {
                this.teleportPlayer(player, i, teleporter);
                return;
            }
        }
    }

    @Override
    public void writeExtraCompound(NBTTagCompound comp) {
        super.writeExtraCompound(comp);

        comp.setBoolean("Whitelist", this.whitelist);
        NBTTagList nbtPlayerList = new NBTTagList();
        for (UUID uuid : this.playerList) {
            NBTTagCompound compPlayer = new NBTTagCompound();
            compPlayer.setLong("UUIDMost", uuid.getMostSignificantBits());
            compPlayer.setLong("UUIDLeast", uuid.getLeastSignificantBits());
            nbtPlayerList.appendTag(compPlayer);
        }
        comp.setTag("PlayerList", nbtPlayerList);
    }

    @Override
    public void readExtraCompound(NBTTagCompound comp) {
        super.readExtraCompound(comp);

        this.whitelist = comp.getBoolean("Whitelist");
        NBTTagList nbtPlayerList = comp.getTagList("PlayerList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtPlayerList.tagCount(); i++) {
            NBTTagCompound compPlayer = nbtPlayerList.getCompoundTagAt(i);
            this.playerList.add(new UUID(compPlayer.getLong("UUIDMost"), compPlayer.getLong("UUIDLeast")));
        }
    }
}
