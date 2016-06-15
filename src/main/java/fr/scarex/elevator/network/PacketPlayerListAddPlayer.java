package fr.scarex.elevator.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.IWhitelist;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerListAddPlayer implements IMessage
{
    private int x;
    private int y;
    private int z;
    private String playerName;

    public PacketPlayerListAddPlayer() {}

    public PacketPlayerListAddPlayer(int x, int y, int z, String playerName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeUTF8String(buf, playerName);
    }

    public static class Handler implements IMessageHandler<PacketPlayerListAddPlayer, PacketPlayerList>
    {
        @Override
        public PacketPlayerList onMessage(PacketPlayerListAddPlayer message, MessageContext ctx) {
            GameProfile p;
            if ((p = MinecraftServer.getServer().func_152358_ax().func_152655_a(message.playerName)) != null) {
                TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
                if (tile instanceof IWhitelist) {
                    ((IWhitelist) tile).addPlayerToList(p.getId());
                    return new PacketPlayerList(PacketPlayerList.uuidsToNames(((IWhitelist) tile).getPlayerList()));
                }
            }
            return null;
        }
    }
}
