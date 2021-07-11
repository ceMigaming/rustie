package com.cemi.rustie.client.render.entity.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class NailModel extends ModelBase {
	private final ModelRenderer bb_main;

	public NailModel() {
		textureWidth = 16;
		textureHeight = 16;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 16.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -1.0F, -1.0F, 5, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -0.5F, -0.5F, 1, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bb_main.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        bb_main.rotateAngleY = netHeadYaw;
        bb_main.rotateAngleX = headPitch;
    }
}
