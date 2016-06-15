package fr.scarex.elevator.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

/**
 * @author SCAREX
 *
 */
public abstract class AbstractTESR extends TileEntitySpecialRenderer
{
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float prt) {}
}
