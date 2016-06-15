package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.inventory.container.IWhitelistContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerListWhitelistRequest implements IMessage
{
    public PacketPlayerListWhitelistRequest() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketPlayerListWhitelistRequest, PacketPlayerListWhitelist>
    {
        @Override
        public PacketPlayerListWhitelist onMessage(PacketPlayerListWhitelistRequest message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof IWhitelistContainer) return new PacketPlayerListWhitelist(((IWhitelistContainer) player.openContainer).getTileEntityWhitelist().isWhitelist());
            return null;
        }
    }
}
