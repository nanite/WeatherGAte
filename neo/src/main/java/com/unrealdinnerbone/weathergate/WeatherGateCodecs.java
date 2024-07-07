package com.unrealdinnerbone.weathergate;

import com.jcraft.jorbis.Block;
import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.util.Type;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class WeatherGateCodecs
{
    public static final Codec<Color4I> COLOR4I_CODEC = Codec.INT.xmap(Color4I::rgb, Color4I::rgb);

    public static final StreamCodec<FriendlyByteBuf, Color4I> COLOR4I_STREAM_CODEC = StreamCodec.of((byteBuf, color4I) -> byteBuf.writeInt(color4I.rgb()), byteBuf -> Color4I.rgb(byteBuf.readInt()));

    public static final StreamCodec<FriendlyByteBuf, Map<Type, Color4I>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new,
                    Type.STREAM_CODEC,
                    COLOR4I_STREAM_CODEC);


    public static final StreamCodec<FriendlyByteBuf, Map<BlockPos, Map<Type, Color4I>>> MAP_STREAM_CODEC_TWO =
            ByteBufCodecs.map(HashMap::new,
                    BlockPos.STREAM_CODEC,
                    MAP_STREAM_CODEC);

}
