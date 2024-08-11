
package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.level.attachments.SunInABlockAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.UpdateSunInABoxPosPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class SunInABoxBlock extends Block {

    public SunInABoxBlock() {
        super(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(5f).sound(SoundType.METAL));
    }

    @Override
    public void onPlace(BlockState newState, Level level, BlockPos blockPos, BlockState oldState, boolean pistonMoved) {
        if(level instanceof ServerLevel serverLevel) {
            SunInABlockAttachment.addBlockPos(serverLevel, blockPos);
            PacketDistributor.sendToPlayersInDimension(serverLevel, new UpdateSunInABoxPosPacket(new GlobalPos(level.dimension(), blockPos), UpdateSunInABoxPosPacket.UpdateType.ADD));
        }
        super.onPlace(newState, level, blockPos, oldState, pistonMoved);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if(!blockState.is(blockState2.getBlock()) && level instanceof ServerLevel serverLevel) {
            SunInABlockAttachment.removeBlockPos(serverLevel, blockPos);
            PacketDistributor.sendToPlayersInDimension(serverLevel, new UpdateSunInABoxPosPacket(new GlobalPos(level.dimension(), blockPos), UpdateSunInABoxPosPacket.UpdateType.REMOVE));
        }
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, Item.TooltipContext p_339606_, List<Component> components, TooltipFlag p_49819_) {
        components.add(Component.translatable("weathergate.sun_in_a_box.tooltip"));

    }
}
