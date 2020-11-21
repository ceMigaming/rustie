package com.cemi.rustie.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jline.terminal.MouseEvent.Button;
import org.lwjgl.input.Mouse;

import com.cemi.rustie.CraftingRegistry;
import com.cemi.rustie.Rustie;
import com.cemi.rustie.Packets.MessageCraftItem;
import com.cemi.rustie.handlers.RustiePacketHandler;
import com.cemi.rustie.utility.ItemFinder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RustieCraftingGui extends GuiContainer
{
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png");
    /** The old x position of the mouse pointer */
    private float oldMouseX;
    /** The old y position of the mouse pointer */
    private float oldMouseY;
    
    private CustomGuiButton testButton;
    private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();
    private boolean widthTooNarrow;
    private boolean buttonClicked;
    
    private List<CustomGuiButton> otherButtons = new ArrayList<>();
    private List<CustomGuiButton> tabButtons = new ArrayList<>();
    private List<CustomGuiButton> commonTabButtons = new ArrayList<>();
    
    private List<Pair<ItemStack, Integer>> ingredients = new ArrayList<>();
    private Triple<ItemStack, Integer, String> output;
    
    private float itemScale;
    
    private float itemXSize;
    private float itemYSize;
    private boolean mousePressed = false;
    
    private int firstBtnIndex = 1;
    
    private craftingTab currentTab = craftingTab.COMMON;
    
    public RustieCraftingGui(EntityPlayer player)
    {
        super(player.inventoryContainer);
        this.allowUserInput = true;
        }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
        }

        this.recipeBookGui.tick();
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        otherButtons.clear();
        tabButtons.clear();
        commonTabButtons.clear();

        if (this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
        }
        else
        {
        	super.guiLeft = 0;
        	super.guiTop = 0;
        	super.xSize = this.width;
        	super.ySize = this.height;
        	
        	itemScale = (float)width / 480;
            itemXSize = (float)width / 30;
            itemYSize = (float)height / 16;
        	
            super.initGui();
            
        }

        this.widthTooNarrow = this.width < 379;
        
        this.recipeBookGui.func_194303_a(this.width, this.height, this.mc, this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
        
        //this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
        
        this.testButton = new CustomGuiButton(11, //id
        		width / 2 - width / 10, 0, //x, y
        		width / 5, height / 15, //width, height
        		itemScale, // scale
        		"INVENTORY", //text
        		0x1ECCCCCC, 0x1EDDDDDD, 0x1E999999, //default color, hovered color, pressed color
        		width * 6 / 10 - (int)(itemXSize * 3 / 4), (int)(height / 30 - itemYSize / 4), //image x, image y
        		16, 16, //image width, image height
        		0, 0, //texture start x, texture start y
        		16, 16, //texture width, texture height
        		new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png") //texture
        	);
        
        
        for(int i = 50; i < 63; i++) {
        	tabButtons.add(new CustomGuiButton(i, width / 19 + 1, ((i - 50) * (height / 21 + 1)) + height / 12, width * 7 / 64 - 1, height / 22, itemScale, (craftingTab.values()[i-50]).toString(), 0x1FEEEEEE, 0x1FDDDDDD, 0x1FCCCCCC, 0, 0, 40, 40, 0, 0, 0, 0, new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png")));
        	
        }
        
        
        
        for(int i = 0; i < CraftingRegistry.getCount(); i++) {
        	commonTabButtons.add(new CustomGuiButton(i + 99, width / 19 + width / 9 + 3, -100, width * 21 / 64, height / 17, itemScale, "Craft " + CraftingRegistry.getOutputFromId(i).getLeft().getDisplayName(), 0x1FEEEEEE, 0x1FDDDDDD, 0x1FCCCCCC, 0, 0, 40, 40, 0, 0, 0, 0, new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png")));
        }
        
        this.otherButtons.add(this.testButton);
        
        buttonList.addAll(tabButtons);
        buttonList.addAll(commonTabButtons);
        buttonList.addAll(otherButtons);
        
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        // this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
    }


    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    	
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	this.drawDefaultBackground();
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        
        Gui.drawRect(width / 19, height / 15 + 2, width / 19 + width / 9, height / 15 + 2 + (int)(height / 1.5), 0x1FFFFFFF);
        
        Gui.drawRect(width / 19, height / 15 + 2 + (int)(height / 1.5) + 2, width / 2 - 1, height / 15 + 2 + (int)(height / 1.5) + 2 + height / 10, 0x1FFFFFFF);
        
        Gui.drawRect(width / 19 + width / 9 + 2, height / 15 + 2, width / 2 - 1, height / 15 + 2 + (int)(height / 1.5), 0x1FFFFFFF);
        
        Gui.drawRect(width / 2 + 1, height / 15 + 2, width * 18 / 19, height / 15 + 2 + (int)(height / 2.5) + 2 + height / 10, 0x1FFFFFFF);
        
        Gui.drawRect(width / 2 + 1, height / 15 + 2 + (int)(height / 2.5) + 4 + height / 10, width * 18 / 19, height / 15 + 2 + (int)(height / 1.5) + 2 + height / 10, 0x1FFFFFFF);
        
        
        for (int i1 = 0; i1 < this.otherButtons.size(); ++i1) {
            ((GuiButton)this.otherButtons.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        
        for (int i1 = 0; i1 < this.tabButtons.size(); ++i1) {
        	((CustomGuiButton)this.tabButtons.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        	if(craftingTab.valueOf(((CustomGuiButton)this.tabButtons.get(i1)).displayString) == currentTab) {
        		((CustomGuiButton)this.tabButtons.get(i1)).color = 0x7766ccff;
        		((CustomGuiButton)this.tabButtons.get(i1)).hoveredColor = 0xaa66ccff;
        	}
        }
        
        if(currentTab == craftingTab.COMMON) {
	        if(firstBtnIndex > commonTabButtons.size() - 11) firstBtnIndex = Math.max(commonTabButtons.size() - 10, 0);
	        if(firstBtnIndex < 0) firstBtnIndex = 0;
	        int offsetY = (firstBtnIndex - 1) * (height / 15);
	        
	        for (int i1 = firstBtnIndex; i1 < Math.min(firstBtnIndex + 10, firstBtnIndex + commonTabButtons.size()); ++i1) {
	        	((CustomGuiButton)this.commonTabButtons.get(i1)).y = i1 * (height / 15) - offsetY + height / 64 + 1;
	            ((CustomGuiButton)this.commonTabButtons.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
	            drawCraftingInfo(i1);
	        }
        } 
        else {
        	for (int i1 = firstBtnIndex; i1 < Math.min(firstBtnIndex + 10, firstBtnIndex + commonTabButtons.size()); ++i1) {
	        	((CustomGuiButton)this.commonTabButtons.get(i1)).y = -100;
	        }
        }
    }
    
    private void drawCraftingInfo(int i1) {
    	if(((CustomGuiButton)this.commonTabButtons.get(i1)).isHovered()) {
        	ingredients = CraftingRegistry.getMaterialsFromId(i1);
        	output = CraftingRegistry.getOutputFromId(i1);
        	for(int i2 = 0; i2 < ingredients.size(); i2++) {
        		int iconX = width / 2 + 3;
                int iconY = height / 15 + 4;
                float scale = itemScale * 2f;
        		GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, 1f);
                GlStateManager.translate(- (iconX - iconX / scale), - (iconY - iconY / scale), 32f);
                itemRender.renderItemAndEffectIntoGUI(output.getLeft(), iconX, iconY);
        		GlStateManager.popMatrix();
        		drawCenteredString(fontRenderer, output.getLeft().getDisplayName(), (width * 18 / 19) - ((width * 18 / 19) - (width / 2 + 1))/2, iconY, 0xFFFFFFFF);
        		fontRenderer.drawString(output.getRight(), iconX, (int)(iconY + 16 * scale) + 2, 0xFFFFFFFF);
        		int slot = ItemFinder.findItem(mc.player.inventory, ingredients.get(i2).getLeft());
        		fontRenderer.drawString(ingredients.get(i2).getRight() + "x", width / 2 + 1 + 2, 2 + height / 15 + 2 + (int)(height / 2.5) + 4 + height / 10 + i2 * 10, slot != -1 ? 0xFFFFFFFF : 0xFFFF0000);
        		fontRenderer.drawString(ingredients.get(i2).getLeft().getDisplayName(), width / 2 + 1 + 22, 2 + height / 15 + 2 + (int)(height / 2.5) + 4 + height / 10 + i2 * 10, slot != -1 ? 0xFFFFFFFF : 0xFFFF0000);
        	}
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0) {
            if (i > 1) {
                i = 1;
            }

            if (i < -1) {
                i = -1;
            }

            firstBtnIndex -= i;
        }
    }
    
    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	this.mousePressed = true;
    	if (mouseButton == 0) {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                GuiButton guibutton = this.buttonList.get(i);

                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                        break;
                    guibutton = event.getButton();
                    this.selectedButton = guibutton;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                    if (this.equals(this.mc.currentScreen))
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
                }
            }
            
        }
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	this.mousePressed = false;
    	if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    	
        this.dragSplitting = false;
    }

    protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.recipeBookGui.hasClickedOutside(p_193983_1_, p_193983_2_, this.width * 1 / 4, this.height * 1 / 10, this.width / 2, this.height * 8 /1) && flag;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 11) {
        	Minecraft.getMinecraft().displayGuiScreen(new RustieGuiInventory(Minecraft.getMinecraft().player));
        }
        
        if(button.id >= 50 && button.id <= 63) {
        	currentTab = craftingTab.valueOf(button.displayString);
        }
        if(button.id >= 99 && button.id <= 199) {
        	IMessage message = new MessageCraftItem(button.id-99);
            RustiePacketHandler.INSTANCE.sendToServer(message);
        }
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!this.recipeBookGui.keyPressed(typedChar, keyCode))
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    public void recipesUpdated()
    {
        this.recipeBookGui.recipesUpdated();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        this.recipeBookGui.removed();
        super.onGuiClosed();
    }
    
    @Override
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, (double)this.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)left, (double)top, (double)this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, (double)this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, (double)this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    enum craftingTab {
    	FAVORITE,
    	COMMON,
    	CONSTRUCTION,
    	ITEMS,
    	RESOURCES,
    	CLOTHING,
    	TOOLS,
    	MEDICAL,
    	WEAPONS,
    	AMMO,
    	ELECTRICAL,
    	FUN,
    	OTHER
    }
}