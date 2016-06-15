package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiElevatorBlock;
import fr.scarex.elevator.inventory.container.ContainerElevatorBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockHotKey implements IMessage
{
    private int hotKey;

    public PacketElevatorBlockHotKey() {}

    public PacketElevatorBlockHotKey(int hotKey) {
        this.hotKey = hotKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.hotKey = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.hotKey);
    }

    public static class ClientHandler implements IMessageHandler<PacketElevatorBlockHotKey, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockHotKey message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiElevatorBlock) ((GuiElevatorBlock) Minecraft.getMinecraft().currentScreen).updateHotKey(message.hotKey);
            return null;
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketElevatorBlockHotKey, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockHotKey message, MessageContext ctx) {
            EntityPlayer p = ctx.getServerHandler().playerEntity;
            if (p.openContainer instanceof ContainerElevatorBlock && ((ContainerElevatorBlock) p.openContainer).elevator.isOwner(p.getUniqueID())) ((ContainerElevatorBlock) p.openContainer).elevator.setHotKey(message.hotKey);
            return null;
        }
    }
}
