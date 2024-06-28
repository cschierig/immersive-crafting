package com.carlschierig.immersivecrafting.impl.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ICUtil {
    public static String MODID = "immersive_crafting";
    public static Logger LOG = LoggerFactory.getLogger(MODID);

    public static final StreamCodec<ByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            string -> {
                try {
                    return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), string, true).blockState();
                } catch (CommandSyntaxException e) {
                    LOG.error("Error decoding block state.", e);
                    throw new RuntimeException();
                }
            },
            BlockStateParser::serialize
    );

    private ICUtil() {
    }

    public static ResourceLocation getId(String value) {
        return ResourceLocation.tryBuild(MODID, value);
    }

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(String value) {
        return new CustomPacketPayload.Type<>(getId(value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Stream<Holder<T>> getValues(TagKey<T> tag) {
        // simplified copy of the version in EmiUtil
        var registry = BuiltInRegistries.REGISTRY.get((ResourceKey) tag.registry());
        Optional<HolderSet.Named<T>> tagEntry = registry.getTag(tag);
        return tagEntry.map(HolderSet.ListBacked::stream).orElseGet(Stream::of);
    }
}
