package com.cemi.rustie.item;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.client.RustieTabs;
import com.cemi.rustie.entity.EntityBullet;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GunBase extends ItemBase {
	
	public GunBase(String name) {
		super(name);
        setCreativeTab(RustieTabs.WEAPONS);
        setMaxDamage(0);
    }

	@Override
	public boolean onEntitySwing(EntityLivingBase playerIn, ItemStack itemstack) {
		if(playerIn instanceof EntityPlayer) {
			//if(!((EntityPlayer)playerIn).capabilities.isCreativeMode) itemstack.shrink(1);
	        //worldIn.playSound(playerIn, playerIn.getPosition(), new SoundEvent(new ResourceLocation(Rustie.MODID, "test")), SoundCategory.PLAYERS, 0.5F, 1.0F); 
	        if (!playerIn.world.isRemote) {
	        	EntityBullet entityBullet = new EntityBullet(playerIn.world, playerIn.posX + playerIn.getLookVec().x * 1.5, playerIn.posY + (double)playerIn.getEyeHeight() - 0.5D, playerIn.posZ + playerIn.getLookVec().z * 1.5);
	        	entityBullet.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.5F, 1.0F);
	        	//entityBullet.rotationPitch = 180F;
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
