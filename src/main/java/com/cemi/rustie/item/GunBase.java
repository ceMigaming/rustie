package com.cemi.rustie.item;

import com.cemi.rustie.client.RustieTabs;
import com.cemi.rustie.entity.EntityBullet;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class GunBase extends ItemBase {
	
	public GunBase(String name) {
		super(name);
        setCreativeTab(RustieTabs.WEAPONS);
        setMaxDamage(0);
    }

	@Override
	public boolean onEntitySwing(EntityLivingBase playerIn, ItemStack itemstack) {
		if(playerIn instanceof EntityPlayer) {
			if (!playerIn.world.isRemote) {
	        	EntityBullet entityBullet = new EntityBullet(playerIn.world, playerIn, playerIn.posX, playerIn.posY + (double)playerIn.getEyeHeight() - 0.5D, playerIn.posZ);
	        	entityBullet.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 1.0F);
	        	playerIn.world.spawnEntity(entityBullet);
	        }
	        playerIn.setPositionAndRotation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw+3f, playerIn.rotationPitch-3f);
		}
        
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		final Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		 
		if (slot == EntityEquipmentSlot.MAINHAND)
        {
			modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, ATTACK_SPEED_MODIFIER.toString(), 20, 0));
        }
		return modifiers;
	}
}
