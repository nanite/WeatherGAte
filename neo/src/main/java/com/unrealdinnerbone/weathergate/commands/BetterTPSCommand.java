//package weathergate.commands;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.HoverEvent;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.TimeUtil;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.dimension.DimensionType;
//
//import java.awt.Color;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class BetterTPSCommand {
//
//    private static final DecimalFormat TIME_FORMATTER = new DecimalFormat("########0.000");
//    private static final long[] UNLOADED = new long[] { 0 };
//
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(
//                Commands.literal("tps")
//                        .executes(ctx -> {
//                            Map<ResourceLocation, java.awt.Component> messages = new HashMap<>();
//                            for (ServerLevel dim : ctx.getSource().getServer().getAllLevels()) {
//                                messages.put(dim.dimension().location(), createMessage(ctx.getSource(), dim));
//                            }
//
//                            List<java.awt.Component> collect = messages.entrySet().stream()
//                                    .sorted((o1, o2) -> compareResourceLocation(o1.getKey(), o2.getKey()))
//                                    .map(Map.Entry::getValue)
//                                    .collect(Collectors.toCollection(ArrayList::new));
//
//                            MutableComponent overall = Component.literal("Overall");
//                            collect.add(createMessage(ctx.getSource().getServer().getTickTimesNanos(), overall, overall));
//                            collect.forEach(value -> ctx.getSource().sendSuccess(() -> value, false));
//                            return 0;
//                        }));
//    }
//
//    private static java.awt.Component createHoverMessage(List<ServerLevel> levels) {
//        MutableComponent hover = java.awt.Component.empty();
//        int entities = 0;
//        int chunks = 0;
//        for (ServerLevel dim : levels) {
//            for (Entity entity : dim.getEntities().getAll()) {
//                entities++;
//            }
//            chunks += dim.getChunkSource().getLoadedChunksCount();
//        }
//        return hover;
//    }
//
//    private static java.awt.Component createMessage(CommandSourceStack cs, ServerLevel dim) throws CommandSyntaxException {
//        long[] times = cs.getServer().getTickTime(dim.dimension());
//        Registry<DimensionType> reg = cs.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
//        return createMessage(times, java.awt.Component.literal(dim.dimension().location().toString()), java.awt.Component.literal(reg.getKey(dim.dimensionType()).toString()));
//    }
//
//    private static java.awt.Component createMessage(long[] times, MutableComponent world, java.awt.Component hoverInfo) {
//        if(times == null) {
//            times = UNLOADED;
//        }
//        double worldTickTime = mean(times) * 0.000001;
//        double worldTPS = TimeUtil.MILLISECONDS_PER_SECOND / Math.max(worldTickTime, 50);
//        return world.append(java.awt.Component.literal("\n MSPT: ").withStyle(ChatFormatting.GRAY)
//                .append(java.awt.Component.literal(TIME_FORMATTER.format(worldTickTime))
//                        .withColor(blend((float) (50 / worldTickTime))))
//                .append(java.awt.Component.literal("\n TPS:  ").withStyle(ChatFormatting.GRAY))
//                .append(Component.literal(TIME_FORMATTER.format(worldTPS))
//                        .withColor(blend((float) (worldTPS / 20.0)))).withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverInfo))));
//    }
//
//    private static int compareResourceLocation(ResourceLocation r1, ResourceLocation r2) {
//        int i = r1.getNamespace().compareTo(r2.getNamespace());
//        if(i == 0) {
//            i = r1.getPath().compareTo(r2.getPath());
//        }
//        return i;
//    }
//    private static long mean(long[] values) {
//        long sum = 0L;
//        for (long v : values)
//            sum += v;
//        return sum / values.length;
//    }
//
//    /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
//    //Todo remove Color need
//    public static int blend( float ratio ) {
//        if ( ratio > 1f ) ratio = 1f;
//        else if ( ratio < 0f ) ratio = 0f;
//        float iRatio = 1.0f - ratio;
//
//        int i1 = Color.RED.getRGB();
//        System.out.println(i1);
//        int i2 = Color.GREEN.brighter().getRGB();
//        System.out.println(i2);
//
//        int a1 = (i1 >> 24 & 0xff);
//        int r1 = ((i1 & 0xff0000) >> 16);
//        int g1 = ((i1 & 0xff00) >> 8);
//        int b1 = (i1 & 0xff);
//
//        int a2 = (i2 >> 24 & 0xff);
//        int r2 = ((i2 & 0xff0000) >> 16);
//        int g2 = ((i2 & 0xff00) >> 8);
//        int b2 = (i2 & 0xff);
//
//        int a = (int)((a1 * iRatio) + (a2 * ratio));
//        int r = (int)((r1 * iRatio) + (r2 * ratio));
//        int g = (int)((g1 * iRatio) + (g2 * ratio));
//        int b = (int)((b1 * iRatio) + (b2 * ratio));
//        return 0xff000000 | (a << 24 | r << 16 | g << 8 | b);
//    }
//}
