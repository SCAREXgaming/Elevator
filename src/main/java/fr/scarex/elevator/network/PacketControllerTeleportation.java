package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import fr.scarex.elevator.tileentity.TileEntityElevatorController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class PacketControllerTeleportation implements IMessage
{
    private int index;
    private int tileX;
    private int tileY;
    private int tileZ;

    public PacketControllerTeleportation() {}

    public PacketControllerTeleportation(int index, int tileX, int tileY, int tileZ) {
        this.index = index;
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileZ = tileZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.index = buf.readInt();
        this.tileX = buf.readInt();
        this.tileY = buf.readInt();
        this.tileZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(index);
        buf.writeInt(tileX);
        buf.writeInt(tileY);
        buf.writeInt(tileZ);
    }

    public static class Handler implements IMessageHandler<PacketControllerTeleportation, IMessage>
    {
        @Override
        public IMessage onMessage(PacketControllerTeleportation message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            World world = ctx.getServerHandler().playerEntity.getEntityWorld();
            TileEntity tile = world.getTileEntity(message.tileX, message.tileY, message.tileZ);
            if ((tile instanceof ITeleportationEntry && ((ITeleportationEntry) tile).isUseableByPlayer(player)) || (tile instanceof TileEntityElevatorController && ((TileEntityElevatorController) tile).isUseableByPlayer(player))) {
                if (tile instanceof TileEntityElevatorController)
                    ((TileEntityElevatorController) tile).teleportPlayer(player, message.index, tile);
                else if (tile instanceof TileEntityElevator && ((TileEntityElevator) tile).getController() != null) ((TileEntityElevator) tile).getController().teleportPlayer(player, message.index, tile);
                player.closeScreen();
            }
            return null;
        }
    }
}
