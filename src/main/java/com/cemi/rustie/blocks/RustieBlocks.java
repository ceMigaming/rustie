package com.cemi.rustie.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class RustieBlocks {

	public static BoulderBlock boulder = new BoulderBlock();
			
	
	@SuppressWarnings("deprecation")
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				boulder
		);
		GameRegistry.registerTileEntity(boulder.getTileEntityClass(), boulder.getRegistryName().toString());
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				boulder.createItemBlock()
		);
	}

	public static void registerModels() {
		boulder.registerItemModel(Item.getItemFromBlock(boulder));
	}
	
}
