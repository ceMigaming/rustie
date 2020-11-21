package com.cemi.rustie.client;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.gui.RustieGuiInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RustieClient {
	
	@SubscribeEvent
	public static void openGui(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiInventory) event.setGui(new RustieGuiInventory(Minecraft.getMinecraft().player));
	}
}
