package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.level.attachments.SnowCatcherAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class SnowCatcherBlock extends Block {

    public SnowCatcherBlock() {
        super(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(5f).sound(SoundType.SNOW));
    }


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos blockPos, BlockState blockState, boolean update) {
        if(level instanceof ServerLevel serverLevel) {
            SnowCatcherAttachment.addBlockPos(serverLevel, blockPos);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if(!blockState.is(blockState2.getBlock()) && level instanceof ServerLevel serverLevel) {
            SnowCatcherAttachment.removeBlockPos(serverLevel, blockPos);
        }
    }

}
