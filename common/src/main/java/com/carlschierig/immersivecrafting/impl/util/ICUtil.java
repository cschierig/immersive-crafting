package com.carlschierig.immersivecrafting.impl.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@ApiStatus.Internal
public final class ICUtil {
    public static String MODID = "immersive_crafting";
    public static Logger LOG = LoggerFactory.getLogger(MODID);

    @Nullable
    private static Function<ItemStack, List<Component>> tooltipFunction;

    private ICUtil() {
    }

    public static Identifier getId(String value) {
        return Identifier.fromNamespaceAndPath(MODID, value);
    }

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(String value) {
        return new CustomPacketPayload.Type<>(getId(value));
    }

    public static List<Component> getTooltipFromItem(ItemStack stack) {
        if (tooltipFunction == null) {
            return new ArrayList<>();
        }
        return tooltipFunction.apply(stack);
    }

    public static void setTooltipFunction(@Nullable Function<ItemStack, List<Component>> tooltipFunction) {
        ICUtil.tooltipFunction = tooltipFunction;
    }

    //    @SuppressWarnings({"unchecked", "rawtypes"})
//    public static <T> Stream<Holder<T>> getValues(TagKey<T> tag) {
//        // simplified copy of the version in EmiUtil
//        var registry = BuiltInRegistries.REGISTRY.get((ResourceKey) tag.registry());
//        Optional<HolderSet.Named<T>> tagEntry = registry.map(.getTag(tag);
//        return tagEntry.map(HolderSet.ListBacked::stream).orElseGet(Stream::of);
//    }
}
