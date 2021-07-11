package com.cemi.rustie.utility;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class MathHelper {
	
	public static double getDistance(Entity entity, BlockPos pos) {
		double deltaX = entity.posX - pos.getX();
		double deltaY = entity.posY - pos.getY();
		double deltaZ = entity.posZ - pos.getZ();
			
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
	
	public static double getDistance(BlockPos pos1, BlockPos pos2) {
		double deltaX = pos2.getX() - pos1.getX();
		double deltaY = pos2.getY() - pos1.getY();
		double deltaZ = pos2.getZ() - pos1.getZ();
			
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
}
