package com.unrealdinnerbone.weathergate.client.screen;

import com.mojang.logging.LogUtils;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.util.Type;
import dev.ftb.mods.ftblibrary.config.ColorConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.BlankPanel;
import dev.ftb.mods.ftblibrary.ui.ColorSelectorPanel;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
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

public class TerrainControllerScreen2 extends AbstractButtonListScreen implements NordColors {

    private static final Logger LOGGER = LogUtils.getLogger();


    private final BlockPos blockPos;

    private final Map<Type, Color4I> color4IMap;

    public TerrainControllerScreen2(BlockPos blockPos, Map<Type, Color4I> color4IMap) {
        super();
        this.blockPos = blockPos;
        this.color4IMap = color4IMap;
        setWidth(60);
        setHeight(65);
        showBottomPanel(false);
    }

    @NotNull
    private SimpleButton createSimpleButton(Panel panel, Type value) {
        final Color4I[] activeValue = {color4IMap.getOrDefault(value, Color4I.WHITE)};
        return new MySimpleButton(panel, value, activeValue);
    }


    @Override
    protected void doCancel() {

    }

    @Override
    protected void doAccept() {

    }

    @Override
    public void addButtons(Panel panel) {
        Arrays.stream(Type.values()).forEach(value -> panel.add(createSimpleButton(panel, value)));
    }

    private class MySimpleButton extends SimpleButton {
        private final Color4I[] activeValue;
        private final Type value;

        public MySimpleButton(Panel panel, Type value, Color4I[] activeValue) {
            super(panel, value.getComponent(), value.getIcon(), (button, mouseButton) -> {
                ColorConfig config = new ColorConfig();
                config.setValue(activeValue[0]);
                ColorSelectorPanel colorSelectorPanel = ColorSelectorPanel.popupAtMouse(TerrainControllerScreen2.this, config, (accepted) -> {
                    if(accepted) {
                        activeValue[0] = config.getValue();
                        PacketDistributor.sendToServer(new UpdateControllerPacket(GlobalPos.of(Minecraft.getInstance().player.level().dimension(), TerrainControllerScreen2.this.blockPos), Map.of(value, config.getValue())));
                    }
                });
                colorSelectorPanel.setAllowAlphaEdit(false);
                colorSelectorPanel.setExtraZlevel(100);
            });
            this.value = value;
            this.activeValue = activeValue;
        }

        @Override
        public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            theme.drawWidget(graphics, x, y, w, h, getWidgetType());
            Color4I.BLACK.withAlpha(125).draw(graphics, x + 2, y + 2 , 12, 12);
            activeValue[0].draw(graphics, x + 3, y + 3 , 10, 10);
            icon.draw(graphics, x + 3,y + 3, 10, 10);
            theme.drawString(graphics, value.getComponent().getString(), x + 18, y + 4);
        }

    }
}
