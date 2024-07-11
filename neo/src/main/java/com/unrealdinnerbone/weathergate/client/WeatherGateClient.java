package com.unrealdinnerbone.weathergate.client;

import com.unrealdinnerbone.weathergate.client.compact.SereneSeasonsCompact;
import com.unrealdinnerbone.weathergate.util.Type;
import com.unrealdinnerbone.weathergate.block.TerrainControllerBlock;
import com.unrealdinnerbone.weathergate.util.RangeUtils;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherGateClient
{
    public static final Map<ResourceKey<Level>, Map<BlockPos, Map<Type, Color4I>>> BLOCK_COLORS = new HashMap<>();
    public static final Map<ResourceKey<Level>, List<BlockPos>> SUN_IN_BOX_LOCATIONS = new HashMap<>();

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.LOWEST, WeatherGateClient::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if(ModList.get().isLoaded("sereneseasons")) {
                SereneSeasonsCompact.registerCompact();
                WeatherGateClient.injectResolvers(false);
            }
            WeatherGateClient.injectResolvers(true);
        });
    }


    public static void injectResolvers(boolean basic) {
        if(basic) {
            BiomeColors.GRASS_COLOR_RESOLVER = createForType(BiomeColors.GRASS_COLOR_RESOLVER, Type.GRASS);
            BiomeColors.FOLIAGE_COLOR_RESOLVER = createForType(BiomeColors.FOLIAGE_COLOR_RESOLVER, Type.FOLIAGE);
        }
        BiomeColors.WATER_COLOR_RESOLVER = createForType(BiomeColors.WATER_COLOR_RESOLVER, Type.WATER);
    }


    public static ColorResolver createForType(ColorResolver minecraftResolver, Type type) {
        return (biome, x, z) -> {
            Color4I colorAtLocation = getColorAtLocation((int) x, (int) z, type);
            if(colorAtLocation != null) {
                return colorAtLocation.rgb();
            }else {
                return minecraftResolver.getColor(biome, x, z);
            }
        };
    }

    public static Color4I getColorAtLocation(int x, int z, Type type) {
        Player player = Minecraft.getInstance().player;
        ResourceKey<Level> location = player.level().dimension();
        Map<BlockPos, Map<Type, Color4I>> orDefault = BLOCK_COLORS.get(location);
        if(orDefault != null) {
            for (Map.Entry<BlockPos, Map<Type, Color4I>> blockPosMapEntry : orDefault.entrySet()) {
                BlockPos key = blockPosMapEntry.getKey();
                if(RangeUtils.isWithinRange(key.getX(), key.getZ(), x, z, TerrainControllerBlock.RANGE)) {
                    return blockPosMapEntry.getValue().get(type);
                }
            }
        }
        return null;
    }


    public static boolean isPosInRange(BlockPos blockPos) {
        Vec3 position = Minecraft.getInstance().player.position();
        int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
        int x = (int) position.x;
        int z = (int) position.z;
        return RangeUtils.isWithinRange(blockPos.getX(), blockPos.getZ(), x, z, renderDistance);
    }
}
