package com.cemi.rustie.client.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.item.ItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RustieGuiInventory extends GuiContainer implements IRecipeShownListener
{
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png");
    /** The old x position of the mouse pointer */
    private float oldMouseX;
    /** The old y position of the mouse pointer */
    private float oldMouseY;
    
    private GuiButtonImage recipeButton;
    private CustomGuiButton testButton;
    private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();
    private boolean widthTooNarrow;
    //private boolean buttonClicked;
    
    private float itemScale;
    
    private float itemXSize;
    private float itemYSize;
    
    private boolean mousePressed = false;
    private static boolean entityHovered = false;
    static float lastMouseX, lastMouseY = 0, deltaRot = 0, lastRot = 0;
    
    private ItemStack lastStack = ItemStack.EMPTY;
    
    public RustieGuiInventory(EntityPlayer player)
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

    void drawSlots()
    {
    	List<Slot> inventorySlots = super.inventorySlots.inventorySlots;
    	
    	for(int i = 0; i < inventorySlots.size(); ++i)
    	{
    		Slot slot = inventorySlots.get(i);
    		if(i<5 || i == 45) 
    			slot.xPos = -32;
    		else if(i < 9)
    		{
    			slot.xPos = width * 1 / 6 + i % 5 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize*2.1);
    			slot.yPos = height * 8 / 10;
    		}
    		else if(i > 35)
    		{
    			slot.xPos = width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 2);
    			slot.yPos = height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + 8 + (int)itemYSize;
    		}
    		else
    		{
    			slot.xPos = width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 2);
    			slot.yPos = height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + (int)itemYSize;
			}
    	}
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();

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
        	
        	drawSlots();
            
            super.initGui();
            
        }

        this.widthTooNarrow = this.width < 379;
        
        this.recipeBookGui.func_194303_a(this.width, this.height, this.mc, this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
         
        //this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
        this.recipeButton = new GuiButtonImage(10, -100, -100, 20, 18, 178, 0, 19, INVENTORY_BACKGROUND);
        this.buttonList.add(this.recipeButton);
         
        
        this.testButton = new CustomGuiButton(11, //id
        		width / 2 - width / 10, 0, //x, y
        		width / 5, height / 15, //width, height
        		itemScale, // scale
        		"CRAFTING", //text
        		0x1ECCCCCC, 0x1EDDDDDD, 0x1E999999, //default color, hovered color, pressed color
        		width * 6 / 10 - (int)(itemXSize * 3 / 4), (int)(height / 30 - itemYSize / 4), //image x, image y
        		16, 16, //image width, image height
        		0, 0, //texture start x, texture start y
        		16, 16, //texture width, texture height
        		new ResourceLocation(Rustie.MODID, "textures/gui/buttons.png") //texture
        	);
        this.buttonList.add(this.testButton);
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
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
        drawEntityOnScreen(this.width / 6, this.height * 3 / 4, height / 3, 51 - this.oldMouseX, 25 - this.oldMouseY, mousePressed, this.width, this.height, this.mc.player);
        Gui.drawRect(0, 0, this.width, this.height, 0x05A8A8A8);
        for(int i = 5; i < 45; ++i)
    	{
    		if(i < 9)
    		{
    			Gui.drawRect(width * 1 / 6 + i % 5 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 1.1f), height * 8 / 10, width * 1 / 6 + i % 5 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 1.1f) - (int)itemXSize, height * 8 / 10 + (int)itemYSize, 0x1FFFFFFF);
    		}
    		else if(i > 35)
    		{
    			Gui.drawRect(width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 2), height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + 8 + (int)itemYSize, width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize), height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + 8 + (int)(itemYSize * 2), 0x1FFFFFFF);
    		}
    		else	
    		{
    			Gui.drawRect(width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 2), height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + (int)itemYSize, width * 4 / 10 + i % 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize), height * 1 / 2 + i / 9 * (int)(itemXSize + (itemXSize / 8)) + (int)(itemYSize * 2), 0x1FFFFFFF);
			}
    	}
        GlStateManager.pushMatrix();
        int textX = width * 4 / 10 - (int)(itemXSize * 2) + (int)(itemXSize / 8);
        int textY = height / 2 + (int)(itemYSize * 2) - (int)(itemYSize / 4);
        float scale = itemScale / 2;
        GlStateManager.scale(scale, scale, 1f);
        GlStateManager.translate(- (textX - textX / scale), - (textY - textY / scale), 0f);
        this.fontRenderer.drawString("INVENTORY", textX, textY, 0xFFFFFFFF);
        GlStateManager.popMatrix();
        
        for(int i = 0; i < 9; i++) {
        	int x = (int)(width / 6f);
        	int y = (int)(height * 9 / 40 + i * (itemYSize) + 2);
        	int rightX = this.width * 4 / 10 - (int)(itemXSize * 2) - 2;
        	int downY = (int)(this.height * 9 / 40 + i * (itemYSize) + (itemYSize));
        	this.drawGradientRect(x, 
        			y, 
        			rightX, 
        			downY, 
        			0x00FFFFFF, 0x1FFFFFFF);
        	GlStateManager.pushMatrix();
        	GlStateManager.scale(scale, scale, 1f);
        	int statsX = rightX - (int)(itemXSize * 1.5);
        	int statsY = y + (downY - y) / 2 - 2;
        	GlStateManager.translate(- (statsX - statsX / scale), - (statsY - statsY / scale), 0f);
        	this.drawString(this.fontRenderer, "0", 
        			statsX, 
        			statsY, 
        			0xFFFFFFFF);
        	GlStateManager.popMatrix();
        	// Draw images
        }
    }

    	
    
    /**
     * Draws an entity on the screen looking toward the cursor.
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, boolean mousePressed, int width, int height, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        //GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        if(!mousePressed)
        	entityHovered = false;
        if(mouseX < 0 && mouseY < 0 && mouseX > - width / 4.8 && mouseY > - height * 5 / 6)
	        entityHovered = true;
        if(mousePressed && entityHovered) 
        {
        	deltaRot = lastMouseX - mouseX;
        }
        
        lastRot = lastRot - deltaRot * 2f;
        ent.renderYawOffset = lastRot;
        ent.rotationYaw = lastRot;
        ent.rotationPitch = 0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	this.drawDefaultBackground();
    	drawEntityOnScreen(this.width / 6, this.height * 3 / 4, height / 3, 51 - this.oldMouseX,  25 - this.oldMouseY, mousePressed, this.width, this.height, this.mc.player);
    	
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        for (int i1 = 0; i1 < this.buttonList.size(); ++i1)
        {
            ((GuiButton)this.buttonList.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int j1 = 0; j1 < this.labelList.size(); ++j1)
        {
            ((GuiLabel)this.labelList.get(j1)).drawLabel(this.mc, mouseX, mouseY);
        }
        
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        
        this.recipeBookGui.render(mouseX, mouseY, partialTicks);
        
        try {
	        Field hoverField = this.getClass().getSuperclass().getDeclaredField("hoveredSlot");
	        hoverField.setAccessible(true);
	        hoverField.set(this, null);
	        Field draggedField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
	        draggedField.setAccessible(true);
	        Field rightClickField = this.getClass().getSuperclass().getDeclaredField("isRightMouseClick");
	        rightClickField.setAccessible(true);
	        Field splittingRemnantField = this.getClass().getSuperclass().getDeclaredField("dragSplittingRemnant");
	        splittingRemnantField.setAccessible(true);
	        Field returningStackField = this.getClass().getSuperclass().getDeclaredField("returningStack");
	        returningStackField.setAccessible(true);
	        Field returningStackTimeField = this.getClass().getSuperclass().getDeclaredField("returningStackTime");
	        returningStackTimeField.setAccessible(true);
	        Field returningStackDestSlotField = this.getClass().getSuperclass().getDeclaredField("returningStackDestSlot");
	        returningStackDestSlotField.setAccessible(true);
	        Field touchUpXField = this.getClass().getSuperclass().getDeclaredField("touchUpX");
	        touchUpXField.setAccessible(true);
	        Field touchUpYField = this.getClass().getSuperclass().getDeclaredField("touchUpY");
	        touchUpYField.setAccessible(true);
	        
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	
	        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
	        {
	            Slot slot = this.inventorySlots.inventorySlots.get(i1);
	
	            if (slot.isEnabled())
	            {
	                this.drawSlot(slot);
	            }
	
	            if (this.isMouseOverSlot(slot, mouseX, mouseY, (int)itemXSize, (int)itemYSize) && slot.isEnabled())
	            {
	            	hoverField.set(this, slot);
	                GlStateManager.disableLighting();
	                GlStateManager.disableDepth();
	                int j1 = slot.xPos;
	                int k1 = slot.yPos;
	                GlStateManager.colorMask(true, true, true, false);
	                this.drawGradientRect(j1, k1, j1 + (int)itemXSize, k1 + (int)itemYSize, -2130706433, -2130706433);
	                GlStateManager.colorMask(true, true, true, true);
	                GlStateManager.enableLighting();
	                GlStateManager.enableDepth();
	            }
	        }
	
	        RenderHelper.disableStandardItemLighting();
	        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
	        RenderHelper.enableGUIStandardItemLighting();
	        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
	        InventoryPlayer inventoryplayer = this.mc.player.inventory;
	        ItemStack draggedItemStack = (ItemStack)draggedField.get(this);
	        ItemStack itemstack = draggedItemStack.isEmpty() ? inventoryplayer.getItemStack() : draggedItemStack;
	
	        if (!itemstack.isEmpty())
	        {
	            int k2 = draggedItemStack.isEmpty() ? (int)(itemYSize / 2): (int)itemYSize;
	            
	            String s;
	            if(itemstack.getItem() instanceof ItemBase)
		        	s = itemstack.getTagCompound().getInteger("rustieCount") > 1 ? "x" + itemstack.getTagCompound().getInteger("rustieCount") : null;
		        else s = itemstack.getCount() > 1 ? "x" + itemstack.getCount() : null;
	            if (!draggedItemStack.isEmpty() && (boolean)rightClickField.get(this))
	            {
	            	itemstack = itemstack.copy();
	            	if(itemstack.getItem() instanceof ItemBase) {
	            		itemstack.getTagCompound().setInteger("rustieCount", MathHelper.ceil((float)itemstack.getTagCompound().getInteger("rustieCount") / 2.0F));
	            	}
	            	else {
	            		itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
	            	}
	            }
	            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1)
	            {
	                itemstack = itemstack.copy();
	                itemstack.setCount((int)splittingRemnantField.get(this));
	
	                if (itemstack.isEmpty())
	                {
	                    s = "" + TextFormatting.YELLOW + "0";
	                }
	            }
	
	            this.drawItemStack(itemstack, mouseX - i - (int)(itemXSize / 2), mouseY - j - k2, s);
	        }
	        ItemStack returningStack = (ItemStack)returningStackField.get(this);
	        if (!returningStack.isEmpty())
	        {
	            float f = (float)((long)(Minecraft.getSystemTime() - (long)returningStackTimeField.get(this)) / 100.0F);
	
	            if (f >= 1.0F)
	            {
	                f = 1.0F;
	                returningStack = ItemStack.EMPTY;
	            }
	
	            int l2 = ((Slot)returningStackDestSlotField.get(this)).xPos - (int)touchUpXField.get(this);
	            int i3 = ((Slot)returningStackDestSlotField.get(this)).yPos - (int)touchUpYField.get(this);
	            int l1 = (int)touchUpXField.get(this) + (int)((float)l2 * f);
	            int i2 = (int)touchUpYField.get(this) + (int)((float)i3 * f);
	            this.drawItemStack(returningStack, l1, i2, (String)null);
	        }
	
	        GlStateManager.popMatrix();
	        GlStateManager.enableLighting();
	        GlStateManager.enableDepth();
	        RenderHelper.enableStandardItemLighting();
			
        } catch (Exception e) {
			e.printStackTrace();
		}
        
		
        this.renderHoveredToolTip(mouseX, mouseY);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
    }
    
    public boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY, int xSize, int ySize)
    {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, xSize, ySize, mouseX, mouseY);
    }
    
    private void drawSlot(Slot slotIn)
    {
    	if(!lastStack.isEmpty()) {
    		this.renderCustomToolTip(lastStack);
    	}
    	try {
	    	Field clickedSlotField = this.getClass().getSuperclass().getDeclaredField("clickedSlot");
	    	clickedSlotField.setAccessible(true);
	    	Field draggedField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
	        draggedField.setAccessible(true);
	        Field rightClickField = this.getClass().getSuperclass().getDeclaredField("isRightMouseClick");
	        rightClickField.setAccessible(true);
	        Field dragSplittingLimitField = this.getClass().getSuperclass().getDeclaredField("dragSplittingLimit");
	        dragSplittingLimitField.setAccessible(true);
	        
	        int i = slotIn.xPos;
	        int j = slotIn.yPos;
	        ItemStack itemstack = slotIn.getStack();
	        boolean flag = false;
	        boolean flag1 = slotIn == (Slot)clickedSlotField.get(this) && !((ItemStack)draggedField.get(this)).isEmpty() && !(boolean)rightClickField.get(this);
	        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
	        String s;
	        if(itemstack.getItem() instanceof ItemBase) {
	        	s = itemstack.getTagCompound().getInteger("rustieCount") > 1 ? "x" + itemstack.getTagCompound().getInteger("rustieCount") : null;
	        }
	        else s = itemstack.getCount() > 1 ? "x" + itemstack.getCount() : null;
	
	        if (slotIn == (Slot)clickedSlotField.get(this) && !((ItemStack)draggedField.get(this)).isEmpty() && (boolean)rightClickField.get(this) && !itemstack.isEmpty())
	        {
	            itemstack = itemstack.copy();
	            itemstack.setCount(itemstack.getCount() / 2);
	        }
	        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
	        {
	            if (this.dragSplittingSlots.size() == 1)
	            {
	                return;
	            }
	
	            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
	            {
	                itemstack = itemstack1.copy();
	                flag = true;
	                Container.computeStackSize(this.dragSplittingSlots, (int)dragSplittingLimitField.get(this), itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
	                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
	
	                if (itemstack.getCount() > k)
	                {
	                    s = "" + TextFormatting.YELLOW.toString() + k;
	                    itemstack.setCount(k);
	                }
	            }
	            else
	            {
	                this.dragSplittingSlots.remove(slotIn);
	                this.updateDragSplitting();
	            }
	        }
	
	        this.zLevel = 100.0F;
	        this.itemRender.zLevel = 100.0F;
	
	        if (itemstack.isEmpty() && slotIn.isEnabled())
	        {
	            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
	
	            if (textureatlassprite != null)
	            {
	                GlStateManager.disableLighting();
	                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
	                this.drawTexturedModalRect(i, j, textureatlassprite, (int)itemXSize, (int)itemYSize);
	                GlStateManager.enableLighting();
	                flag1 = true;
	            }
	        }
	
	        if (!flag1)
	        {
	            if (flag)
	            {
	                drawRect(i, j, i + (int)itemXSize, j + (int)itemYSize, -2130706433);
	            }
	            GlStateManager.pushMatrix();
	            GlStateManager.enableDepth();
	            GlStateManager.scale(itemScale, itemScale, 1f);
	            GlStateManager.translate(-(i-i/itemScale), -(j-j/itemScale), 0f);
	            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
	            GlStateManager.popMatrix();
	            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i + (int)(itemXSize / 2), j + (int)(itemYSize / 2), s);
	        }
	
	        this.itemRender.zLevel = 0.0F;
	        this.zLevel = 0.0F;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void updateDragSplitting()
    {
    	try {
	    	Field dragSplittingLimitField = this.getClass().getSuperclass().getDeclaredField("dragSplittingLimit");
	        dragSplittingLimitField.setAccessible(true);
	        Field dragSplittingRemnantField = this.getClass().getSuperclass().getDeclaredField("dragSplittingRemnant");
	        dragSplittingRemnantField.setAccessible(true);
	    	
	        ItemStack itemstack = this.mc.player.inventory.getItemStack();
	
	        if (!itemstack.isEmpty() && this.dragSplitting)
	        {
	            if ((int)dragSplittingLimitField.get(this) == 2)
	            {
	            	dragSplittingRemnantField.set(this, itemstack.getMaxStackSize());
	            }
	            else
	            {
	            	dragSplittingRemnantField.set(this, itemstack.getCount());
	
	                for (Slot slot : this.dragSplittingSlots)
	                {
	                    ItemStack itemstack1 = itemstack.copy();
	                    ItemStack itemstack2 = slot.getStack();
	                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
	                    Container.computeStackSize(this.dragSplittingSlots, (int)dragSplittingLimitField.get(this), itemstack1, i);
	                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
	
	                    if (itemstack1.getCount() > j)
	                    {
	                        itemstack1.setCount(j);
	                    }
	                    
	                    dragSplittingRemnantField.set(this, (int)dragSplittingRemnantField.get(this) - itemstack1.getCount() - i);
	                }
	            }
	        }
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
    	try {
			Field draggedStackField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
			draggedStackField.setAccessible(true);
		
			GlStateManager.pushMatrix();
		    GlStateManager.translate(0.0F, 0.0F, 32.0F);
		    GlStateManager.scale(itemScale, itemScale, 1f);
		    GlStateManager.translate(- (x - x / itemScale), - (y - y / itemScale), 0f);
		    this.zLevel = 200.0F;
		    this.itemRender.zLevel = 200.0F;
		    net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		    if (font == null) font = fontRenderer;
		    this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		    GlStateManager.popMatrix();
		    this.itemRender.renderItemOverlayIntoGUI(font, stack, x + (int)(itemXSize / 2), y + (int)(itemYSize / 2)  /* - (((ItemStack)draggedStackField.get(this)).isEmpty() ? 0 : (int)(itemYSize / 2)) */, altText);
		    this.zLevel = 0.0F;
		    this.itemRender.zLevel = 0.0F;
    	} catch (Exception e) {
			e.printStackTrace();
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

    private Slot getSlotAtPosition(int x, int y)
    {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (isMouseOverSlot(slot, x, y, (int)itemXSize, (int)itemYSize) && slot.isEnabled())
            {
                return slot;
            }
        }

        return null;
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
    	this.mousePressed = true;
    	if (mouseButton == 0)
        {
            for (int i = 0; i < this.buttonList.size(); ++i)
            {
                GuiButton guibutton = this.buttonList.get(i);

                if (guibutton.mousePressed(this.mc, mouseX, mouseY))
                {
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
    	
    	try {
	    	Field doubleClickField = this.getClass().getSuperclass().getDeclaredField("doubleClick");
	    	doubleClickField.setAccessible(true);
	    	Field lastClickSlotField = this.getClass().getSuperclass().getDeclaredField("lastClickSlot");
	    	lastClickSlotField.setAccessible(true);
	    	Field lastClickTimeField = this.getClass().getSuperclass().getDeclaredField("lastClickTime");
	    	lastClickTimeField.setAccessible(true);
	    	Field lastClickButtonField = this.getClass().getSuperclass().getDeclaredField("lastClickButton");
	    	lastClickButtonField.setAccessible(true);
	    	Field ignoreMouseUpField = this.getClass().getSuperclass().getDeclaredField("ignoreMouseUp");
	    	ignoreMouseUpField.setAccessible(true);
	    	Field clickedSlotField = this.getClass().getSuperclass().getDeclaredField("clickedSlot");
	    	clickedSlotField.setAccessible(true);
	    	Field draggedStackField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
	    	draggedStackField.setAccessible(true);
	    	Field isRightMouseClickField = this.getClass().getSuperclass().getDeclaredField("isRightMouseClick");
	    	isRightMouseClickField.setAccessible(true);
	    	Field shiftClickedSlotField = this.getClass().getSuperclass().getDeclaredField("shiftClickedSlot");
	    	shiftClickedSlotField.setAccessible(true);
	    	Field dragSplittingButtonField = this.getClass().getSuperclass().getDeclaredField("dragSplittingButton");
	    	dragSplittingButtonField.setAccessible(true);
	    	Field dragSplittingLimitField = this.getClass().getSuperclass().getDeclaredField("dragSplittingLimit");
	    	dragSplittingLimitField.setAccessible(true);
	    	
	        boolean flag = this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
	        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
	        long i = Minecraft.getSystemTime();
	        doubleClickField.set(this, (Slot)lastClickSlotField.get(this) == slot && i - (long)lastClickTimeField.get(this) < 250L && (int)lastClickButtonField.get(this) == mouseButton);
	        ignoreMouseUpField.set(this, false);
	
	        if (mouseButton == 0 || mouseButton == 1 || flag)
	        {
	            int j = this.guiLeft;
	            int k = this.guiTop;
	            boolean flag1 = this.hasClickedOutside(mouseX, mouseY, j, k);
	            if (slot != null) flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
	            int l = -1;
	
	            if (slot != null)
	            {
	                l = slot.slotNumber;
	            }
	
	            if (flag1)
	            {
	                l = -999;
	            }
	
	            if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().isEmpty())
	            {
	                this.mc.displayGuiScreen((GuiScreen)null);
	                return;
	            }
	
	            if (l != -1)
	            {
	                if (this.mc.gameSettings.touchscreen)
	                {
	                    if (slot != null && slot.getHasStack())
	                    {
	                    	clickedSlotField.set(this, slot);
	                    	draggedStackField.set(this, ItemStack.EMPTY);
	                    	isRightMouseClickField.set(this, mouseButton == 1);
	                    }
	                    else
	                    {
	                    	clickedSlotField.set(this, null);
	                    }
	                }
	                else if (!this.dragSplitting)
	                {
	                    if (this.mc.player.inventory.getItemStack().isEmpty())
	                    {
	                        if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
	                        {
	                            this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
	                        }
	                        else
	                        {
	                            boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
	                            ClickType clicktype = ClickType.PICKUP;
	
	                            if (flag2)
	                            {
	                            	shiftClickedSlotField.set(this, slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY);
	                                clicktype = ClickType.QUICK_MOVE;
	                            }
	                            else if (l == -999)
	                            {
	                                clicktype = ClickType.THROW;
	                            }
	
	                            this.handleMouseClick(slot, l, mouseButton, clicktype);
	                        }
	
	                        ignoreMouseUpField.set(this, true);
	                    }
	                    else
	                    {
	                        this.dragSplitting = true;
	                        dragSplittingButtonField.set(this, mouseButton);
	                        this.dragSplittingSlots.clear();
	
	                        if (mouseButton == 0)
	                        {
	                        	dragSplittingLimitField.set(this, 0);
	                        }
	                        else if (mouseButton == 1)
	                        {
	                        	dragSplittingLimitField.set(this, 1);
	                        }
	                        else if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
	                        {
	                        	dragSplittingLimitField.set(this, 2);
	                        }
	                    }
	                }
	            }
	        }
	
	        lastClickSlotField.set(this, slot);
	        lastClickTimeField.set(this, i);
	        lastClickButtonField.set(this, mouseButton);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
	    	try {
	    	Field clickedSlotField = this.getClass().getSuperclass().getDeclaredField("clickedSlot");
	    	clickedSlotField.setAccessible(true);
	    	Field draggedStackField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
	    	draggedStackField.setAccessible(true);
	    	Field currentDragTargetSlotField = this.getClass().getSuperclass().getDeclaredField("currentDragTargetSlot");
	    	currentDragTargetSlotField.setAccessible(true);
	    	Field dragItemDropDelayField = this.getClass().getSuperclass().getDeclaredField("dragItemDropDelay");
	    	dragItemDropDelayField.setAccessible(true);
	    	Field dragSplittingLimitField = this.getClass().getSuperclass().getDeclaredField("dragSplittingLimit");
	    	dragSplittingLimitField.setAccessible(true);
	    	
	    	
	        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
	        ItemStack itemstack = this.mc.player.inventory.getItemStack();
	
	        if (clickedSlotField.get(this) != null && this.mc.gameSettings.touchscreen)
	        {
	            if (clickedMouseButton == 0 || clickedMouseButton == 1)
	            {
	                if (((ItemStack)draggedStackField.get(this)).isEmpty())
	                {
	                    if (slot != (Slot)clickedSlotField.get(this) && !((Slot)clickedSlotField.get(this)).getStack().isEmpty())
	                    {
	                    	draggedStackField.set(this, ((Slot)clickedSlotField.get(this)).getStack().copy());
	                    }
	                }
	                else if (((ItemStack)draggedStackField.get(this)).getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, (ItemStack)draggedStackField.get(this), false))
	                {
	                    long i = Minecraft.getSystemTime();
	
	                    if (currentDragTargetSlotField.get(this) == slot)
	                    {
	                        if (i - (long)dragItemDropDelayField.get(this) > 500L)
	                        {
	                            this.handleMouseClick((Slot)clickedSlotField.get(this), ((Slot)clickedSlotField.get(this)).slotNumber, 0, ClickType.PICKUP);
	                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
	                            this.handleMouseClick((Slot)clickedSlotField.get(this), ((Slot)clickedSlotField.get(this)).slotNumber, 0, ClickType.PICKUP);
	                            dragItemDropDelayField.set(this, i + 750L);
	                            ((ItemStack)draggedStackField.get(this)).shrink(1);
	                            
	                        }
	                    }
	                    else
	                    {
	                    	currentDragTargetSlotField.set(this, slot);
	                    	dragItemDropDelayField.set(this, i);
	                    }
	                }
	            }
	        }
	        else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || (int)dragSplittingLimitField.get(this) == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot))
	        {
	            this.dragSplittingSlots.add(slot);
	            this.updateDragSplitting();
	        }
	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
    	this.mousePressed = false;
    	if (this.selectedButton != null && state == 0)
        {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    	
    	try {
	    	Field doubleClickField = this.getClass().getSuperclass().getDeclaredField("doubleClick");
	    	doubleClickField.setAccessible(true);
	    	Field shiftClickedSlotField = this.getClass().getSuperclass().getDeclaredField("shiftClickedSlot");
	    	shiftClickedSlotField.setAccessible(true);
	    	Field lastClickTimeField = this.getClass().getSuperclass().getDeclaredField("lastClickTime");
	    	lastClickTimeField.setAccessible(true);
	    	Field dragSplittingButtonField = this.getClass().getSuperclass().getDeclaredField("dragSplittingButton");
	    	dragSplittingButtonField.setAccessible(true);
	    	Field ignoreMouseUpField = this.getClass().getSuperclass().getDeclaredField("ignoreMouseUp");
	    	ignoreMouseUpField.setAccessible(true);
	    	Field clickedSlotField = this.getClass().getSuperclass().getDeclaredField("clickedSlot");
	    	clickedSlotField.setAccessible(true);
	    	Field draggedStackField = this.getClass().getSuperclass().getDeclaredField("draggedStack");
	    	draggedStackField.setAccessible(true);
	    	Field returningStackField = this.getClass().getSuperclass().getDeclaredField("returningStack");
	    	returningStackField.setAccessible(true);
	    	Field touchUpXField = this.getClass().getSuperclass().getDeclaredField("touchUpX");
	    	touchUpXField.setAccessible(true);
	    	Field touchUpYField = this.getClass().getSuperclass().getDeclaredField("touchUpY");
	    	touchUpYField.setAccessible(true);
	    	Field returningStackDestSlotField = this.getClass().getSuperclass().getDeclaredField("returningStackDestSlot");
	    	returningStackDestSlotField.setAccessible(true);
	    	Field returningStackTimeField = this.getClass().getSuperclass().getDeclaredField("returningStackTime");
	    	returningStackTimeField.setAccessible(true);
	    	Field dragSplittingLimitField = this.getClass().getSuperclass().getDeclaredField("dragSplittingLimit");
	    	dragSplittingLimitField.setAccessible(true);
	    	
	        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
	        int i = this.guiLeft;
	        int j = this.guiTop;
	        boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
	        if (slot != null) flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
	        int k = -1;
	
	        if (slot != null)
	        {
	            k = slot.slotNumber;
	        }
	
	        if (flag)
	        {
	            k = -999;
	        }
	
	        if ((boolean)doubleClickField.get(this) && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot))
	        {
	            if (isShiftKeyDown())
	            {
	                if (!((ItemStack)shiftClickedSlotField.get(this)).isEmpty())
	                {
	                    for (Slot slot2 : this.inventorySlots.inventorySlots)
	                    {
	                        if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.isSameInventory(slot) && Container.canAddItemToSlot(slot2, (ItemStack)shiftClickedSlotField.get(this), true))
	                        {
	                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
	                        }
	                    }
	                }
	            }
	            else
	            {
	                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
	            }
	
	            doubleClickField.set(this, false);
	            lastClickTimeField.set(this, 0L);
	        }
	        else
	        {
	            if (this.dragSplitting && (int)dragSplittingButtonField.get(this) != state)
	            {
	                this.dragSplitting = false;
	                this.dragSplittingSlots.clear();
	                ignoreMouseUpField.set(this, true);
	                return;
	            }
	
	            if ((boolean)ignoreMouseUpField.get(this))
	            {
	            	ignoreMouseUpField.set(this, false);
	                return;
	            }
	
	            if (clickedSlotField.get(this) != null && this.mc.gameSettings.touchscreen)
	            {
	                if (state == 0 || state == 1)
	                {
	                    if (((ItemStack)draggedStackField.get(this)).isEmpty() && slot != clickedSlotField.get(this))
	                    {
	                    	draggedStackField.set(this, ((Slot)clickedSlotField.get(this)).getStack());
	                    }
	
	                    boolean flag2 = Container.canAddItemToSlot(slot, (ItemStack)draggedStackField.get(this), false);
	
	                    if (k != -1 && !((ItemStack)draggedStackField.get(this)).isEmpty() && flag2)
	                    {
	                        this.handleMouseClick((Slot)clickedSlotField.get(this), ((Slot)clickedSlotField.get(this)).slotNumber, state, ClickType.PICKUP);
	                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);
	
	                        if (this.mc.player.inventory.getItemStack().isEmpty())
	                        {
	                            returningStackField.set(this, ItemStack.EMPTY);
	                        }
	                        else
	                        {
	                            this.handleMouseClick((Slot)clickedSlotField.get(this), ((Slot)clickedSlotField.get(this)).slotNumber, state, ClickType.PICKUP);
	                            touchUpXField.set(this, mouseX - i);
	                            touchUpYField.set(this, mouseY - j);
	                            returningStackDestSlotField.set(this, clickedSlotField.get(this));
	                            returningStackField.set(this, draggedStackField.get(this));
	                            returningStackTimeField.set(this, Minecraft.getSystemTime());
	                        }
	                    }
	                    else if (!((ItemStack)draggedStackField.get(this)).isEmpty())
	                    {
	                    	touchUpXField.set(this, mouseX - i);
	                        touchUpYField.set(this, mouseY - j);
	                        returningStackDestSlotField.set(this, clickedSlotField.get(this));
	                        returningStackField.set(this, draggedStackField.get(this));
	                        returningStackTimeField.set(this, Minecraft.getSystemTime());
	                    }
	
	                    draggedStackField.set(this, ItemStack.EMPTY);
	                    clickedSlotField.set(this, null);
	                }
	            }
	            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty())
	            {
	                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(0, (int)dragSplittingLimitField.get(this)), ClickType.QUICK_CRAFT);
	
	                for (Slot slot1 : this.dragSplittingSlots)
	                {
	                    this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, (int)dragSplittingLimitField.get(this)), ClickType.QUICK_CRAFT);
	                }
	
	                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(2, (int) dragSplittingLimitField.get(this)), ClickType.QUICK_CRAFT);
	            }
	            else if (!this.mc.player.inventory.getItemStack().isEmpty())
	            {
	                if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100))
	                {
	                    this.handleMouseClick(slot, k, state, ClickType.CLONE);
	                }
	                else
	                {
	                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
	
	                    if (flag1)
	                    {
	                        shiftClickedSlotField.set(this, slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY);
	                    }
	
	                    this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
	                }
	            }
	        }
	
	        if (this.mc.player.inventory.getItemStack().isEmpty())
	        {
	            lastClickTimeField.set(this, 0L);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        this.dragSplitting = false;
    }

    protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.recipeBookGui.hasClickedOutside(p_193983_1_, p_193983_2_, this.width * 1 / 4, this.height * 1 / 10, this.width / 2, this.height * 8 /1) && flag;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 11)
        {
        	Minecraft.getMinecraft().displayGuiScreen(new RustieCraftingGui(Minecraft.getMinecraft().player));
        }
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!this.recipeBookGui.keyPressed(typedChar, keyCode) && keyCode != 16)
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
    	if(mouseButton == 1 && type == ClickType.PICKUP && !slotIn.getStack().isEmpty())
    		if(slotIn.getStack().getItem() instanceof ItemBase)
    		{
    			if (mc.player.inventory.getItemStack().isEmpty())
                {
                    if (slotIn.getStack().isEmpty())
                    {
                        slotIn.putStack(ItemStack.EMPTY);
                        mc.player.inventory.setItemStack(ItemStack.EMPTY);
                    }
                    else
                    {
                    	System.out.println("TEST");
                    	if(!lastStack.isEmpty()) {
                    		lastStack = ItemStack.EMPTY;
                    	}
                    	else {
                    		lastStack = slotIn.getStack();
                    	}
                    	
                    }
                }
    			//mc.player.inventoryContainer.detectAndSendChanges();
    			//mc.getConnection().sendPacket(new CPacketClickWindow(mc.player.inventoryContainer.windowId, slotId, mouseButton, type, ItemStack.EMPTY, mc.player.openContainer.getNextTransactionID(mc.player.inventory)));
    			return;
    		}
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
    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_)
    {
    	try {
	    	Field hoveredSlotField = this.getClass().getSuperclass().getDeclaredField("hoveredSlot");
	    	hoveredSlotField.setAccessible(true);
	        if (this.mc.player.inventory.getItemStack().isEmpty() && (Slot)hoveredSlotField.get(this) != null && super.getSlotUnderMouse().getHasStack())
	        {
	            //this.renderCustomToolTip(super.getSlotUnderMouse().getStack());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    protected void renderCustomToolTip(ItemStack stack)
    {
    	// Panel
        Gui.drawRect(width * 4 / 10 - (int)(itemXSize * 2), height / 15 + (int)itemYSize, width * 4 / 10 + 9 * (int)(itemXSize + (itemXSize / 8)) - (int)(itemXSize * 2), height * 1 / 2 , 0x01FFFFFF);
        
        // Square
        Gui.drawRect(width * 4 / 10 - (int)(itemXSize * 2) + (int)(itemXSize / 4), height / 15 + (int)itemYSize + (int)(itemYSize / 4), width * 4 / 10 - (int)(itemXSize * 2) + (int)(itemXSize / 4) + (int)itemXSize * 2, height / 15 + (int)itemYSize + (int)(itemYSize / 4) + (int)(itemYSize * 2), 0x01FFFFFF);
        
        int textX = width * 4 / 10 - (int)(itemXSize * 2) + (int)(itemXSize / 16);
        int textY = height / 15 + (int)(itemYSize * 3 / 5);
        
        float scale = itemScale * 0.7f;
        
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1f);
        GlStateManager.translate(- (textX - textX / scale), - (textY - textY / scale), 0f);
        this.fontRenderer.drawString(I18n.format(stack.getDisplayName()), textX, textY, 0xFFFFFFFF);
        GlStateManager.popMatrix();
        
        int iconX = width * 4 / 10 - (int)(itemXSize * 2) + (int)(itemXSize / 4);
        int iconY = height / 15 + (int)itemYSize + (int)(itemYSize / 4);
        scale = itemScale * 2f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1f);
        GlStateManager.translate(- (iconX - iconX / scale), - (iconY - iconY / scale), 32f);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, iconX, iconY);
        GlStateManager.popMatrix();
        //this.itemRender.renderItemOverlayIntoGUI(font, stack, width * 1 / 2 - 140, 30, "1");
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        
        
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

	@Override
	public GuiRecipeBook func_194310_f() {
		// TODO Auto-generated method stub
		return null;
	}
}