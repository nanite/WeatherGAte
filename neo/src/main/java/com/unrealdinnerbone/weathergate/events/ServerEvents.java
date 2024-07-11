package com.unrealdinnerbone.weathergate.events;

import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import com.unrealdinnerbone.weathergate.level.attachments.SunInABlockAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.SyncSunInABoxPosPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.UpdateSunInABoxPosPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.SyncColorsPacket;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ServerEvents
{

    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();
            TerrainControllerAttachment data = level.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get());
            PacketDistributor.sendToPlayer(serverPlayer, new SyncColorsPacket(level.dimension(), data.data()));
            SunInABlockAttachment sunInABlockAttachment = level.getData(WeatherGateRegistry.SUN_IN_A_BOX_ATTACHMENT.get());
            PacketDistributor.sendToPlayer(serverPlayer, new SyncSunInABoxPosPacket(level.dimension(), sunInABlockAttachment.blockPosList()));
        }
    }

    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ResourceKey<Level> level = event.getTo();
            ServerLevel level1 = serverPlayer.getServer().getLevel(level);
            if(level1 != null) {
                TerrainControllerAttachment data = level1.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncColorsPacket(level, data.data()));
                SunInABlockAttachment sunInABlockAttachment = level1.getData(WeatherGateRegistry.SUN_IN_A_BOX_ATTACHMENT.get());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncSunInABoxPosPacket(level1.dimension(), sunInABlockAttachment.blockPosList()));
            }

        }
    }

    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if(event.getEntity().level().isClientSide()) {
            WeatherGateClient.BLOCK_COLORS.clear();
            WeatherGateClient.SUN_IN_BOX_LOCATIONS.clear();
        }
    }
}
