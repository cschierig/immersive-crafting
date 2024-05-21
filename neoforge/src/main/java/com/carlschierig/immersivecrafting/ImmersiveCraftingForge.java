package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.S2CPacketsForge;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod("immersive_crafting")
public class ImmersiveCraftingForge {
    public ImmersiveCraftingForge() {
        NeoForge.EVENT_BUS.register(this);

        ImmersiveCraftingCommon.init();

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
