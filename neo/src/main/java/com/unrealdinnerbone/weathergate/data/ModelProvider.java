package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModelProvider extends BlockStateProvider {


    public ModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WeatherGate.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        standardBlock(WeatherGateRegistry.SNOW_CATCHER.get());
        standardBlock(WeatherGateRegistry.TERIANN_CONTROLLER.get());
    }

    private void standardBlock(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
}
