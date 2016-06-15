package fr.scarex.elevator.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author SCAREX
 *
 */
public class GuiButtonKeyBinding extends GuiButton
{
    protected int defaultKey;
    protected int key;
    public boolean inModification = false;

    public GuiButtonKeyBinding(int index, int x, int y, int width, int height, int defaultKey, int key) {
        super(index, x, y, width, height, GameSettings.getKeyDisplayString(key));
        this.defaultKey = defaultKey;
        this.key = key;
    }

    public GuiButtonKeyBinding(int index, int x, int y, int defaultKey, int key) {
        super(index, x, y, GameSettings.getKeyDisplayString(key));
        this.defaultKey = defaultKey;
        this.key = key;
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (super.mousePressed(Minecraft.getMinecraft(), x, y)) {
            this.inModification = !this.inModification;
            this.updateDisplayName();
        }
    }

    public boolean mouseClicked(int x, int y, int click) {
        if (click == 1) {
            this.setKey(this.defaultKey);
            return true;
        }
        return false;
    }

    public boolean keyTyped(char c, int keyCode) {
        if (this.inModification) {
            this.inModification = !this.inModification;
            this.key = keyCode;
            this.updateDisplayName();
            return true;
        }
        return false;
    }

    public void updateDisplayName() {
        if (this.inModification)
            this.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + GameSettings.getKeyDisplayString(this.key) + EnumChatFormatting.WHITE + " <";
        else
            this.displayString = GameSettings.getKeyDisplayString(this.key);
    }
    
    public void setKey(int keyCode) {
        this.inModification = false;
        this.key = keyCode;
        this.updateDisplayName();
    }

    /**
     * @return the key
     */
    public int getKey() {
        return key;
    }
}
