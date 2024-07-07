package com.unrealdinnerbone.weathergate.network.packets.s2c;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record UpdateSunInABoxPosPacket(GlobalPos globalPos, UpdateType updateType) implements CustomPacketPayload {

    public static final Type<UpdateSunInABoxPosPacket> TYPE = new Type<>(WeatherGate.rl("update_sun_in_a_box_pos"));

    public static final StreamCodec<FriendlyByteBuf, UpdateSunInABoxPosPacket> CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, UpdateSunInABoxPosPacket::globalPos,
            UpdateType.CODEC, UpdateSunInABoxPosPacket::updateType,
            UpdateSunInABoxPosPacket::new);



    public static void handleUpdateSunInBoxPos(UpdateSunInABoxPosPacket removePosPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            GlobalPos globalPos = removePosPacket.globalPos();
            if(!WeatherGateClient.SUN_IN_BOX_LOCATIONS.containsKey(globalPos.dimension())) {
                WeatherGateClient.SUN_IN_BOX_LOCATIONS.put(globalPos.dimension(), new ArrayList<>());
            }
            if(removePosPacket.updateType() == UpdateType.ADD) {
                WeatherGateClient.SUN_IN_BOX_LOCATIONS.get(globalPos.dimension()).add(globalPos.pos());
            } else if(removePosPacket.updateType() == UpdateType.REMOVE) {
                WeatherGateClient.SUN_IN_BOX_LOCATIONS.get(globalPos.dimension()).remove(globalPos.pos());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public enum UpdateType {
        ADD,
        REMOVE;

        public static final StreamCodec<FriendlyByteBuf, UpdateType> CODEC = NeoForgeStreamCodecs.enumCodec(UpdateType.class);
    }
}
