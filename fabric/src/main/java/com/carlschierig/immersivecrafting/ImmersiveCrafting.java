package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.S2CPacketsFabric;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloaderFabric;
import com.carlschierig.immersivecrafting.impl.registry.ICRegistriesImplFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ImmersiveCrafting implements ModInitializer {
    //public static final Block ICON_BLOCK = new Block(BlockBehaviour.Properties.of());
    //public static final Item ICON_ITEM = new BlockItem(ICON_BLOCK, new Item.Properties());

    @Override
    public void onInitialize() {
        new ICRegistriesImplFabric();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new RecipeReloaderFabric());

        ImmersiveCraftingCommon.init();

        ICMessages.registerPayloadsS2C();
        new S2CPacketsFabric();
        S2CPlayChannelEvents.REGISTER.register(ICMessages::registerPlayer);

        ServerPlayConnectionEvents.DISCONNECT.register(ICMessages::unregisterPlayer);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> S2CPackets.INSTANCE.trySendRecipes(handler.player));
    }
}
