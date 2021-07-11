package com.cemi.rustie.entity;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.render.entity.RenderBulletEntity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

public class RustieEntities {
	
	public static void register(IForgeRegistry<EntityEntry> registry) {
		registry.register(EntityEntryBuilder.create().entity(EntityBullet.class).tracker(160, 1, true)
                .id(new ResourceLocation(Rustie.MODID, "bullet"), 33).name("bullet").build());
        
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new IRenderFactory<EntityBullet>() {
			@Override
			public Render<? super EntityBullet> createRenderFor(RenderManager manager)
			{
				return new RenderBulletEntity(manager);
			}
		});
	}
}
