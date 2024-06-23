package com.unrealdinnerbone.weathergate;

import com.unrealdinnerbone.trenzalore.api.platform.services.IRegistry;
import com.unrealdinnerbone.trenzalore.api.registry.Regeneration;
import com.unrealdinnerbone.trenzalore.api.registry.RegistryEntry;
import com.unrealdinnerbone.trenzalore.api.registry.RegistryObjects;
import com.unrealdinnerbone.trenzalore.lib.CreativeTabs;
import com.unrealdinnerbone.weathergate.block.TerrainControllerBlock;
import com.unrealdinnerbone.weathergate.level.attachments.SnowCatcherAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.block.SnowCatcherBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class WeatherGateRegistry implements IRegistry {

    private static final RegistryObjects<Block> BLOCKS = Regeneration.create(Registries.BLOCK);
    private static final RegistryObjects<Item> ITEMS = Regeneration.create(Registries.ITEM);
    private static final RegistryObjects<AttachmentType<?>> ATTACHMENT_TYPE = Regeneration.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);

    public static final RegistryEntry<AttachmentType<SnowCatcherAttachment>> SNOW_CATCHER_ATTACHMENT = ATTACHMENT_TYPE.register("snow_catcher", () ->
            AttachmentType.builder(() -> SnowCatcherAttachment.EMPTY).serialize(SnowCatcherAttachment.CODEC).build());

    public static final RegistryEntry<AttachmentType<TerrainControllerAttachment>> TERIANN_CONTROLLER_ATTACHMENT = ATTACHMENT_TYPE.register("terrain_controllers", () ->
            AttachmentType.builder(() -> TerrainControllerAttachment.EMPTY).serialize(TerrainControllerAttachment.CODEC).build());
    public static final RegistryEntry<Block> SNOW_CATCHER = BLOCKS.register("snow_catcher", SnowCatcherBlock::new);
    public static final RegistryEntry<Item> SNOW_CATCHER_ITEM = ITEMS.register("snow_catcher", () -> new BlockItem(SNOW_CATCHER.get(), new Item.Properties()));

    public static final RegistryEntry<Block> TERIANN_CONTROLLER = BLOCKS.register("terrain_controller", TerrainControllerBlock::new);

    public static final RegistryEntry<Item> TERIANN_CONTROLLER_ITEM = ITEMS.register("terrain_controller", () -> new BlockItem(TERIANN_CONTROLLER.get(), new Item.Properties()));



    @Override
    public void afterRegistered() {
        Regeneration.addItemsToCreateTab(CreativeTabs.FUNCTIONAL_BLOCKS, List.of(SNOW_CATCHER_ITEM, TERIANN_CONTROLLER_ITEM));
    }

    @Override
    public List<RegistryObjects<?>> getRegistryObjects() {
        return List.of(BLOCKS, ITEMS,  ATTACHMENT_TYPE);
    }

    @Override
    public String getModID() {
        return WeatherGate.MOD_ID;
    }


}
