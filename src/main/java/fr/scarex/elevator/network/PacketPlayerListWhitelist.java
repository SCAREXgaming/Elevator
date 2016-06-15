package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiPlayerList;
import fr.scarex.elevator.inventory.container.IWhitelistContainer;
import fr.scarex.elevator.tileentity.IOwneable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerListWhitelist implements IMessage
{
    private boolean value;

    public PacketPlayerListWhitelist() {}

    public PacketPlayerListWhitelist(boolean value) {
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

    public static class ClientHandler implements IMessageHandler<PacketPlayerListWhitelist, IMessage>
    {
        @Override
        public IMessage onMessage(PacketPlayerListWhitelist message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiPlayerList) ((GuiPlayerList) Minecraft.getMinecraft().currentScreen).updateWhitelist(message.value);
            return null;
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketPlayerListWhitelist, IMessage>
    {
        @Override
        public IMessage onMessage(PacketPlayerListWhitelist message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof IWhitelistContainer && (((IWhitelistContainer) player.openContainer).getTileEntityWhitelist() instanceof IOwneable ? ((IOwneable) ((IWhitelistContainer) player.openContainer).getTileEntityWhitelist()).isOwner(ctx.getServerHandler().playerEntity.getUniqueID()) : true)) ((IWhitelistContainer) player.openContainer).getTileEntityWhitelist().setWhitelist(message.value);
            return null;
        }
    }
}
