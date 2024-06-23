package com.unrealdinnerbone.weathergate.network.packets.c2s;

import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.UpdateColorPacket;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public record UpdateControllerPacket(GlobalPos globalPos, Map<com.unrealdinnerbone.weathergate.util.Type, Color4I> updateMap) implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<UpdateControllerPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.rl("update_controller_packet"));


    public static final StreamCodec<FriendlyByteBuf, UpdateControllerPacket> CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, UpdateControllerPacket::globalPos,
            WeatherGateCodecs.MAP_CODEC, UpdateControllerPacket::updateMap,
            UpdateControllerPacket::new);

    public static void handleUpdateColorPacket(UpdateControllerPacket updateColorPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level levelO = context.player().level();
            if(levelO instanceof ServerLevel serverLevel){
                TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(serverLevel);
                Map<com.unrealdinnerbone.weathergate.util.Type, Color4I> map = attachment.data().get(updateColorPacket.globalPos().pos());
                if(map != null) {
                    map.putAll(updateColorPacket.updateMap());
                    attachment.data().put(updateColorPacket.globalPos().pos(), map);
                    attachment.save(serverLevel);
                    PacketDistributor.sendToPlayersInDimension(serverLevel, new UpdateColorPacket(updateColorPacket.globalPos(), updateColorPacket.updateMap()));
                }else {
                    //Todo Yell?
                }
            }else {
                //Todo Yell?
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
