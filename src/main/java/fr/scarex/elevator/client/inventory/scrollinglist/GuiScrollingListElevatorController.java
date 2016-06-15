package fr.scarex.elevator.client.inventory.scrollinglist;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.GuiScrollingList;
import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.client.inventory.GuiElevatorController;
import fr.scarex.elevator.network.PacketControllerTeleportation;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;

/**
 * @author SCAREX
 *
 */
public class GuiScrollingListElevatorController extends GuiScrollingList
{
    protected final GuiElevatorController parent;
    protected List<ITeleportationEntry> entries = new ArrayList<ITeleportationEntry>();

    public GuiScrollingListElevatorController(int width, GuiElevatorController parent, int top, int left) {
        super(parent.mc, width, parent.height, top, parent.height - 120, left, 35);
        this.parent = parent;
    }

    public void updateList(List<ITeleportationEntry> list) {
        this.entries = list;
    }

    @Override
    protected int getSize() {
        return entries != null ? entries.size() : 0;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        Elevator.NETWORK.sendToServer(new PacketControllerTeleportation(index, this.parent.tileX, this.parent.tileY, this.parent.tileZ));
    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int index, int var2, int var3, int var4, Tessellator tess) {
        RenderHelper.enableGUIStandardItemLighting();
        this.parent.getItemRenderer().renderItemAndEffectIntoGUI(this.parent.getFontRenderer(), this.parent.mc.getTextureManager(), this.entries.get(index).getItemStackIcon(), this.left + 12, var3);
        this.parent.getItemRenderer().renderItemOverlayIntoGUI(this.parent.getFontRenderer(), this.parent.mc.getTextureManager(), this.entries.get(index).getItemStackIcon(), this.left + 12, var3);
        this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(this.entries.get(index).getName(), listWidth - 40), this.left + 40, var3 + 3, 0xFFFFFF);
    }
}
