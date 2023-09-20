package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.S2CPacketsForge;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("immersive_crafting")
public class ImmersiveCraftingForge {
    public ImmersiveCraftingForge() {
        MinecraftForge.EVENT_BUS.register(this);

        ICRecipeTypes.init();
        ICRecipeSerializers.init();
        ICConditionSerializers.init();
        ICMessages.registerPackets();

        S2CPackets.INSTANCE = new S2CPacketsForge();
    }

    @SubscribeEvent
    public void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new RecipeReloader());
    }

    @SubscribeEvent
    public void playerJoined(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            S2CPackets.INSTANCE.trySendRecipes(player);
        }
    }
}
