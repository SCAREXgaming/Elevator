package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.ByteBufUtils;
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
public class PacketElevatorBlockNameToServer implements IMessage
{
    private String name;
    private int x;
    private int y;
    private int z;

    public PacketElevatorBlockNameToServer() {}

    public PacketElevatorBlockNameToServer(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements IMessageHandler<PacketElevatorBlockNameToServer, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockNameToServer message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.getEntityWorld();
            if (world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityElevator && ((IOwneable) world.getTileEntity(message.x, message.y, message.z)).isOwner(ctx.getServerHandler().playerEntity.getUniqueID())) ((TileEntityElevator) world.getTileEntity(message.x, message.y, message.z)).setName(message.name);
            return null;
        }
    }
}
