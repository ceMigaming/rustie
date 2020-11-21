package com.cemi.rustie.item;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.RustieTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class RustieItems {
	
	/************************ Crafting ingredients **************************/
	// Resources
	public static ItemBase animalFat = new ItemBase("animal_fat").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase boneFragments = new ItemBase("bone_fragments").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase charcoal = new ItemBase("charcoal").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase cloth = new ItemBase("cloth").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase crudeOil = new ItemBase("crude_oil").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase emptyCanOfBeans = new ItemBase("empty_can_of_beans").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase emptyTunaCan = new ItemBase("empty_tuna_can").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase explosives = new ItemBase("explosives").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase hqm = new ItemBase("hqm").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase hqmOre = new ItemBase("hqm_ore").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase humanSkull = new ItemBase("human_skull").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase leather = new ItemBase("leather").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase lowGradeFuel = new ItemBase("low_grade_fuel").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase metalFragments = new ItemBase("metal_fragments").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase metalOre = new ItemBase("metal_ore").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase saltWater = new ItemBase("salt_water").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase scrap = new ItemBase("scrap").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase stones = new ItemBase("stones").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase sulfur = new ItemBase("sulfur").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase sulfurOre = new ItemBase("sulfur_ore").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase water = new ItemBase("water").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase wolfSkull = new ItemBase("wolf_skull").setCreativeTab(RustieTabs.RESOURCES);
	public static ItemBase wood = new ItemBase("wood").setCreativeTab(RustieTabs.RESOURCES);
	
	// Components
	public static ItemBase cctvCamera = new ItemBase("cctv_camera").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase gears = new ItemBase("gears").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase emptyPropaneTank = new ItemBase("empty_propane_tank").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase metalBlade = new ItemBase("metal_blade").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase metalPipe = new ItemBase("metal_pipe").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase rifle_body = new ItemBase("rifle_body").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase roadSign = new ItemBase("road_sign").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase rope = new ItemBase("rope").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase semiautoBody = new ItemBase("semiauto_body").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase sewingKit = new ItemBase("sewing_kit").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase sheetMetal = new ItemBase("sheet_metal").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase smgBody = new ItemBase("smg_body").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase spring = new ItemBase("spring").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase targetingComputer = new ItemBase("targeting_computer").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase tarp = new ItemBase("tarp").setCreativeTab(RustieTabs.COMPONENTS);
	public static ItemBase techTrash = new ItemBase("tech_trash").setCreativeTab(RustieTabs.COMPONENTS);
	
	// Medicals
	public static ItemBase antiradPills = new ItemBase("antirad_pills").setCreativeTab(RustieTabs.MEDICALS);
	public static ItemBase bandage = new ItemBase("bandage").setCreativeTab(RustieTabs.MEDICALS);
	public static ItemBase largeMedkit = new ItemBase("large_medkit").setCreativeTab(RustieTabs.MEDICALS);
	public static ItemBase medicalSyringe = new ItemBase("medical_syringe").setCreativeTab(RustieTabs.MEDICALS);
	
	
	
	//public static ItemBase test = new ItemBase("test").setCreativeTab(RustieTabs.COMPONENTS);
	
	
	/************************ Armor **************************/
	// Burlap
	public static ItemArmor burlapHelmet = (ItemArmor) new ItemArmor(Rustie.burlapArmorMaterial, EntityEquipmentSlot.HEAD, "burlap_helmet").setCreativeTab(CreativeTabs.COMBAT);
	//public static ItemArmor burlapArmor = (ItemArmor) new ItemArmor(Rustie.burlapArmorMaterial, EntityEquipmentSlot.CHEST, "burlap_chestplate").setCreativeTab(CreativeTabs.COMBAT);
	//public static ItemArmor burlapLeggings = (ItemArmor) new ItemArmor(Rustie.burlapArmorMaterial, EntityEquipmentSlot.LEGS, "burlap_leggings").setCreativeTab(CreativeTabs.COMBAT);
	//public static ItemArmor burlapBoots = (ItemArmor) new ItemArmor(Rustie.burlapArmorMaterial, EntityEquipmentSlot.FEET, "burlap_boots").setCreativeTab(CreativeTabs.COMBAT);
	
	
	
	/*********************** Weapons *************************/
	// TEST
	public static GunBase M92Pistol = new GunBase("m92pistol");
	
	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
			// Resources
			animalFat,
			boneFragments,
			charcoal,
			cloth,
			crudeOil,
			emptyCanOfBeans,
			emptyTunaCan,
			explosives,
			hqm,
			hqmOre,
			humanSkull,
			leather,
			lowGradeFuel,
			metalFragments,
			metalOre,
			saltWater,
			scrap,
			stones,
			sulfur,
			sulfurOre,
			water,
			wolfSkull,
			wood,

			// Components
			cctvCamera,
			gears,
			emptyPropaneTank,
			metalBlade,
			metalPipe,
			rifle_body,
			roadSign,
			rope,
			semiautoBody,
			sewingKit,
			sheetMetal,
			smgBody,
			spring,
			targetingComputer,
			tarp,
			techTrash,
			
			// Medicals
			antiradPills,
			bandage,
			largeMedkit,
			medicalSyringe,
			
			// Armor
			burlapHelmet,
			//burlapArmor,
			//burlapLeggings,
			//burlapBoots,
						
			// Weapons
			M92Pistol
		);
	}
	
	public static void registerModels() {
		// Resources
		animalFat.registerItemModel();
		boneFragments.registerItemModel();
		charcoal.registerItemModel();
		cloth.registerItemModel();
		crudeOil.registerItemModel();
		emptyCanOfBeans.registerItemModel();
		emptyTunaCan.registerItemModel();
		explosives.registerItemModel();
		hqm.registerItemModel();
		hqmOre.registerItemModel();
		humanSkull.registerItemModel();
		leather.registerItemModel();
		lowGradeFuel.registerItemModel();
		metalFragments.registerItemModel();
		metalOre.registerItemModel();
		saltWater.registerItemModel();
		scrap.registerItemModel();
		stones.registerItemModel();
		sulfur.registerItemModel();
		sulfurOre.registerItemModel();
		water.registerItemModel();
		wolfSkull.registerItemModel();
		wood.registerItemModel();

		// Components
		cctvCamera.registerItemModel();
		gears.registerItemModel();
		emptyPropaneTank.registerItemModel();
		metalBlade.registerItemModel();
		metalPipe.registerItemModel();
		rifle_body.registerItemModel();
		roadSign.registerItemModel();
		rope.registerItemModel();
		semiautoBody.registerItemModel();
		sewingKit.registerItemModel();
		sheetMetal.registerItemModel();
		smgBody.registerItemModel();
		spring.registerItemModel();
		targetingComputer.registerItemModel();
		tarp.registerItemModel();
		techTrash.registerItemModel();

		// Medicals
		antiradPills.registerItemModel();
		bandage.registerItemModel();
		largeMedkit.registerItemModel();
		medicalSyringe.registerItemModel();

		// Armor
		burlapHelmet.registerItemModel();
		//burlapArmor.registerItemModel();
		//burlapLeggings.registerItemModel();
		//burlapBoots.registerItemModel();
		
		// Weapons
		M92Pistol.registerItemModel();
	}
	
}
