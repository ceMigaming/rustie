package com.cemi.rustie.item;

import com.cemi.rustie.Rustie;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class ItemArmor extends net.minecraft.item.ItemArmor {

	private String name;
	
	public int coldResistance;
	public int fireResistance;
	public int radiationResistance;
	public int electricityResistance;
	public int explosionResistance;
	public int bluntResistance;
	public int stabResistance;
	public int slashResistance;
	public int bulletResistance;
	public int biteResistance;
	
	
	
	public ItemArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
		super(material, 0, slot);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}

	public void registerItemModel() {
		Rustie.proxy.registerItemRenderer(this, 0, name);
	}
}
