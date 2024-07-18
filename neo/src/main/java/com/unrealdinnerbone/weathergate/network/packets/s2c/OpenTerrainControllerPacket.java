package com.unrealdinnerbone.weathergate.network.packets.s2c;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.screen.TerrainControllerScreen2;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


import java.util.Map;

public record OpenTerrainControllerPacket(BlockPos blockPos, Map<com.unrealdinnerbone.weathergate.util.Type, Color4I> map) implements CustomPacketPayload {

    public static final Type<OpenTerrainControllerPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.rl("open_terrain_controller_packet"));


    public static final StreamCodec<FriendlyByteBuf, OpenTerrainControllerPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenTerrainControllerPacket::blockPos,
            WeatherGateCodecs.MAP_STREAM_CODEC, OpenTerrainControllerPacket::map,
            OpenTerrainControllerPacket::new);


    public static void handleOpenTerrainControllerPacket(OpenTerrainControllerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> new TerrainControllerScreen2(packet.blockPos(), packet.map()).openGui());
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
