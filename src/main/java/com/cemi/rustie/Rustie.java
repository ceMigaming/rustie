package com.cemi.rustie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.cemi.rustie.blocks.RustieBlocks;
import com.cemi.rustie.entity.RustieEntities;
import com.cemi.rustie.handlers.ItemPickupHandler;
import com.cemi.rustie.handlers.RustiePacketHandler;
import com.cemi.rustie.item.RustieItems;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = Rustie.MODID, version = Rustie.VERSION)
public class Rustie
{
    public static final String MODID = "rustie";
    public static final String VERSION = "1.0";
    
    public static int RecipesCount = 0;
    
    @SidedProxy(serverSide = "com.cemi.rustie.CommonProxy", clientSide = "com.cemi.rustie.client.ClientProxy")
    public static CommonProxy proxy;
    
    public static final ItemArmor.ArmorMaterial burlapArmorMaterial = EnumHelper.addArmorMaterial("BURLAP", MODID + ":burlap", 7, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    
    @Mod.Instance(MODID)
	public static Rustie instance;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    	RustiePacketHandler.init();
    	
    	
    }
    
	@EventHandler
    public void init(FMLInitializationEvent event) throws Exception {
        MinecraftForge.EVENT_BUS.register(ItemPickupHandler.class);
        
        for (int i = 0; i < RecipesCount; i++) {
        	String[] recipes = Configs.RECIPES.get(Integer.toString(i)).replaceAll(" ", "").split(",");
        	List<Pair<ItemStack, Integer>> pairs = new ArrayList<>();
        	int firstJ = recipes.length % 2 == 1 ? 3 : 2;
        	for (int j = firstJ; j < recipes.length-firstJ+2; j+=2) {
        		ResourceLocation rl = new ResourceLocation(MODID, recipes[j]);
        		if(!ForgeRegistries.ITEMS.containsKey(rl))
        			throw new Exception("Incorrect input item: " + rl);
        		pairs.add(Pair.of(new ItemStack(ForgeRegistries.ITEMS.getValue(rl)), Integer.parseInt(recipes[j+1])));
        	}
        	ResourceLocation rl = new ResourceLocation(MODID, recipes[0]);
        	if(!ForgeRegistries.ITEMS.containsKey(rl))
    			throw new Exception("Incorrect ouput item: " + rl);
        	
        	if (recipes.length % 2 == 1) {
        		CraftingRegistry.registerRecipe(Triple.of(new ItemStack(ForgeRegistries.ITEMS.getValue(rl)), Integer.parseInt(recipes[1]), recipes[2]), pairs);
        	}
        	else {
        		CraftingRegistry.registerRecipe(Pair.of(new ItemStack(ForgeRegistries.ITEMS.getValue(rl)), Integer.parseInt(recipes[1])), pairs);
        	}
        }
        
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    	
    }
    
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {
    	
    	
    	@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
    		RustieItems.register(event.getRegistry());
    		RustieBlocks.registerItemBlocks(event.getRegistry());
    		CraftingRegistry.registerRecipes();
		}
    	
    	@SubscribeEvent
    	public static void registerModels(ModelRegistryEvent event) {
    		RustieItems.registerModels();
    		RustieBlocks.registerModels();
    	}
    	
    	@SubscribeEvent
    	public static void registerBlocks(RegistryEvent.Register<Block> event) {
    		RustieBlocks.register(event.getRegistry());
    	}
    	
    	@SubscribeEvent
        public static void registerEntity(RegistryEvent.Register<EntityEntry> event) {
            RustieEntities.register(event.getRegistry());
        }
    	
    	
	}
    
    
    
    @Config(modid = "rustie")
    public static class Configs {
    	@Name("Craftings")
        @Comment("Add craftings here in the format: <outputItem, outputCount, description, inputItem1, input1Count, inputItem2, input2Count, ... inputItemN, inputNCount")
        @RequiresMcRestart
        public static Map<String, String> RECIPES;
    	
        static
        {
        	RECIPES = Maps.newHashMap();
        	RECIPES.put("" + RecipesCount++, "burlap_helmet, 1, cloth, 10");
        	RECIPES.put("" + RecipesCount++, "bandage, 1, cloth, 4");
        	RECIPES.put("" + RecipesCount++, "low_grade_fuel, 4, animal_fat, 3, cloth, 1");
        }
    }
}


