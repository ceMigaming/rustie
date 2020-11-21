package com.cemi.rustie.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomGuiButton extends GuiButton
{
    int color = 0xFFCCCCCC, hoveredColor = 0xFFFFFFFF, pressedColor = 0xFF999999;
    int imageX, imageY, imageWidth, imageHeight;
    int texX = 0, texY = 0, texWidth = 16, texHeight = 16;
    ResourceLocation image;
    float scale;
    boolean pressed;
    
    public CustomGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, float scale, String buttonText, int color, int hoveredColor, int pressedColor, int imageX, int imageY, int imageWidth, int imageHeight, int texX, int texY, int texWidth, int texHeight, ResourceLocation image)
    {
    	super(buttonId, x, y, "");
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.x = x;
        this.y = y;
        this.width = widthIn;
        this.height = heightIn;
        this.scale = scale * 0.5f;
        this.displayString = buttonText;
        this.color = color;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.hoveredColor = hoveredColor;
        this.pressedColor = pressedColor;
        this.texX = texX;
        this.texY = texY;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.image = image;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
    	int bgColor;
    	
    	if(this.pressed)
        	bgColor = pressedColor;
        else if(this.hovered)
        	bgColor = hoveredColor;
        else
        	bgColor = color;
    	
        if (this.visible)
        {
        	FontRenderer fontrenderer = mc.fontRenderer;
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            //int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //GlStateManager.disableDepth();
            if(image != null)
            {
	            GlStateManager.pushMatrix();
	            GlStateManager.scale(this.scale, this.scale, 1f);
	            //GlStateManager.translate(- imageX, -imageY, 50f);
	            GlStateManager.translate(- (imageX - imageX / scale) - imageWidth / 8, - (imageY - imageY / scale), 50f);
            	mc.getTextureManager().bindTexture(image);
            	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            	this.drawTexturedModalRect(imageX, imageY, texX, texY, imageWidth, imageHeight);
            }
            //GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, bgColor);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 0xE0E0E0;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 0xA0A0A0;
            }
            else if (this.hovered)
            {
                j = 0xFFFFA0;
            }

            
            int textX = this.x + this.width / 2;
            int textY = this.y + (this.height * 3 / 4) / 2;
            GlStateManager.pushMatrix();
            GlStateManager.scale(this.scale, this.scale, 1f);
            GlStateManager.translate(- (textX - textX / this.scale), - (textY - textY / this.scale), 0f);
            this.drawCenteredString(fontrenderer, this.displayString, textX, textY, j);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    	this.pressed = false;
    	super.mouseReleased(mouseX, mouseY);
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    	this.pressed = true;
    	return super.mousePressed(mc, mouseX, mouseY);
    }
    
    public boolean isHovered() {
		return this.hovered;
	}
}