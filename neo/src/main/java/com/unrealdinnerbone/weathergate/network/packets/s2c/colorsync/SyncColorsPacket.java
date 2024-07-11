package com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record SyncColorsPacket(ResourceKey<Level> levelResourceKey, Map<BlockPos, Map<com.unrealdinnerbone.weathergate.util.Type, Color4I>> data) implements CustomPacketPayload {

    public static final Type<SyncColorsPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.rl("sync_colors_packet"));

    public static final StreamCodec<FriendlyByteBuf, SyncColorsPacket> CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), SyncColorsPacket::levelResourceKey,
            WeatherGateCodecs.MAP_STREAM_CODEC_TWO, SyncColorsPacket::data,
            SyncColorsPacket::new);


    public static void handleSyncColorsPacket(SyncColorsPacket syncColorsPacket, IPayloadContext context) {
        context.enqueueWork(() -> WeatherGateClient.BLOCK_COLORS.put(syncColorsPacket.levelResourceKey(), syncColorsPacket.data()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
