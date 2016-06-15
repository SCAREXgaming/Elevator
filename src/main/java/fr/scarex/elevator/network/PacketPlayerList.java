package fr.scarex.elevator.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiPlayerList;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * @author SCAREX
 *
 */
public class PacketPlayerList implements IMessage
{
    protected List<String> names;

    public PacketPlayerList() {}

    public PacketPlayerList(List<String> names) {
        this.names = names;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        names = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            names.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(names.size());
        for (String s : names) {
            ByteBufUtils.writeUTF8String(buf, s);
        }
    }

    public static List<String> uuidsToNames(List<UUID> uuids) {
        List<String> names = new ArrayList<String>(uuids.size());
        for (UUID uuid : uuids) {
            names.add(MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid).getName());
        }
        return names;
    }

    public static class Handler implements IMessageHandler<PacketPlayerList, IMessage>
    {
        @Override
        public IMessage onMessage(PacketPlayerList message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiPlayerList) ((GuiPlayerList) Minecraft.getMinecraft().currentScreen).updateList(message.names);
            return null;
        }
    }
}
