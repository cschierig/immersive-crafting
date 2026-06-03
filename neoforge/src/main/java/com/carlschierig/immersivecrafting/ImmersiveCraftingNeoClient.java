package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.NeoPlatformHelper;
import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.S2CPacketsForge;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(value = "immersive_crafting", dist = Dist.CLIENT)
public class ImmersiveCraftingNeoClient {
    public ImmersiveCraftingNeoClient(IEventBus bus) {
        bus.register(this);
        ImmersiveCraftingClient.init();
    }

    @SubscribeEvent
    private void onClientSetup(FMLClientSetupEvent event) {
        ImmersiveCraftingClient.lateInit();
    }
}
