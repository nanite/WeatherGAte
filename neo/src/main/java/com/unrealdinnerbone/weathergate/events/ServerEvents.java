package com.unrealdinnerbone.weathergate.events;

import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.SyncColorsPacket;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ServerEvents
{

    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();
            TerrainControllerAttachment data = level.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get());
            PacketDistributor.sendToPlayer(serverPlayer, new SyncColorsPacket(level.dimension().location(), data.data()));
        }
    }

    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ResourceKey<Level> level = event.getTo();
            ServerLevel level1 = serverPlayer.getServer().getLevel(level);
            if(level1 != null) {
                TerrainControllerAttachment data = level1.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncColorsPacket(level.location(), data.data()));
            }

        }
    }

    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if(event.getEntity().level().isClientSide()) {
            WeatherGateClient.BLOCK_COLORS.clear();
        }
    }
}
