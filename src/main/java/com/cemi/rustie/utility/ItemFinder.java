package com.cemi.rustie.utility;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ItemFinder {
	public static int findItem(InventoryPlayer inventory, ItemStack itemStackIn)
	{
        for (int i = 0; i < 36; i++)
        {
        	if(!inventory.getStackInSlot(i).isEmpty() && inventory.getStackInSlot(i).isItemEqual(itemStackIn))
        		return i;
        }
        return -1;
	}
}
