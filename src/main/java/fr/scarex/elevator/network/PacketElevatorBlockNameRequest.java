package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.IOwneable;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockNameRequest implements IMessage
{
    private int x;
    private int y;
    private int z;

    public PacketElevatorBlockNameRequest() {}

    public PacketElevatorBlockNameRequest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static class Handler implements IMessageHandler<PacketElevatorBlockNameRequest, PacketElevatorBlockNameToPlayer>
    {
        @Override
        public PacketElevatorBlockNameToPlayer onMessage(PacketElevatorBlockNameRequest message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.getEntityWorld();
            if (world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityElevator && ((IOwneable) world.getTileEntity(message.x, message.y, message.z)).isOwner(ctx.getServerHandler().playerEntity.getUniqueID())) return new PacketElevatorBlockNameToPlayer(((TileEntityElevator) world.getTileEntity(message.x, message.y, message.z)).getName());
            return null;
        }
    }
}
