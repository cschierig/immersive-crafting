package com.carlschierig.immersivecrafting.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateAccessor {
    @Accessor
    float getDestroySpeed();
}
