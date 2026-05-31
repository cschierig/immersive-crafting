package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.impl.FabricPlatformHelper;
import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.network.S2CPacketsFabric;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ClientboundPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class ImmersiveCrafting implements ModInitializer {
    //public static final Block ICON_BLOCK = new Block(BlockBehaviour.Properties.of());
    //public static final Item ICON_ITEM = new BlockItem(ICON_BLOCK, new Item.Properties());

    @Override
    public void onInitialize() {
        new FabricPlatformHelper();

        // resource loader
        var loaderId = Identifier.fromNamespaceAndPath(ICUtil.MODID, "recipe_reloader");
        var serverLoader = ResourceLoader.get(PackType.SERVER_DATA);
        serverLoader.registerReloadListener(loaderId, new RecipeReloader());
        serverLoader.addListenerOrdering(ResourceReloaderKeys.AFTER_VANILLA, loaderId);

        ImmersiveCraftingCommon.init();

        ICMessages.registerPayloadsClientbound();
        S2CPackets.INSTANCE = new S2CPacketsFabric();
        ClientboundPlayChannelEvents.REGISTER.register(ICMessages::registerPlayer);

        ServerPlayConnectionEvents.DISCONNECT.register(ICMessages::unregisterPlayer);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> S2CPackets.INSTANCE.trySendRecipes(handler.player));
    }
}
