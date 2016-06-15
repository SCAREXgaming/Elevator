package fr.scarex.elevator.client.inventory.scrollinglist;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.GuiScrollingList;
import fr.scarex.elevator.client.inventory.GuiPlayerList;
import net.minecraft.client.renderer.Tessellator;

/**
 * @author SCAREX
 *
 */
public class GuiScrollingListPlayerList extends GuiScrollingList
{
    protected final GuiPlayerList parent;
    protected List<String> playerList = new ArrayList<String>();
    public int elementSelected = -1;

    public GuiScrollingListPlayerList(int width, GuiPlayerList parent, int top, int left) {
        super(parent.mc, width, parent.height, top, parent.height - 120, left, 35);
        this.parent = parent;
    }

    public void updateList(List<String> list) {
        this.playerList = list;
    }

    @Override
    protected int getSize() {
        return playerList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.elementSelected = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return index == elementSelected;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int index, int var2, int var3, int var4, Tessellator tess) {
        this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(this.playerList.get(index), listWidth - 40), this.left + 40, var3 + 3, 0xFFFFFF);
    }
}
