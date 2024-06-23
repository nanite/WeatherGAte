package com.unrealdinnerbone.weathergate.server;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.util.Type;
import net.minecraft.server.level.ServerLevel;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.RemovePosPacket;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class TerrainManager
{

    public static void addBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        Biome biome = level.getBiome(pos).value();
        Map<Type, Color4I> colorMap = new HashMap<>();
        for (Type type : Type.values()) {
            colorMap.put(type, type.getDefaultColor().apply(biome, pos));
        }
        attachment.data().put(pos, colorMap);
        attachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        attachment.data().remove(pos);
        attachment.save(level);
        if(level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersInDimension(serverLevel, new RemovePosPacket(GlobalPos.of(level.dimension(), pos)));
        }
    }

}

