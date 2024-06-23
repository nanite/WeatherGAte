package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.util.Type;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import com.unrealdinnerbone.weathergate.server.TerrainManager;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public class TerrainControllerBlock extends Block{

    public static final int RANGE = 64;
    public TerrainControllerBlock() {
        super(Properties.of().mapColor(MapColor.STONE).strength(5.0F, 6.0F).sound(SoundType.STONE));
    }
//

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
            Map<Type, Color4I> data = attachment.getAttachmentData(blockPos);
            if(data != null) {
                PacketDistributor.sendToPlayer(serverPlayer, new OpenTerrainControllerPacket(blockPos, data));
            }else {
                serverPlayer.sendSystemMessage(Component.translatable("block.weathergate.terrain_controller.no_data").withStyle(ChatFormatting.RED), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onPlace(BlockState newState, Level level, BlockPos blockPos, BlockState oldState, boolean pistonMoved) {
        if(level instanceof ServerLevel serverLevel) {
            TerrainManager.addBlockPos(serverLevel, blockPos);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if(!blockState.is(blockState2.getBlock()) && level instanceof ServerLevel serverLevel) {
            TerrainManager.removeBlockPos(serverLevel, blockPos);
        }
    }
}
