package com.cemi.rustie.client;

import com.cemi.rustie.CommonProxy;
import com.cemi.rustie.Rustie;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

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
