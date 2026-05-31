package com.carlschierig.immersivecrafting.impl.util;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public final class ICUtil {
    public static String MODID = "immersive_crafting";
    public static Logger LOG = LoggerFactory.getLogger(MODID);

    private ICUtil() {
    }

    public static Identifier getId(String value) {
        return Identifier.tryBuild(MODID, value);
    }

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(String value) {
        return new CustomPacketPayload.Type<>(Identifier.tryBuild(MODID, value));
    }

//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public static <T> Stream<Holder<T>> getValues(TagKey<T> tag) {
//        // simplified copy of the version in EmiUtil
//        var registry = BuiltInRegistries.REGISTRY.get((ResourceKey) tag.registry());
//        Optional<HolderSet.Named<T>> tagEntry = registry.map(.getTag(tag);
//        return tagEntry.map(HolderSet.ListBacked::stream).orElseGet(Stream::of);
//    }
}
