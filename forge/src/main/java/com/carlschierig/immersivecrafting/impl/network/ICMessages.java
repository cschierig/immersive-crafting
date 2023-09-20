package com.carlschierig.immersivecrafting.impl.network;

import com.carlschierig.immersivecrafting.api.serialization.ICByteBufHelper;
import com.carlschierig.immersivecrafting.impl.util.ICByteBufHelperImpl;
import com.carlschierig.immersivecrafting.impl.util.ICUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ICMessages {
    public static final ResourceLocation UPDATE = ICUtil.getId("update_recipes");
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            UPDATE,
            () -> PROTOCOL_VERSION,
            ICMessages::checkVersion,
            ICMessages::checkVersion
    );

    private static boolean checkVersion(String version) {
        return PROTOCOL_VERSION.equals(version)
                || version.equals(NetworkRegistry.ABSENT.version())
                || version.equals(NetworkRegistry.ACCEPTVANILLA);
    }

    public static void registerPackets() {
        int id = 0;
        CHANNEL.registerMessage(id++, RecipeMessage.class, ICMessages::encodeRecipes, ICMessages::decodeRecipes, ICMessages::handle);
    }

    private static void encodeRecipes(RecipeMessage recipes, FriendlyByteBuf buf) {
        ICByteBufHelperImpl.writeList(buf, recipes.recipes(), ICByteBufHelper::writeICRecipe);
    }

    private static RecipeMessage decodeRecipes(FriendlyByteBuf buf) {
        return new RecipeMessage(ICByteBufHelperImpl.readList(buf, ICByteBufHelper::readICRecipe));
    }

    private static void handle(RecipeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketReciever.receiveRecipes(message.recipes(), ctx))
        );
        ctx.get().setPacketHandled(true);
    }
}
