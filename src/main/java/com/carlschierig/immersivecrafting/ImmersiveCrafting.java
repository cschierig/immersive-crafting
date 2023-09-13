package com.carlschierig.immersivecrafting;

import com.carlschierig.immersivecrafting.api.predicate.condition.ICConditionSerializers;
import com.carlschierig.immersivecrafting.api.recipe.ICRecipeTypes;
import com.carlschierig.immersivecrafting.impl.recipe.ICRecipeSerializers;
import com.carlschierig.immersivecrafting.impl.recipe.RecipeReloader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmersiveCrafting implements ModInitializer {
    public static final String MODID = "immersive_crafting";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);
    public static final Block ICON_BLOCK = new Block(BlockBehaviour.Properties.of());
    public static final Item ICON_ITEM = new BlockItem(ICON_BLOCK, new Item.Properties());

    @Override
    public void onInitialize(ModContainer mod) {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new RecipeReloader());

        ICRecipeTypes.init();
        ICRecipeSerializers.init();
        ICConditionSerializers.init();

        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MODID, "icon"), ICON_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "icon"), ICON_ITEM);
    }
}
