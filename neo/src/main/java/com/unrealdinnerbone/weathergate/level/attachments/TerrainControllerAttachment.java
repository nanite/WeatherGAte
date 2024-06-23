package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.util.Type;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public record TerrainControllerAttachment(Map<BlockPos, Map<Type, Color4I>> data) {

    public static TerrainControllerAttachment of(List<Data> data) {
        Map<BlockPos, Map<Type, Color4I>> map = new HashMap<>();
        data.forEach(blockPos -> map.put(blockPos.blockPos(), blockPos.data()));
        return new TerrainControllerAttachment(map);
    }


    public List<Data> asList() {
        List<Data> dataList = new ArrayList<>();
        data.forEach((pos, map) -> dataList.add(Data.of(pos, map)));
        return dataList;
    }

    public record Data(BlockPos blockPos, Map<Type, Color4I> data) {

        public static Data of(BlockPos blockPos, Map<Type, Color4I> data) {
            return new Data(blockPos, new HashMap<>(data));
        }
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC
                        .fieldOf("pos")
                        .forGetter(Data::blockPos),
                Codec.unboundedMap(Type.CODEC, WeatherGateCodecs.COLOR4I_CODEC)
                        .fieldOf("data")
                        .forGetter(Data::data)
        ).apply(instance, Data::of));
    }
    public static final TerrainControllerAttachment EMPTY = new TerrainControllerAttachment(new HashMap<>());

    public static final Codec<TerrainControllerAttachment> CODEC = Data.CODEC.listOf().xmap(TerrainControllerAttachment::of, TerrainControllerAttachment::asList);

    public void save(Level level) {
        level.setData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get(), this);
    }

    public static TerrainControllerAttachment getAttachment(Level level) {
        return level.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.get());
    }

    public Map<Type, Color4I> getAttachmentData(BlockPos pos) {
        return data.get(pos);
    }

}
