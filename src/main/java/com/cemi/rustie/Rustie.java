package com.cemi.rustie;

import java.util.HashMap;
import java.util.Map;

import com.cemi.rustie.client.RustieClient;
import com.cemi.rustie.handlers.ItemPickupHandler;
import com.cemi.rustie.item.RustieItems;
import com.google.common.collect.Maps;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui.ModIDEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Rustie.MODID, version = Rustie.VERSION)
public class Rustie
{
    public static final String MODID = "rustie";
    public static final String VERSION = "1.0";
    
    @SidedProxy(serverSide = "com.cemi.rustie.CommonProxy", clientSide = "com.cemi.rustie.client.ClientProxy")
    public static CommonProxy proxy;
    
    public static final ItemArmor.ArmorMaterial burlapArmorMaterial = EnumHelper.addArmorMaterial("BURLAP", MODID + ":burlap", 7, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    
    @Mod.Instance(MODID)
	public static Rustie instance;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ItemPickupHandler.class);
    }
    
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {
    	
    	
    	@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
    		RustieItems.register(event.getRegistry());
		}
    	
    	@SubscribeEvent
    	public static void registerItems(ModelRegistryEvent event) {
    		RustieItems.registerModels();
    	}
	}
    
    @Config(modid = "rustie")
    public static class Configs {
    	@Name("map")
        @Comment("This comment belongs to the \"map\" category, not the \"general\" category")
        @RequiresMcRestart
        public static Map<String, Integer[]> theMap;

        @Name("regex(test]")
        public static Map<String, String> regexText = new HashMap<>();

        static
        {
            theMap = Maps.newHashMap();
            for (int i = 0; i < 7; i++)
            {
                Integer[] array = new Integer[6];
                for (int x = 0; x < array.length; x++)
                {
                    array[x] = i + x;
                }
                theMap.put("" + i, array);
                regexText.put("" + i, "" + i);
            }
        }
    }
}


