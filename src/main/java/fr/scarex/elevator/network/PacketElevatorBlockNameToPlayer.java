package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiElevatorBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockNameToPlayer implements IMessage
{
    private String name;

    public PacketElevatorBlockNameToPlayer() {}

    public PacketElevatorBlockNameToPlayer(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements IMessageHandler<PacketElevatorBlockNameToPlayer, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockNameToPlayer message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiElevatorBlock) ((GuiElevatorBlock) Minecraft.getMinecraft().currentScreen).updateName(message.name);
            return null;
        }
    }
}
