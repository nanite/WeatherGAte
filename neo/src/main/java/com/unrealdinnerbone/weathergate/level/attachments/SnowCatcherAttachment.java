package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.util.RangeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record SnowCatcherAttachment(List<BlockPos> blockPosList) {

    public static SnowCatcherAttachment of(List<BlockPos> data) {
        return new SnowCatcherAttachment(new ArrayList<>(data));
    }
    public static final SnowCatcherAttachment EMPTY = new SnowCatcherAttachment(new ArrayList<>());
    public static final Codec<SnowCatcherAttachment> CODEC = BlockPos.CODEC
            .listOf()
            .xmap(SnowCatcherAttachment::of, SnowCatcherAttachment::blockPosList);

    public static SnowCatcherAttachment get(Level level) {
        return level.getData(WeatherGateRegistry.SNOW_CATCHER_ATTACHMENT.get());
    }

    public void save(Level level) {
        level.setData(WeatherGateRegistry.SNOW_CATCHER_ATTACHMENT.get(), this);
    }

    public static void addBlockPos(Level level, BlockPos blockPos) {
        SnowCatcherAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.add(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos blockPos) {
        SnowCatcherAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.remove(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static boolean isInRange(Level level, BlockPos blockPos) {
        for (BlockPos location : get(level).blockPosList()) {
            if(RangeUtils.isWithinRange(location, blockPos, 64)) {
                return true;
            }
        }
        return false;
    }
}
