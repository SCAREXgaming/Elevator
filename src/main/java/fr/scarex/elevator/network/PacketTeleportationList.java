package fr.scarex.elevator.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.client.inventory.GuiElevatorController;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import fr.scarex.elevator.tileentity.TeleportationEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

/**
 * @author SCAREX
 *
 */
public class PacketTeleportationList implements IMessage
{
    private List<ITeleportationEntry> telList;

    public PacketTeleportationList() {}

    public PacketTeleportationList(List<ITeleportationEntry> telList) {
        this.telList = telList;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int max = buf.readInt();
        this.telList = new ArrayList<ITeleportationEntry>(max);
        for (int i = 0; i < max; i++) {
            String name = ByteBufUtils.readUTF8String(buf);
            if (buf.readBoolean())
                this.telList.add(new TeleportationEntry(name, ByteBufUtils.readItemStack(buf)));
            else
                this.telList.add(new TeleportationEntry(name));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(telList.size());
        Iterator<ITeleportationEntry> ite = telList.iterator();
        while (ite.hasNext()) {
            ITeleportationEntry e = ite.next();
            ByteBufUtils.writeUTF8String(buf, e.getName());
            buf.writeBoolean(e.getItemStackIcon() != null);
            if (e.getItemStackIcon() != null) ByteBufUtils.writeItemStack(buf, e.getItemStackIcon());
        }
    }

    public static class Handler implements IMessageHandler<PacketTeleportationList, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTeleportationList message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiElevatorController) ((GuiElevatorController) Minecraft.getMinecraft().currentScreen).updateList(message.telList);
            return null;
        }
    }
}
