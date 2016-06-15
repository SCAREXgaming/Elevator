package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.inventory.container.IWhitelistContainer;
import fr.scarex.elevator.tileentity.IWhitelist;
import io.netty.buffer.ByteBuf;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerListRequest implements IMessage
{
    public PacketPlayerListRequest() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketPlayerListRequest, PacketPlayerList>
    {
        @Override
        public PacketPlayerList onMessage(PacketPlayerListRequest message, MessageContext ctx) {
            if (ctx.getServerHandler().playerEntity.openContainer instanceof IWhitelistContainer) {
                IWhitelist tile = ((IWhitelistContainer) ctx.getServerHandler().playerEntity.openContainer).getTileEntityWhitelist();
                return new PacketPlayerList(PacketPlayerList.uuidsToNames(((IWhitelist) tile).getPlayerList()));
            }
            return null;
        }
    }
}
