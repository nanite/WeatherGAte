package com.unrealdinnerbone.weathergate.mixin;

import com.unrealdinnerbone.weathergate.level.attachments.SnowCatcherAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLevel.class)
@Debug(export = true)
public class ServerLevelMixin {

    @Inject(method = "tickPrecipitation", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void tickWeatherSnow(BlockPos blockPos, CallbackInfo callbackInfo, BlockPos blockPos2) {
        ServerLevel serverLevel = (ServerLevel) (Object) this;
        if(SnowCatcherAttachment.isInRange(serverLevel, blockPos2)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "tickPrecipitation", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void tickWeatherIce(BlockPos blockPos, CallbackInfo callbackInfo, BlockPos blockPos2) {
        ServerLevel serverLevel = (ServerLevel) (Object) this;
        if(SnowCatcherAttachment.isInRange(serverLevel, blockPos2)) {
            callbackInfo.cancel();
        }
    }


}
