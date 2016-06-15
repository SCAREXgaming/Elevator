package fr.scarex.elevator.client;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.scarex.elevator.CommonProxy;
import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.block.AbstractElevatorBlock;
import fr.scarex.elevator.block.ElevatorBlock;
import fr.scarex.elevator.client.render.block.AbstractTESR;
import fr.scarex.elevator.network.PacketElevatorBlockHotKeysPressed;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

/**
 * @author SCAREX
 *
 */
public class ClientProxy extends CommonProxy implements ISimpleBlockRenderingHandler
{
    public static int renderId;

    @Override
    public void registerRender() {
        this.renderId = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void registerHandlers() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        AbstractTESR tesr = getAbastractTESRForBlock((AbstractElevatorBlock) block);
        if (tesr != null) tesr.renderInventoryBlock(block, metadata, modelId, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        AbstractTESR tesr = getAbastractTESRForBlock((AbstractElevatorBlock) block);
        return tesr != null ? tesr.renderWorldBlock(world, x, y, z, block, modelId, renderer) : false;
    }

    public static AbstractTESR getAbastractTESRForBlock(AbstractElevatorBlock block) {
        return (AbstractTESR) TileEntityRendererDispatcher.instance.mapSpecialRenderers.get(((AbstractElevatorBlock) block).getTileEntityClass());
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }

    public ArrayList<Integer> keyPressedCache = new ArrayList<Integer>();
    public boolean checkKeys = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (Minecraft.getMinecraft().thePlayer != null) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                int x = MathHelper.floor_double(p.posX);
                int y = MathHelper.floor_double(p.posY - 0.20000000298023224D - (double) p.yOffset);
                int z = MathHelper.floor_double(p.posZ);
                Block block = p.getEntityWorld().getBlock(x, y, z);
                if (block instanceof ElevatorBlock) {
                    this.checkKeys = true;
                    this.keyPressedCache.ensureCapacity(Keyboard.getNumKeyboardEvents());
                }
            }
        } else {
            if (!this.keyPressedCache.isEmpty()) Elevator.NETWORK.sendToServer(new PacketElevatorBlockHotKeysPressed((Integer[]) this.keyPressedCache.toArray(new Integer[0])));
            this.checkKeys = false;
            this.keyPressedCache.clear();
        }
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (this.checkKeys && !Keyboard.getEventKeyState()) this.keyPressedCache.add(Keyboard.getEventKey());
    }
}
