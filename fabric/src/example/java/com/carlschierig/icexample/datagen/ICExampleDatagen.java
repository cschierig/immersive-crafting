package com.carlschierig.icexample.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ICExampleDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        // register the immersive crafting recipe provider as you would with any other provider.
        pack.addProvider(ICXRecipeProvider::new);
    }
}
