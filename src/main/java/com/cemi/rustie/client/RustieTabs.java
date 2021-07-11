package com.cemi.rustie.client;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.item.RustieItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public abstract class RustieTabs extends CreativeTabs {
	
    public static final RustieTabs RESOURCES = new RustieTabs("resources")
    {
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RustieItems.animalFat);
        }
    };
    public static final RustieTabs MEDICALS = new RustieTabs("medicals")
    {
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RustieItems.largeMedkit);
        }
    };
    public static final RustieTabs COMPONENTS = new RustieTabs("components")
    {
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RustieItems.targetingComputer);
        }
    };
    public static final RustieTabs WEAPONS = new RustieTabs("weapons")
    {
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(RustieItems.M92Pistol);
        }
    };
	public RustieTabs(String label) {
		super(Rustie.MODID + "." + label);
	}

}
