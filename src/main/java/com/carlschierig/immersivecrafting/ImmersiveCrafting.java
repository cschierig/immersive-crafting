package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import com.carlschierig.immersivecrafting.impl.network.ICMessages;
import com.carlschierig.immersivecrafting.impl.network.S2CPackets;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.S2CPlayChannelEvents;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmersiveCrafting implements ModInitializer {
    public static final String MODID = "immersive_crafting";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);
    //public static final Block ICON_BLOCK = new Block(BlockBehaviour.Properties.of());
    //public static final Item ICON_ITEM = new BlockItem(ICON_BLOCK, new Item.Properties());

    @Override
    public void onInitialize(ModContainer mod) {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new RecipeReloader());

        ICRecipeTypes.init();
        ICRecipeSerializers.init();
        ICConditionSerializers.init();

        S2CPlayChannelEvents.REGISTER.register(ICMessages::registerPlayer);
        ServerPlayConnectionEvents.DISCONNECT.register(ICMessages::unregisterPlayer);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> S2CPackets.trySendRecipes(handler.player));
    }
}
