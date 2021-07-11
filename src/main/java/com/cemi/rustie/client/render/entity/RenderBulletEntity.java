package com.cemi.rustie.client.render.entity;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.render.entity.models.BulletModel;
import com.cemi.rustie.entity.EntityBullet;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBulletEntity extends Render<EntityBullet> {

	private static final ResourceLocation texture = new ResourceLocation(Rustie.MODID, "textures/entity/nail.png");
    private ModelBase model;
	
    public RenderBulletEntity(RenderManager renderManagerIn)
    {
    	super(renderManagerIn);
    	model = new BulletModel();
    }

    int a = 0;
    
    public void doRender(EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        //GlStateManager.rotate(a, 0.0F, 1.0F, 0.0F);
        a+=2;
        a%=360;
        GlStateManager.rotate(180 + entity.prevRotationYaw + (entity.prevRotationYaw - entity.rotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        
        GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        bindTexture(texture);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity) {
		// TODO Auto-generated method stub
		return texture;
	}
}
