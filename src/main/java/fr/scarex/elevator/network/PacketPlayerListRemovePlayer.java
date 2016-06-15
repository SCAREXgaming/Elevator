package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.IOwneable;
import fr.scarex.elevator.tileentity.IWhitelist;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerListRemovePlayer implements IMessage
{
    private int x;
    private int y;
    private int z;
    private int index;

    public PacketPlayerListRemovePlayer() {}

    public PacketPlayerListRemovePlayer(int x, int y, int z, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(index);
    }

    public static class Handler implements IMessageHandler<PacketPlayerListRemovePlayer, PacketPlayerList>
    {
        @Override
        public PacketPlayerList onMessage(PacketPlayerListRemovePlayer message, MessageContext ctx) {
            TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
            if (tile instanceof IWhitelist && (tile instanceof IOwneable ? ((IOwneable) tile).isOwner(ctx.getServerHandler().playerEntity.getUniqueID()) : true)) {
                ((IWhitelist) tile).removePlayer(message.index);
                return new PacketPlayerList(PacketPlayerList.uuidsToNames(((IWhitelist) tile).getPlayerList()));
            }
            return null;
        }
    }
}
