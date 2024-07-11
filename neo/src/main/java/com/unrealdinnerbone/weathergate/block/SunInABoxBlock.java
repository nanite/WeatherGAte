
package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.level.attachments.SunInABlockAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.UpdateSunInABoxPosPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class SunInABoxBlock extends Block {

    public SunInABoxBlock() {
        super(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(5f).sound(SoundType.METAL));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(level instanceof ServerLevel serverLevel) {
            SunInABlockAttachment.addBlockPos(serverLevel, blockPos);
            PacketDistributor.sendToPlayersInDimension(serverLevel, new UpdateSunInABoxPosPacket(new GlobalPos(level.dimension(), blockPos), UpdateSunInABoxPosPacket.UpdateType.ADD));
        }
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if(!blockState.is(blockState2.getBlock()) && level instanceof ServerLevel serverLevel) {
            SunInABlockAttachment.removeBlockPos(serverLevel, blockPos);
            PacketDistributor.sendToPlayersInDimension(serverLevel, new UpdateSunInABoxPosPacket(new GlobalPos(level.dimension(), blockPos), UpdateSunInABoxPosPacket.UpdateType.REMOVE));

        }
    }

}
