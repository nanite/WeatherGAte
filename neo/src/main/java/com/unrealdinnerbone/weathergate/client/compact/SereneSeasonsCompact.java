package com.unrealdinnerbone.weathergate.client.compact;

import sereneseasons.season.SeasonColorHandlers;

import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import com.unrealdinnerbone.weathergate.util.Type;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;


public class SereneSeasonsCompact
{
    public static void registerCompact() {
        SeasonColorHandlers.registerResolverOverride(SeasonColorHandlers.ResolverType.GRASS, new TypedColorOverride(Type.GRASS));
        SeasonColorHandlers.registerResolverOverride(SeasonColorHandlers.ResolverType.FOLIAGE, new TypedColorOverride(Type.FOLIAGE));
    }


    public record TypedColorOverride(Type type) implements SeasonColorHandlers.ColorOverride {

        @Override
        public int apply(int originalColor, int seasonalColor, int currentColor, Holder<Biome> biome, double x, double z) {
            Color4I colorAtLocation = WeatherGateClient.getColorAtLocation((int) x, (int) z, Type.GRASS);
            if(colorAtLocation != null) {
                return colorAtLocation.rgb();
            }
            return originalColor;
        }
    }
}
