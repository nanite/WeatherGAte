package com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync;

import com.mojang.logging.LogUtils;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public record UpdateColorPacket(GlobalPos globalPos, Map<com.unrealdinnerbone.weathergate.util.Type, Color4I> updateMap) implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Type<UpdateColorPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.rl("update_color_packet"));

    public static final StreamCodec<FriendlyByteBuf, UpdateColorPacket> CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, UpdateColorPacket::globalPos,
            WeatherGateCodecs.MAP_STREAM_CODEC, UpdateColorPacket::updateMap,
            UpdateColorPacket::new);




    public static void handleUpdateColorPacket(UpdateColorPacket updateColorPacket, IPayloadContext context) {
        ResourceLocation location = updateColorPacket.globalPos().dimension().location();
        if(WeatherGateClient.BLOCK_COLORS.containsKey(location)) {
            Map<BlockPos, Map<com.unrealdinnerbone.weathergate.util.Type, Color4I>> blockPosMapMap = WeatherGateClient.BLOCK_COLORS.get(location);
            if(blockPosMapMap.containsKey(updateColorPacket.globalPos().pos())) {
                blockPosMapMap.get(updateColorPacket.globalPos().pos()).putAll(updateColorPacket.updateMap());
            }else {
                blockPosMapMap.put(updateColorPacket.globalPos().pos(), new HashMap<>(updateColorPacket.updateMap()));
            }
            context.enqueueWork(() -> {
                if(WeatherGateClient.isPosInRange(updateColorPacket.globalPos().pos())) {
                    Minecraft.getInstance().levelRenderer.allChanged();
                }
            });
        }else {
            LOGGER.error("Failed to find block color map for {}", location);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
