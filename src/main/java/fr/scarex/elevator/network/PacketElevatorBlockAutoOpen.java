package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiElevatorBlock;
import fr.scarex.elevator.inventory.container.ContainerElevatorBlock;
import fr.scarex.elevator.tileentity.IOwneable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockAutoOpen implements IMessage
{
    private boolean value;

    public PacketElevatorBlockAutoOpen() {}

    public PacketElevatorBlockAutoOpen(boolean value) {
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.value = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.value);
    }

    public static class ServerHandler implements IMessageHandler<PacketElevatorBlockAutoOpen, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockAutoOpen message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof ContainerElevatorBlock && ((IOwneable) ((ContainerElevatorBlock) player.openContainer).elevator).isOwner(ctx.getServerHandler().playerEntity.getUniqueID())) ((ContainerElevatorBlock) player.openContainer).elevator.setAutoOpen(message.value);
            return null;
        }
    }

    public static class ClientHandler implements IMessageHandler<PacketElevatorBlockAutoOpen, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockAutoOpen message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiElevatorBlock) ((GuiElevatorBlock) Minecraft.getMinecraft().currentScreen).updateAutoOpen(message.value);
            return null;
        }
    }
}
