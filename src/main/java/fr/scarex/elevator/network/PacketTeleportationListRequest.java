package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class PacketTeleportationListRequest implements IMessage
{
    private int x;
    private int y;
    private int z;

    public PacketTeleportationListRequest() {}

    public PacketTeleportationListRequest(int x, int y, int z) {
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

    public static class Handler implements IMessageHandler<PacketTeleportationListRequest, PacketTeleportationList>
    {
        @Override
        public PacketTeleportationList onMessage(PacketTeleportationListRequest message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.getEntityWorld();
            TileEntityElevatorController controller = null;
            if (world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityElevatorController)
                controller = (TileEntityElevatorController) world.getTileEntity(message.x, message.y, message.z);
            else if (world.getTileEntity(message.x, message.y, message.z) instanceof ITeleportationEntry) controller = ((ITeleportationEntry) world.getTileEntity(message.x, message.y, message.z)).getController();

            return new PacketTeleportationList(controller.getTeleportationList(ctx.getServerHandler().playerEntity));
        }
    }
}
