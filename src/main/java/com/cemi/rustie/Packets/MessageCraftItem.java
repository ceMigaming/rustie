package com.cemi.rustie.Packets;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.cemi.rustie.CraftingRegistry;
import com.cemi.rustie.item.ItemBase;
import com.cemi.rustie.item.RustieItems;
import com.cemi.rustie.utility.ItemFinder;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCraftItem implements IMessage, IMessageHandler<MessageCraftItem, IMessage> {

	public int desiredItem;
	
	public MessageCraftItem() {}
	
	public MessageCraftItem(int desiredItem) {
		this.desiredItem = desiredItem;
	}
	
	@Override
	public IMessage onMessage(MessageCraftItem message, MessageContext ctx) {
		List<Pair<ItemStack, Integer>> materials = CraftingRegistry.getMaterialsFromId(message.desiredItem);
		Triple<ItemStack, Integer, String> output = CraftingRegistry.getOutputFromId(message.desiredItem);
		ItemStack outputStack = output.getLeft();
		EntityPlayerMP player = ctx.getServerHandler().player;
		
		for(Pair<ItemStack, Integer> currentPair : materials)
			if(ItemFinder.findItem(player.inventory, currentPair.getLeft()) == -1)
				return null;
			else
			{
				int slot = ItemFinder.findItem(player.inventory, currentPair.getLeft());
				ItemStack itemStack = player.inventory.getStackInSlot(slot);
				if(itemStack.getTagCompound().getInteger("rustieCount") < currentPair.getRight()) 
					return null;
			}
		
		int emptySlot = -1;
		for(int i = 0; i < 36; i++) 
			if(player.inventory.getStackInSlot(i).isEmpty())
				emptySlot = i;
		if(emptySlot == -1) return null;
		
		for(Pair<ItemStack, Integer> currentPair : materials) {
			int slot = ItemFinder.findItem(player.inventory, currentPair.getLeft());
			ItemStack itemStack = player.inventory.getStackInSlot(slot);
			if(itemStack.getTagCompound().getInteger("rustieCount") > currentPair.getRight()) {
				itemStack.getTagCompound().setInteger("rustieCount", itemStack.getTagCompound().getInteger("rustieCount") - currentPair.getRight());
			}
			else if(itemStack.getTagCompound().getInteger("rustieCount") == currentPair.getRight())
				player.inventory.removeStackFromSlot(slot);
		}
		outputStack.setCount(1);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("rustieCount", output.getMiddle());
			outputStack.setTagCompound(nbt);
			int outputSlot = ItemFinder.findItem(player.inventory, outputStack);
			System.out.println("test");
			if(outputSlot == -1)
				player.inventory.addItemStackToInventory(outputStack);
			else
				player.inventory.getStackInSlot(outputSlot).getTagCompound().setInteger("rustieCount", player.inventory.getStackInSlot(outputSlot).getTagCompound().getInteger("rustieCount") + output.getMiddle());
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		desiredItem = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(desiredItem);
	}
	
}
