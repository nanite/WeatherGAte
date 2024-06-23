package com.unrealdinnerbone.weathergate.util;

import net.minecraft.core.BlockPos;

public class RangeUtils
{
    public static boolean isWithinRange(BlockPos pos1, BlockPos pos2, int range) {
        return isWithinRange(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), range);
    }

    public static boolean isWithinRange(int x1, int z1, int x2, int z2, int range) {
        int dx = Math.abs(x1 - x2);
        int dz = Math.abs(z1 - z2);
        return dx <= range && dz <= range;
    }

}
