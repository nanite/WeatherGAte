package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import com.unrealdinnerbone.weathergate.util.RangeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record SunInABlockAttachment(List<BlockPos> blockPosList) {

    public static SunInABlockAttachment of(List<BlockPos> data) {
        return new SunInABlockAttachment(new ArrayList<>(data));
    }
    public static final SunInABlockAttachment EMPTY = new SunInABlockAttachment(new ArrayList<>());
    public static final Codec<SunInABlockAttachment> CODEC = BlockPos.CODEC
            .listOf()
            .xmap(SunInABlockAttachment::of, SunInABlockAttachment::blockPosList);

    public static SunInABlockAttachment get(Level level) {
        return level.getData(WeatherGateRegistry.SUN_IN_A_BOX_ATTACHMENT.get());
    }

    public void save(Level level) {
        level.setData(WeatherGateRegistry.SUN_IN_A_BOX_ATTACHMENT.get(), this);
    }

    public static void addBlockPos(Level level, BlockPos blockPos) {
        SunInABlockAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.add(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos blockPos) {
        SunInABlockAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.remove(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static boolean isInRange(Level level, Vec3i blockPos) {
        for (BlockPos location : get(level).blockPosList()) {
            if(RangeUtils.isWithinRange(location, blockPos, 64)) {
                return true;
            }
        }
        return false;
    }

    public List<GlobalPos> toGlobalPos(ResourceKey<Level> level) {
        return blockPosList.stream().map(blockPos -> GlobalPos.of(level, blockPos)).collect(Collectors.toList());
    }
}
