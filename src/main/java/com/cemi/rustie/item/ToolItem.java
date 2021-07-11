package com.cemi.rustie.item;

import com.cemi.rustie.blocks.BoulderBlock;
import com.cemi.rustie.tileentity.TileEntityBreakable;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ToolItem extends ItemBase {

	float woodDamage, boulderDamage, damage, attackSpeed;

	boolean shouldSwing;

	double range = 3d;
	float lostFraction = 10.0f;

	public ToolItem(String name, int woodDamage, int boulderDamage, int damage, int attackSpeed) {
		super(name);
		this.woodDamage = woodDamage;
		this.boulderDamage = boulderDamage;
		this.damage = damage;
		this.attackSpeed = attackSpeed;
	}

	public void enableSwing(boolean b) {
		shouldSwing = b;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if (entityLiving instanceof EntityPlayer) {
			World world = entityLiving.world;
			if (!((EntityPlayer) entityLiving).getCooldownTracker().hasCooldown(stack.getItem())) {
				RayTraceResult result = rayTrace(world, (EntityPlayer) entityLiving, false);
				if (com.cemi.rustie.utility.MathHelper.getDistance(entityLiving, result.getBlockPos()) < range) {
					if (world.getBlockState(result.getBlockPos()).getBlock() instanceof BoulderBlock) {
						TileEntityBreakable te = (TileEntityBreakable) world.getTileEntity(result.getBlockPos());
						te.dealDamage(damage, woodDamage, boulderDamage, lostFraction);
						System.out.println(te.getHealth());
					}
				}
			}
		}
		return shouldSwing;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		final Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, ATTACK_SPEED_MODIFIER.toString(), attackSpeed - 4, 0));
		}
		return modifiers;
	}
}
