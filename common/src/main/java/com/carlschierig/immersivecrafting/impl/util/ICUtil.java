package com.carlschierig.immersivecrafting.impl.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ICUtil {
    public static String MODID = "immersive_crafting";
    public static Logger LOG = LoggerFactory.getLogger(MODID);

    private ICUtil() {
    }

    public static ResourceLocation getId(String value) {
        return new ResourceLocation(MODID, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Stream<Holder<T>> getValues(TagKey<T> tag) {
        // simplified copy of the version in EmiUtil
        var registry = BuiltInRegistries.REGISTRY.get((ResourceKey) tag.registry());
        Optional<HolderSet.Named<T>> tagEntry = registry.getTag(tag);
        return tagEntry.map(HolderSet.ListBacked::stream).orElseGet(Stream::of);
    }
}
