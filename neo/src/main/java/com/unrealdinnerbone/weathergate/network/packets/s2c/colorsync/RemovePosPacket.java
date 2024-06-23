package com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RemovePosPacket(GlobalPos globalPos) implements CustomPacketPayload {

    public static final Type<RemovePosPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.rl("remove_pos_packet"));

    public static final StreamCodec<FriendlyByteBuf, RemovePosPacket> CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, RemovePosPacket::globalPos,
            RemovePosPacket::new);



    public static void handleRemovePosPacket(RemovePosPacket removePosPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            if(WeatherGateClient.BLOCK_COLORS.containsKey(removePosPacket.globalPos().dimension().location())) {
                WeatherGateClient.BLOCK_COLORS.get(removePosPacket.globalPos().dimension().location()).remove(removePosPacket.globalPos().pos());
                if(WeatherGateClient.isPosInRange(removePosPacket.globalPos().pos())) {
                    Minecraft.getInstance().levelRenderer.allChanged();
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
