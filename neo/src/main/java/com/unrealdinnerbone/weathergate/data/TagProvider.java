package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagProvider {

    public static class Blocks extends BlockTagsProvider {

        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, WeatherGate.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(WeatherGateRegistry.SNOW_CATCHER.get())
                    .add(WeatherGateRegistry.TERIANN_CONTROLLER.get())
                    .add(WeatherGateRegistry.SUN_IN_A_BOX.get());
        }
    }
}
