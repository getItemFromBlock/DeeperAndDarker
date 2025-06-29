package com.kyanite.deeperdarker.mixin;

import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("unused")
@Mixin(value = { ContainerOpenersCounter.class })
public class ContainerOpenersCounterMixin {
    @ModifyVariable(method = "getOpenCount", at = @At("STORE"), ordinal = 0)
    public AABB injected(AABB aabb) {
        return new AABB(-30000000, -2048, -30000000, 30000000, 2048, 30000000);
    }
}
