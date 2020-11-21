package com.cemi.rustie.handlers;

import com.cemi.rustie.item.ItemBase;
import com.cemi.rustie.utility.ItemFinder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemPickupHandler {
	@SubscribeEvent
	public static void onPickupItem(EntityItemPickupEvent event) {
		ItemStack stack = event.getItem().getItem();
		EntityPlayer player = event.getEntityPlayer();
		if(stack.getItem() instanceof ItemBase) {
			int slot = ItemFinder.findItem(player.inventory, stack);
			NBTTagCompound nbt;
			if(slot != -1) {
				ItemStack inventoryStack = player.inventory.getStackInSlot(slot);
				if(inventoryStack.hasTagCompound()) {
					nbt = inventoryStack.getTagCompound();
				} 
				else {
					nbt = new NBTTagCompound();
				}
				
				if (nbt.hasKey("rustieCount")) {
				    nbt.setInteger("rustieCount", nbt.getInteger("rustieCount") + stack.getCount());
				}
				else {
				    nbt.setInteger("rustieCount", stack.getCount());
				}
				inventoryStack.setTagCompound(nbt);
				stack.setCount(0);
				System.out.println(inventoryStack.getTagCompound().getInteger("rustieCount"));
			}
			else {
				if(stack.hasTagCompound()) {
					nbt = stack.getTagCompound();
				} 
				else {
					nbt = new NBTTagCompound();
				}
				nbt.setInteger("rustieCount", stack.getCount());
				stack.setCount(1);
				stack.setTagCompound(nbt);
			}
		}
	}

	@SubscribeEvent
	public static void onItemToss(ItemTossEvent event) {
		ItemStack stack = event.getEntityItem().getItem();
		EntityPlayer player = event.getPlayer();
		if(stack.getItem() instanceof ItemBase)
		{
			ItemStack newStack = new ItemStack(stack.getItem());
			NBTTagCompound nbt;
			if(stack.hasTagCompound()) {
				nbt = stack.getTagCompound();
			} 
			else {
				nbt = new NBTTagCompound();
			}
			if (nbt.hasKey("rustieCount")) {
			    int count = nbt.getInteger("rustieCount");
			    if(count > 1) {
			    	nbt.setInteger("rustieCount", nbt.getInteger("rustieCount") - 1);
			    	System.out.println(nbt.getInteger("rustieCount"));
			    	stack.setTagCompound(nbt);
			    	newStack.setTagCompound(nbt);
			    	player.inventory.addItemStackToInventory(newStack);
			    }
			    else {
			    	nbt.setInteger("rustieCount", 1);
				}
			}
		}
		/*
			ItemStack stack = event.getEntityItem().getItem();
			if(stack.getItem() instanceof ItemBase)
			{
				ItemStack newStack = new ItemStack(stack.getItem());
				if(((ItemBase) stack.getItem()).getCount(event.getPlayer()) > 1) {
					if(!event.getPlayer().getEntityWorld().isRemote)
						((ItemBase)newStack.getItem()).setCount(event.getPlayer(), ((ItemBase)stack.getItem()).getCount(event.getPlayer())-1);
					event.getPlayer().inventory.addItemStackToInventory(newStack);
				}
			}
		 */
	}
	
	
}
