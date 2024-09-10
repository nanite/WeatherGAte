package com.unrealdinnerbone.weathergate.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WeatherGateData {

    public static void onData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new ModelProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new RecipeProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(event.includeClient(), new TagProvider.Blocks(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), create(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(event.includeServer(), new LangProvider(event.getGenerator().getPackOutput()));
    }

    public static net.minecraft.data.loot.LootTableProvider create(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new net.minecraft.data.loot.LootTableProvider(
                pOutput,
                Collections.emptySet(),
                List.of(
                        new net.minecraft.data.loot.LootTableProvider.SubProviderEntry(LootTableProvider::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider
        );
    }

}
