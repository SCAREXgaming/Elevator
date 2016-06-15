package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.inventory.container.ContainerElevatorBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockHotKeyRequest implements IMessage
{
    public PacketElevatorBlockHotKeyRequest() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketElevatorBlockHotKeyRequest, PacketElevatorBlockHotKey>
    {
        @Override
        public PacketElevatorBlockHotKey onMessage(PacketElevatorBlockHotKeyRequest message, MessageContext ctx) {
            EntityPlayer p = ctx.getServerHandler().playerEntity;
            if (p.openContainer instanceof ContainerElevatorBlock && ((ContainerElevatorBlock) p.openContainer).elevator.isOwner(p.getUniqueID())) return new PacketElevatorBlockHotKey(((ContainerElevatorBlock) p.openContainer).elevator.getHotKey());
            return null;
        }
    }
}
