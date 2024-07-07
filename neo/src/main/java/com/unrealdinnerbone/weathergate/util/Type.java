package com.unrealdinnerbone.weathergate.util;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public enum Type implements StringRepresentable {
    GRASS("grass", ItemIcon.getItemIcon(Items.GRASS_BLOCK), (biome, blockPos) -> {
        return biome.getSpecialEffects().getGrassColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN);
    }),
    FOLIAGE("foliage", ItemIcon.getItemIcon(Items.OAK_LEAVES), (biome, blockPos) -> {
        return biome.getSpecialEffects().getFoliageColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN);
    }),
    WATER("water", ItemIcon.getItemIcon(Items.WATER_BUCKET), (biome, blockPos) -> {
        return Color4I.rgb(biome.getWaterColor());
    }),
//    WATER_FOG("water_fog", (biome, blockPos) -> Color4I.rgb(biome.getWaterFogColor())),
//    FOG("fog", (biome, blockPos) -> Color4I.rgb(biome.getFogColor())),

//    SKY("sky", (biome, blockPos) -> Color4I.rgb(biome.getSkyColor()));


    ;

    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

    public static final StreamCodec<FriendlyByteBuf, Type> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Type.class);


    private final BiFunction<Biome, BlockPos, Color4I> defaultColor;
    private final String name;
    private final Icon icon;

    Type(String name, Icon icon, BiFunction<Biome, BlockPos, Color4I> defaultColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.icon = icon;
    }


    public String getLangKey() {
        return "weathergate.type." + name;
    }


    public Component getComponent() {
        return Component.translatable(getLangKey());
    }

    public Icon getIcon() {
        return icon;
    }

    public BiFunction<Biome, BlockPos, Color4I> getDefaultColor() {
        return defaultColor;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }
}
