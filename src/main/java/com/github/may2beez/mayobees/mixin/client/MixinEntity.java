package com.github.may2beez.mayobees.mixin.client;

import com.github.may2beez.mayobees.pathfinder.FlyPathFinderExecutor;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Entity.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntity{
    @Shadow
    public float rotationYaw;

    @Shadow
    public double motionX;

    @Shadow
    public double motionZ;

    /**
     * @author May2Bee
     * @reason Yes
     */
    @Overwrite
    public void moveFlying(float strafe, float forward, float friction) {
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe *= f;
            forward *= f;
            float yaw = FlyPathFinderExecutor.getInstance().isPathing() && FlyPathFinderExecutor.getInstance().getNeededYaw() != Integer.MIN_VALUE ? FlyPathFinderExecutor.getInstance().getNeededYaw() : this.rotationYaw;
            float f1 = MathHelper.sin(yaw * 3.1415927F / 180.0F);
            float f2 = MathHelper.cos(yaw * 3.1415927F / 180.0F);
            this.motionX += strafe * f2 - forward * f1;
            this.motionZ += forward * f2 + strafe * f1;
        }
    }

}
