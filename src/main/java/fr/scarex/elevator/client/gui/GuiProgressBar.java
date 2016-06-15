package fr.scarex.elevator.client.gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class GuiProgressBar extends Gui
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int outlineWidth;
    protected int color;
    protected int outlineColor;
    protected int stringColor;
    protected String displayString;
    protected boolean visible = true;
    protected long maxSize;
    protected long currentSize;

    public GuiProgressBar(int x, int y, int width, int height, long size, int color) {
        this(x, y, width, height, 1, size, color, 0xCCCCCC, "", 0xFFFFFFFF);
    }

    public GuiProgressBar(int x, int y, int width, int height, int outlineWidth, long size, int color, int outlineColor, String displayString, int stringColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.outlineWidth = outlineWidth;
        this.maxSize = size;
        this.color = color;
        this.outlineColor = outlineColor;
        this.displayString = displayString;
        this.stringColor = stringColor;
    }

    public void drawBar(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            drawRect(this.x, this.y, this.x + this.width, this.y + this.outlineWidth, this.outlineColor);
            drawRect(this.x, this.y + this.height - this.outlineWidth, this.x + this.width, this.y + this.height, this.outlineColor);
            drawRect(this.x, this.y, this.x + this.outlineWidth, this.y + this.height, this.outlineColor);
            drawRect(this.x + this.width, this.y, this.x + this.width - this.outlineWidth, this.y + this.height, this.outlineColor);

            this.drawGradientRect(this.x + this.outlineWidth, this.y + this.outlineWidth, (int) Math.max((double) this.x + (this.currentSize * this.width) / (this.maxSize == 0 ? 1 : this.maxSize) - this.outlineWidth, this.x + this.outlineWidth), this.y + this.height - this.outlineWidth, color, new Color(color).darker().getRGB());

            if (this.displayString != null && !this.displayString.isEmpty()) this.drawCenteredString(mc.fontRenderer, mc.fontRenderer.trimStringToWidth(this.displayString, this.width - 4), this.x + this.width / 2, this.y + (this.height - 8) / 2, this.stringColor);

            if (this.isMousehover(mouseX, mouseY)) this.drawHoveringText(mc.currentScreen, Arrays.asList((this.isFinished() ? EnumChatFormatting.GREEN : EnumChatFormatting.WHITE).toString() + this.currentSize + " / " + this.maxSize), mouseX, mouseY, mc.fontRenderer);
        }
    }

    public boolean isMousehover(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    public void update(long l) {
        this.currentSize += l >= this.maxSize ? this.maxSize : l;
    }

    public void setSize(long l) {
        this.currentSize = l > this.maxSize ? this.maxSize : l;
    }

    public void clear() {
        this.maxSize = 1;
        this.currentSize = 1;
    }

    public void setMaxSize(long l) {
        this.maxSize = l;
    }

    public boolean isFinished() {
        return this.currentSize >= this.maxSize;
    }

    public void addMaxSize(long l) {
        this.maxSize += l;
    }

    protected void drawHoveringText(GuiScreen parent, List list, int x, int y, FontRenderer font) {
        if (!list.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 6;
            int k2 = y - 12;
            int i1 = 8;

            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }

            if (j2 + k > parent.width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > parent.height) {
                k2 = parent.height - i1 - 6;
            }

            this.zLevel = 1000.0F;
            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < list.size(); ++i2) {
                String s1 = (String) list.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
