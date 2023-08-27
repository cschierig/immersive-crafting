package com.carlschierig.immersive_crafting;

import com.carlschierig.immersive_crafting.crafting.ICRecipeManager;
import com.carlschierig.immersive_crafting.crafting.ICRecipeSerializers;
import com.carlschierig.immersive_crafting.crafting.ICRecipeTypes;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmersiveCrafting implements ModInitializer {
	public static final String MODID = "immersive_crafting";
	public static final Logger LOG = LoggerFactory.getLogger(MODID);
	public static final ICRecipeManager RECIPE_MANAGER = new ICRecipeManager();

	@Override
	public void onInitialize(ModContainer mod) {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloader(RECIPE_MANAGER);

		ICRecipeTypes.init();
		ICRecipeSerializers.init();
	}
}
