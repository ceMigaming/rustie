package com.cemi.rustie.handlers;

import com.cemi.rustie.utility.InventoryUtilities;

import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemPickupHandler {
	@SubscribeEvent
	public static void onPickupItem(EntityItemPickupEvent event) {
		InventoryUtilities.addItem(event.getEntityPlayer().inventory, event.getItem().getItem());
	}

	@SubscribeEvent
	public static void onItemToss(ItemTossEvent event) {
		InventoryUtilities.removeItem(event.getPlayer().inventory, event.getEntityItem().getItem());
	}

}
