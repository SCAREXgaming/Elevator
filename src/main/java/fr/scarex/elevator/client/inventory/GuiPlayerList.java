package fr.scarex.elevator.client.inventory;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.client.gui.GuiButtonTrim;
import fr.scarex.elevator.client.inventory.scrollinglist.GuiScrollingListPlayerList;
import fr.scarex.elevator.inventory.container.ContainerUseless;
import fr.scarex.elevator.network.PacketPlayerListAddPlayer;
import fr.scarex.elevator.network.PacketPlayerListRemovePlayer;
import fr.scarex.elevator.network.PacketPlayerListRequest;
import fr.scarex.elevator.network.PacketPlayerListWhitelist;
import fr.scarex.elevator.network.PacketPlayerListWhitelistRequest;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * @author SCAREX
 *
 */
public class GuiPlayerList extends GuiContainer
{
    protected GuiScrollingListPlayerList playerList;
    protected GuiTextField playerName;
    protected GuiButtonTrim buttonAddPlayer;
    protected GuiButtonTrim buttonWhitelist;
    protected GuiButtonTrim buttonRemovePlayer;
    public boolean whitelist;
    public int tileX;
    public int tileY;
    public int tileZ;

    public GuiPlayerList(int tileX, int tileY, int tileZ) {
        super(new ContainerUseless());
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileZ = tileZ;

        this.xSize = 248;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        Elevator.NETWORK.sendToServer(new PacketPlayerListRequest());
        Elevator.NETWORK.sendToServer(new PacketPlayerListWhitelistRequest());
        Keyboard.enableRepeatEvents(true);
        super.initGui();

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.playerList = new GuiScrollingListPlayerList(200, this, this.guiTop - 10, this.guiLeft + 24);
        this.playerList.registerScrollButtons(this.buttonList, 10, 11);
        this.playerName = new GuiTextField(this.fontRendererObj, k + 24, l - 54, 120, 16);
        this.playerName.setFocused(true);
        this.buttonList.add(this.buttonAddPlayer = new GuiButtonTrim(0, k + 154, l - 56, 70, 20, Elevator.getTranslation("gui.button.addPlayer")));
        this.buttonList.add(this.buttonWhitelist = new GuiButtonTrim(1, k + 24, l - 34, 95, 20, ""));
        this.buttonList.add(this.buttonRemovePlayer = new GuiButtonTrim(2, k + 130, l - 34, 95, 20, Elevator.getTranslation("gui.button.removePlayer")));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float prt, int x, int y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(GuiElevatorController.BASE_TEXTURE);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2 - 64;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 160);
        this.drawTexturedModalRect(k, l + 160, 0, 32, this.xSize, this.ySize);

        this.playerName.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
        case 0:
            this.addPlayer();
            break;
        case 1:
            this.updateWhitelist(!this.whitelist);
            this.sendWhitelist();
            break;
        case 2:
            this.removePlayer();
            break;
        }
    }

    public void addPlayer() {
        Elevator.NETWORK.sendToServer(new PacketPlayerListAddPlayer(tileX, tileY, tileZ, this.playerName.getText()));
    }

    public void removePlayer() {
        if (this.playerList.elementSelected >= 0) Elevator.NETWORK.sendToServer(new PacketPlayerListRemovePlayer(tileX, tileY, tileZ, this.playerList.elementSelected));
        this.playerList.elementSelected = -1;
    }

    public void sendWhitelist() {
        Elevator.NETWORK.sendToServer(new PacketPlayerListWhitelist(this.whitelist));
    }

    @Override
    public void drawScreen(int x, int y, float prt) {
        super.drawScreen(x, y, prt);
        this.playerList.drawScreen(x, y, prt);
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    public void updateList(List<String> list) {
        this.playerList.updateList(list);
    }

    public void updateWhitelist(boolean value) {
        this.whitelist = value;
        this.buttonWhitelist.displayString = Elevator.getTranslation("gui.general." + (value ? "include" : "exclude"));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int x, int y, int click) {
        super.mouseClicked(x, y, click);
        this.playerName.mouseClicked(x, y, click);
        this.playerName.setFocused(true);
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (key == Keyboard.KEY_RETURN) {
            this.addPlayer();
        } else {
            if (!this.playerName.textboxKeyTyped(c, key)) super.keyTyped(c, key);
        }
    }
}
