package fr.scarex.elevator.client.inventory;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.client.gui.GuiButtonKeyBinding;
import fr.scarex.elevator.client.gui.GuiButtonTrim;
import fr.scarex.elevator.inventory.container.ContainerElevatorBlock;
import fr.scarex.elevator.network.PacketElevatorBlockAutoOpen;
import fr.scarex.elevator.network.PacketElevatorBlockAutoOpenRequest;
import fr.scarex.elevator.network.PacketElevatorBlockHotKey;
import fr.scarex.elevator.network.PacketElevatorBlockHotKeyRequest;
import fr.scarex.elevator.network.PacketElevatorBlockNameRequest;
import fr.scarex.elevator.network.PacketElevatorBlockNameToServer;
import fr.scarex.elevator.tileentity.TileEntityElevator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author SCAREX
 *
 */
public class GuiElevatorBlock extends GuiContainer implements ICrafting
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Elevator.MODID, "textures/gui/container/elevator_block.png");
    protected GuiTextField nameTextField;
    protected GuiButtonTrim buttonAutoOpenGui;
    protected GuiButtonTrim buttonModifyPlayerList;
    protected GuiButtonKeyBinding buttonKeybinding;
    public int tileX;
    public int tileY;
    public int tileZ;
    protected boolean autoOpen;

    public GuiElevatorBlock(InventoryPlayer playerInv, TileEntityElevator elevator) {
        super(new ContainerElevatorBlock(elevator, playerInv));
        this.tileX = elevator.xCoord;
        this.tileY = elevator.yCoord;
        this.tileZ = elevator.zCoord;

        this.ySize = 176;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        Elevator.NETWORK.sendToServer(new PacketElevatorBlockNameRequest(tileX, tileY, tileZ));
        Elevator.NETWORK.sendToServer(new PacketElevatorBlockAutoOpenRequest(tileX, tileY, tileZ));
        Elevator.NETWORK.sendToServer(new PacketElevatorBlockHotKeyRequest());
        super.initGui();

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.nameTextField = new GuiTextField(this.fontRendererObj, k + 50, l + 12, 115, 12);
        this.nameTextField.setFocused(true);

        this.buttonList.add(this.buttonAutoOpenGui = new GuiButtonTrim(0, k + 50, l + 26, 115, 20, ""));
        this.buttonList.add(this.buttonModifyPlayerList = new GuiButtonTrim(1, k + 50, l + 48, 115, 20, Elevator.getTranslation("gui.button.modifyPlayerList")));
        this.buttonList.add(this.buttonKeybinding = new GuiButtonKeyBinding(2, k + 50, l + 70, 115, 20, 0, 0));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float prt, int x, int y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(TEXTURE);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        this.nameTextField.drawTextBox();
        this.buttonAutoOpenGui.drawButton(this.mc, x, y);
        this.buttonModifyPlayerList.drawButton(this.mc, x, y);
    }

    @Override
    protected void mouseClicked(int x, int y, int click) {
        super.mouseClicked(x, y, click);
        this.nameTextField.mouseClicked(x, y, click);
        this.buttonAutoOpenGui.mousePressed(this.mc, x, y);
        this.buttonModifyPlayerList.mousePressed(this.mc, x, y);
        if (this.buttonKeybinding.mouseClicked(x, y, click)) this.sendKey();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
        case 0:
            this.updateAutoOpen(!this.autoOpen);
            Elevator.NETWORK.sendToServer(new PacketElevatorBlockAutoOpen(this.autoOpen));
            break;
        case 1:
            Minecraft.getMinecraft().thePlayer.openGui(Elevator.INSTANCE, 2, Minecraft.getMinecraft().theWorld, tileX, tileY, tileZ);
            break;
        }
    }

    @Override
    protected void keyTyped(char c, int n) {
        if (this.nameTextField.textboxKeyTyped(c, n))
            this.sendText();
        else if (this.buttonKeybinding.keyTyped(c, n))
            this.sendKey();
        else
            super.keyTyped(c, n);
    }

    private void sendText() {
        Elevator.NETWORK.sendToServer(new PacketElevatorBlockNameToServer(this.nameTextField.getText(), tileX, tileY, tileZ));
    }

    private void sendKey() {
        Elevator.NETWORK.sendToServer(new PacketElevatorBlockHotKey(this.buttonKeybinding.getKey()));
    }

    @Override
    public void sendContainerAndContentsToPlayer(Container container, List list) {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
        this.nameTextField.setText(((ContainerElevatorBlock) container).elevator.getName());
    }

    @Override
    public void sendSlotContents(Container container, int index, ItemStack stack) {
        if (index == 0) this.sendText();
    }

    @Override
    public void sendProgressBarUpdate(Container container, int index, int value) {}

    public void updateName(String name) {
        this.nameTextField.setText(name);
    }

    public void updateAutoOpen(boolean value) {
        this.autoOpen = value;
        this.buttonAutoOpenGui.displayString = Elevator.getTranslation("gui.button.autoOpen", value ? Elevator.getTranslation("gui.general.true") : Elevator.getTranslation("gui.general.false"));
    }

    public void updateHotKey(int value) {
        this.buttonKeybinding.setKey(value);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
}
