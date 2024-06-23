package com.unrealdinnerbone.weathergate.client.screen;

import com.mojang.logging.LogUtils;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.util.Type;
import dev.ftb.mods.ftblibrary.config.ColorConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Map;

public class TerrainControllerScreen extends BaseScreen implements NordColors {

    private static final Logger LOGGER = LogUtils.getLogger();


    private Panel typesPanel;

    private Panel topPanel;

    private final BlockPos blockPos;

    private final Map<Type, Color4I> color4IMap;

    public TerrainControllerScreen(BlockPos blockPos, Map<Type, Color4I> color4IMap) {
        super();
        this.blockPos = blockPos;
        this.color4IMap = color4IMap;
    }

    @Override
    public void addWidgets() {
        this.typesPanel = new BlankPanel(this) {
            @Override
            public void alignWidgets() {
                align(new WidgetLayout.Horizontal(2, 8, 0));
            }
        };

        this.topPanel = new BlankPanel(this) {
            @Override
            public void alignWidgets() {
                align(new WidgetLayout.Horizontal(0, 4, 0));
            }
        };

//        SimpleButton simpleButton = new SimpleButton(topPanel, Component.literal("Reset"), ItemIcon.getItemIcon(Items.BARRIER), (button, mouseButton) -> {
//        });
//
//        SimpleButton closeButton = new SimpleButton(topPanel, Component.literal("Close"), Icons.CLOSE, (button, mouseButton) -> {
//        });

        Arrays.stream(Type.values()).forEach(value -> typesPanel.add(createSimpleButton(value)));
//        this.topPanel.add(simpleButton);
//        this.topPanel.add(closeButton);
        add(topPanel);
        add(typesPanel);
    }

    @NotNull
    private SimpleButton createSimpleButton(Type value) {

        final Color4I[] activeValue = {color4IMap.getOrDefault(value, Color4I.WHITE)};
        SimpleButton theButton = new SimpleButton(typesPanel, value.getComponent(), value.getIcon(), (button, mouseButton) -> {
            ColorConfig config = new ColorConfig();
            config.setValue(activeValue[0]);
            ColorSelectorPanel colorSelectorPanel = ColorSelectorPanel.popupAtMouse(typesPanel.getGui(), config, (accepted) -> {
                if(accepted) {
                    activeValue[0] = config.getValue();
                    PacketDistributor.sendToServer(new UpdateControllerPacket(GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos), Map.of(value, config.getValue())));
                }
            });
            colorSelectorPanel.setAllowAlphaEdit(false);
            colorSelectorPanel.setExtraZlevel(100);
        }) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                super.draw(graphics, theme, x, y, w, h);
                Color4I.BLACK.withAlpha(125).draw(graphics, x - 1, y + 16 + 2, w + 2, 6);
                activeValue[0].draw(graphics, x, y + 16 + 3, w, 4);
            }
        };
        theButton.setWidth(18);
        return theButton;
    }


    @Override
    public void alignWidgets() {
        this.typesPanel.setPosAndSize(40, 24, 256, 64);
        this.topPanel.setPosAndSize(112 + 10, 2, 36, 16);
        this.typesPanel.alignWidgets();
        this.topPanel.alignWidgets();

    }

    @Override
    public boolean onInit() {
        return this.setSizeProportional(0.25f, 0.25F);
    }

}
