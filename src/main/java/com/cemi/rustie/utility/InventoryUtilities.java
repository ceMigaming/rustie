package com.cemi.rustie.utility;

import com.cemi.rustie.item.ItemBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryUtilities {

	public static boolean addItem(InventoryPlayer inventory, ItemStack itemStackIn) {
		if(itemStackIn.getItem() instanceof ItemBase) {
			int slot = findItem(inventory, itemStackIn);
			NBTTagCompound nbt;
			if(slot != -1) {
				ItemStack inventoryStack = inventory.getStackInSlot(slot);
				if(inventoryStack.hasTagCompound()) {
					nbt = inventoryStack.getTagCompound();
				} 
				else {
					nbt = new NBTTagCompound();
				}
				
				if (nbt.hasKey("rustieCount")) {
				    nbt.setInteger("rustieCount", nbt.getInteger("rustieCount") + itemStackIn.getCount());
				}
				else {
				    nbt.setInteger("rustieCount", itemStackIn.getCount());
				}
				inventoryStack.setTagCompound(nbt);
				itemStackIn.setCount(0);
			}
			else {
				if(itemStackIn.hasTagCompound()) {
					nbt = itemStackIn.getTagCompound();
				} 
				else {
					nbt = new NBTTagCompound();
				}
				nbt.setInteger("rustieCount", itemStackIn.getCount());
				itemStackIn.setCount(1);
				itemStackIn.setTagCompound(nbt);
				return inventory.addItemStackToInventory(itemStackIn);
			}
		}
		return true;
	}
	
	public static void removeItem(InventoryPlayer inventoryIn, ItemStack itemStackIn) {
		if (itemStackIn.getItem() instanceof ItemBase) {
			int slot = -1;
			ItemStack newStack = new ItemStack(itemStackIn.getItem());
			NBTTagCompound nbt;
			if (itemStackIn.hasTagCompound()) {
				nbt = itemStackIn.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
			}
			if (nbt.hasKey("rustieCount")) {
				int count = nbt.getInteger("rustieCount");
				if (count > 1) {
					nbt.setInteger("rustieCount", nbt.getInteger("rustieCount") - 1);
					itemStackIn.setTagCompound(nbt);
					newStack.setTagCompound(nbt);
					if(nbt.hasKey("rustieSlot"))
						slot = nbt.getInteger("rustieSlot");
					inventoryIn.add(slot, newStack);
				} else {
					nbt.setInteger("rustieCount", 1);
				}
			}
		}
		/*
		 * ItemStack stack = event.getEntityItem().getItem(); if(stack.getItem()
		 * instanceof ItemBase) { ItemStack newStack = new ItemStack(stack.getItem());
		 * if(((ItemBase) stack.getItem()).getCount(event.getPlayer()) > 1) {
		 * if(!event.getPlayer().getEntityWorld().isRemote)
		 * ((ItemBase)newStack.getItem()).setCount(event.getPlayer(),
		 * ((ItemBase)stack.getItem()).getCount(event.getPlayer())-1);
		 * event.getPlayer().inventory.addItemStackToInventory(newStack); } }
		 */
	}

	public static int findItem(InventoryPlayer inventoryIn, ItemStack itemStackIn) {
		for (int i = 0; i < 36; i++) {
			if (!inventoryIn.getStackInSlot(i).isEmpty() && inventoryIn.getStackInSlot(i).isItemEqual(itemStackIn))
				return i;
		}
		return -1;
	}
}
