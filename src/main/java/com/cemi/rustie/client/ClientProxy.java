package com.cemi.rustie.client;

import com.cemi.rustie.CommonProxy;
import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.render.entity.RenderBulletEntity;
import com.cemi.rustie.entity.EntityBullet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Rustie.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public void clientInit()
	{
		MinecraftForge.EVENT_BUS.register(RustieClient.class);
		
		
	}
}
