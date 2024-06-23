package com.unrealdinnerbone.weathergate;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.util.Type;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;

public class WeatherGateCodecs
{
    public static final Codec<Color4I> COLOR4I_CODEC = Codec.INT.xmap(Color4I::rgb, Color4I::rgb);


    public static StreamCodec<FriendlyByteBuf, Map<Type, Color4I>> MAP_CODEC = new StreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf friendlyByteBuf, Map<Type, Color4I> map) {
            friendlyByteBuf.writeMap(map, FriendlyByteBuf::writeEnum, (byteBuf, color4I) -> byteBuf.writeInt(color4I.rgb()));

        }

        @Override
        public Map<Type, Color4I> decode(FriendlyByteBuf buffer) {
            return buffer.readMap(byteBuf -> byteBuf.readEnum(Type.class), byteBuf -> Color4I.rgb(byteBuf.readInt()));
        }
    };


    public static StreamCodec<FriendlyByteBuf, Map<BlockPos, Map<Type, Color4I>>> MAP_CODEC_TW = new StreamCodec<>() {


        @Override
        public void encode(FriendlyByteBuf buffer, Map<BlockPos, Map<Type, Color4I>> data) {
            buffer.writeMap(data, BlockPos.STREAM_CODEC, (byteBuf, typeColor4IMap) -> byteBuf.writeMap(typeColor4IMap, FriendlyByteBuf::writeEnum, (byteBuf1, color4I) -> byteBuf1.writeInt(color4I.rgb())));

        }

        @Override
        public Map<BlockPos, Map<Type, Color4I>> decode(FriendlyByteBuf buffer) {
            return buffer.readMap(BlockPos.STREAM_CODEC, byteBuf -> byteBuf.readMap(byteBuf1 -> byteBuf1.readEnum(Type.class), byteBuf1 -> Color4I.rgb(byteBuf1.readInt())));

        }
    };
}
