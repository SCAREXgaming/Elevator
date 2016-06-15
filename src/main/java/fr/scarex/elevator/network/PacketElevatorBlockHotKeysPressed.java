package fr.scarex.elevator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class PacketElevatorBlockHotKeysPressed implements IMessage
{
    private Integer[] keys;

    public PacketElevatorBlockHotKeysPressed() {}

    public PacketElevatorBlockHotKeysPressed(Integer[] keys) {
        this.keys = keys;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        this.keys = new Integer[size];
        for (int i = 0; i < size; i++) {
            this.keys[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.keys.length);
        for (int i = 0; i < this.keys.length; i++) {
            buf.writeInt(this.keys[i]);
        }
    }

    public static class Handler implements IMessageHandler<PacketElevatorBlockHotKeysPressed, IMessage>
    {
        @Override
        public IMessage onMessage(PacketElevatorBlockHotKeysPressed message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            World world = ctx.getServerHandler().playerEntity.getEntityWorld();
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.posY - 0.20000000298023224D - (double) player.yOffset);
            int z = MathHelper.floor_double(player.posZ);
            TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);
            if (tile instanceof TileEntityElevator && ((TileEntityElevator) tile).canAccess(player)) ((TileEntityElevator) tile).getController().processPlayerKeys(player, tile, message.keys);
            return null;
        }
    }
}
