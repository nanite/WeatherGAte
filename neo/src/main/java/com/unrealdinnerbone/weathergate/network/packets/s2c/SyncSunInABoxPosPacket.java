package com.unrealdinnerbone.weathergate.network.packets.s2c;

import com.jcraft.jorbis.Block;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SyncSunInABoxPosPacket(ResourceKey<Level> levelResourceKey, List<BlockPos> globalPos) implements CustomPacketPayload {

    public static final Type<SyncSunInABoxPosPacket> TYPE = new Type<>(WeatherGate.rl("sync_sun_in_a_box"));

    public static final StreamCodec<FriendlyByteBuf, SyncSunInABoxPosPacket> CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), SyncSunInABoxPosPacket::levelResourceKey,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncSunInABoxPosPacket::globalPos,
            SyncSunInABoxPosPacket::new);



    public static void handleUpdateSunInBoxPos(SyncSunInABoxPosPacket removePosPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            WeatherGateClient.SUN_IN_BOX_LOCATIONS.remove(removePosPacket.levelResourceKey);
            WeatherGateClient.SUN_IN_BOX_LOCATIONS.put(removePosPacket.levelResourceKey, removePosPacket.globalPos);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


}
