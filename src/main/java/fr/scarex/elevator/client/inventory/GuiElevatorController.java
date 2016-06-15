package fr.scarex.elevator.client.inventory;

import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.scarex.elevator.Elevator;
import fr.scarex.elevator.client.gui.GuiButtonKeyBinding;
import fr.scarex.elevator.client.gui.GuiButtonTrim;
import fr.scarex.elevator.client.gui.GuiProgressBar;
import fr.scarex.elevator.client.inventory.scrollinglist.GuiScrollingListElevatorController;
import fr.scarex.elevator.inventory.container.ContainerElevatorController;
import fr.scarex.elevator.inventory.container.ContainerUseless;
import fr.scarex.elevator.network.PacketTeleportationListRequest;
import fr.scarex.elevator.tileentity.ITeleportationEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;

/**
 * @author SCAREX
 *
 */
public class GuiElevatorController extends GuiContainer
{
    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation("textures/gui/demo_background.png");
    protected GuiScrollingListElevatorController scrollingList;
    protected GuiButtonTrim buttonModifyPlayerList;
    protected GuiProgressBar energyProgressBar;
    public int tileX;
    public int tileY;
    public int tileZ;
    public boolean fromController;

    public GuiElevatorController(int tileX, int tileY, int tileZ, boolean fromController) {
        super(new ContainerElevatorController(null, false));
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileZ = tileZ;
        this.fromController = fromController;

        this.xSize = 248;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        Elevator.NETWORK.sendToServer(new PacketTeleportationListRequest(tileX, tileY, tileZ));
        super.initGui();

        this.scrollingList = new GuiScrollingListElevatorController(200, this, this.guiTop - (this.fromController ? 14 : 40), this.guiLeft + 24);
        this.scrollingList.registerScrollButtons(this.buttonList, 10, 11);

        if (fromController) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;

            this.buttonList.add(this.buttonModifyPlayerList = new GuiButtonTrim(0, k + 24, l - 36, Elevator.getTranslation("gui.button.modifyPlayerList")));
            
            this.energyProgressBar = new GuiProgressBar(k + 24, l - 56, 200, 18, 1, 1000, 0xFFFF0000, 0xFF000000, Elevator.getTranslation("gui.general.energy"), 0xFFFFFF);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float prt, int x, int y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(BASE_TEXTURE);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2 - 64;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 160);
        this.drawTexturedModalRect(k, l + 160, 0, 32, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float prt) {
        super.drawScreen(mouseX, mouseY, prt);
        this.scrollingList.drawScreen(mouseX, mouseY, prt);
        if (fromController) this.energyProgressBar.drawBar(mc, mouseX, mouseY);
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    public void updateList(List<ITeleportationEntry> list) {
        this.scrollingList.updateList(list);
    }
    
    public void updateEnergyStored(int value) {
        this.energyProgressBar.setSize(value);
    }
    
    public void updateMaxEnergyStored(int value) {
        this.energyProgressBar.setMaxSize(value);
    }

    public RenderItem getItemRenderer() {
        return itemRender;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
        case 0:
            Minecraft.getMinecraft().thePlayer.openGui(Elevator.INSTANCE, 2, Minecraft.getMinecraft().theWorld, tileX, tileY, tileZ);
            break;
        }
    }
}
