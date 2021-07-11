package com.cemi.rustie.blocks;

import java.lang.reflect.Field;

import com.cemi.rustie.item.RustieItems;
import com.cemi.rustie.item.ToolItem;
import com.cemi.rustie.tileentity.TileEntityBreakable;
import com.cemi.rustie.tileentity.TileEntityBreakable.damageType;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BoulderBlock extends TileEntityBase<TileEntityBreakable> {

	/// iron/salvaged pick / jackhammer -> boulder = 10 hits // (crits) = 6 hits
	/// stone pick -> boulder = 25 hits // (crits) = 12 hits
	/// rock -> boulder = 50 hits // (crits) = 18 hits

	/// salvaged axe -> tree = 10 hits // (crits) = 10 hits?
	/// stone axe -> tree 40 hits // (crits) = 17 hits?
	/// rock -> tree = 25 hits // (crits) = 25 hits
	
	

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public BoulderBlock() {
		super(Material.ROCK, "boulder");
		setHardness(50f);
		setResistance(2000f);
	}

	@Override
	public BoulderBlock setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public Class<TileEntityBreakable> getTileEntityClass() {
		return TileEntityBreakable.class;
	}

	@Override
	public TileEntityBreakable createTileEntity(World world, IBlockState state) {
		return new TileEntityBreakable(50, damageType.generic);
	};

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		/*
		Item item = player.getHeldItem(EnumHand.MAIN_HAND).getItem();
		if (!world.isRemote) {
			TileEntityBoulder tile = getTileEntity(world, pos);
			if (item instanceof ToolItem) {
				if (!player.getCooldownTracker().hasCooldown(item)) {
					player.getCooldownTracker().setCooldown(item, 20 * 2);
					tile.decrementDamage();
					if (tile.getHealth() < 0) {
						world.removeTileEntity(pos);
						world.destroyBlock(pos, false);
					}
				} else {
					player.swingProgressInt = 0;
					ItemRenderer IR = Minecraft.getMinecraft().entityRenderer.itemRenderer;
					try {
						float a1 = 1F;
						{
							Field field = ItemRenderer.class.getDeclaredField("equippedProgressMainHand");
							field.setAccessible(true);
							field.setFloat(IR, a1);
						}
						{
							Field field = ItemRenderer.class.getDeclaredField("prevEquippedProgressMainHand");
							field.setAccessible(true);
							field.setFloat(IR, a1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				player.sendMessage(new TextComponentString("Hp: " + tile.getHealth()));
			}
		}

		if (item instanceof ToolItem) {
			if (!player.getCooldownTracker().hasCooldown(item)) {
				((ToolItem) item).enableSwing(false);
			} else {
				((ToolItem) item).enableSwing(true);
			}

		}
		*/
		return true;
	}

}
