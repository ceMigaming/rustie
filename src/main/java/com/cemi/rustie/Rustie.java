package com.cemi.rustie;

import java.util.HashMap;
import java.util.Map;

import com.cemi.rustie.client.render.entity.RenderBulletEntity;
import com.cemi.rustie.entity.EntityBullet;
import com.cemi.rustie.handlers.ItemPickupHandler;
import com.cemi.rustie.handlers.RustiePacketHandler;
import com.cemi.rustie.item.RustieItems;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

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
    public void preinit(FMLPreInitializationEvent event) {
    	RustiePacketHandler.init();
    	
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ItemPickupHandler.class);
        
        
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    	
    }
    
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {
    	
    	
    	@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
    		RustieItems.register(event.getRegistry());
    		CraftingRegistry.registerRecipes();
		}
    	
    	@SubscribeEvent
    	public static void registerItems(ModelRegistryEvent event) {
    		RustieItems.registerModels();
    	}
    	
    	@SubscribeEvent
        public static void entityRegistration(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().register(EntityEntryBuilder.create().entity(EntityBullet.class).tracker(160, 1, true)
                    .id(new ResourceLocation(MODID, "bullet"), 33).name("bullet").build());
            System.out.println("Entries registered");
            
            RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new IRenderFactory<EntityBullet>() {
    			@Override
    			public Render<? super EntityBullet> createRenderFor(RenderManager manager)
    			{
    				return new RenderBulletEntity(manager);
    			}
    		});
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


