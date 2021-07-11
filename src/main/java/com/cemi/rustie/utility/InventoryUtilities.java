package com.cemi.rustie.utility;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryUtilities {

	public static boolean addItem(InventoryPlayer inventory, ItemStack itemStackIn) {
		int foundItemSlot = findItem(inventory, itemStackIn);
		if(foundItemSlot == -1) 
			return false;
		
		return true;
	}
	
	public static boolean removeItem(InventoryPlayer inventoryIn, ItemStack itemStackIn) {
		int foundItemSlot = findItem(inventoryIn, itemStackIn);
		if(foundItemSlot == -1)
			return false;
		return true;
	}

	public static int findItem(InventoryPlayer inventoryIn, ItemStack itemStackIn) {
		for (int i = 0; i < 36; i++) {
			if (!inventoryIn.getStackInSlot(i).isEmpty() && inventoryIn.getStackInSlot(i).isItemEqual(itemStackIn))
				return i;
		}
		return -1;
	}
}
