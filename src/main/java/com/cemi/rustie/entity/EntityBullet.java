package com.cemi.rustie.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityBullet extends EntityThrowable {
	
	EntityLivingBase thrower;
	float damage = 1f;
	
    public EntityBullet(World worldIn)
    {
        super(worldIn);
    }

    public EntityBullet(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        thrower = throwerIn;
    }

    public EntityBullet(World worldIn, EntityLivingBase throwerIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        thrower = throwerIn;
    }
    protected void onHitBlock(IBlockState state, BlockPos pos, double x, double y, double z)
    {
    	if(!this.world.isRemote)
    		((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, (int) 8, 0.0, 0.0, 0.0, 0.05, Block.getStateId(state));
    }

    protected void onImpact(RayTraceResult result)
    {
    	if(result.entityHit == thrower) return;
    	
    	if(result.entityHit != null) if(thrower instanceof EntityPlayer) result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower), damage);
    	
    	if(result.getBlockPos() != null)
	        this.onHitBlock(world.getBlockState(result.getBlockPos()), result.getBlockPos(), result.hitVec.x, result.hitVec.y, result.hitVec.z);

        if (!this.world.isRemote)
        {
            this.setDead();
        }
        
    }
    
    @Override
    protected void setRotation(float yaw, float pitch) {
    	super.setRotation(yaw, pitch);
    }
    
    public void setRot(float yaw, float pitch) {
    	setRotation(yaw, pitch);
    }
    
    protected float getGravityVelocity()
    {
        return 0.01F;
    }
}
