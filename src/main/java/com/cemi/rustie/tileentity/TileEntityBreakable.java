package com.cemi.rustie.tileentity;

import com.cemi.rustie.item.RustieItems;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBreakable extends TileEntity {

	private float maxHealth = 500;
	private float health = maxHealth;

	damageType type = damageType.generic;
	
	int itemQuantity = 0;
	
	Item dispensedItem = RustieItems.stones;

	public TileEntityBreakable() {
	}

	public TileEntityBreakable(int health, damageType type) {
		this.maxHealth = health;
		this.health = health;
		this.type = type;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setFloat("health", health);
		compound.setFloat("maxHealth", maxHealth);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		health = compound.getFloat("health");
		maxHealth = compound.getFloat("maxHealth");
		super.readFromNBT(compound);
	}

	public float getHealth() {
		return health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void dealDamage(float damage, float woodDamage, float boulderDamage, float lostFraction) {
		float finalDamage;
		float mult = (100.0f - lostFraction)/100.0f;
		switch (type) {
		case generic:
			finalDamage = damage*mult;
			break;
		case wood:
			finalDamage = woodDamage*mult;
			break;
		case boulder:
			finalDamage = boulderDamage*mult;
			break;
		default:
			finalDamage = 0;
			break;
		}
		if(health - finalDamage < 0) {
			setDispensedItemQuantity((int)(health + maxHealth));
		} else {
			setDispensedItemQuantity((int)finalDamage);
		}
		health-=finalDamage;
		markDirty();
	}
	
	public void UpdateTileEntity() {
		if(health < 0) {
			world.destroyBlock(pos, false);
			world.removeTileEntity(pos);
		}
	}

	public Item getDispensedItem() {
		return dispensedItem;
	}

	public void setDispensedItem(Item item) {
		dispensedItem = item;
	}

	public int getDispensedItemQuantity() {
		return itemQuantity;
	}
	
	public void setDispensedItemQuantity(int quantity) {
		this.itemQuantity = quantity;
	}
	
	public enum damageType {
		generic (0),
		wood (1),
		boulder (2);
		
		public final int level;
		damageType(int level) {
			this.level = level;
		}		
	}
	
}
