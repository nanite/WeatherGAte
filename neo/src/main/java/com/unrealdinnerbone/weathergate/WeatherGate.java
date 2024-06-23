package com.unrealdinnerbone.weathergate;

import com.unrealdinnerbone.trenzalore.lib.RLUtils;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.resources.ResourceLocation;
import com.unrealdinnerbone.weathergate.data.WeatherGateData;
import com.unrealdinnerbone.weathergate.events.ServerEvents;
import com.unrealdinnerbone.weathergate.network.WeatherGateNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(WeatherGate.MOD_ID)
public class WeatherGate {
    public static final String MOD_ID = "weathergate";


    public WeatherGate(IEventBus eventBus, Dist dist) {
        eventBus.addListener(WeatherGateData::onData);
        eventBus.addListener(WeatherGateNetwork::register);
        NeoForge.EVENT_BUS.addListener(ServerEvents::onPlayerJoin);
        NeoForge.EVENT_BUS.addListener(ServerEvents::onPlayerLogout);
        NeoForge.EVENT_BUS.addListener(ServerEvents::onPlayerChangeDimension);
        NeoForge.EVENT_BUS.addListener(WeatherGate::onServer);
        if(dist == Dist.CLIENT) {
            WeatherGateClient.init(eventBus);
        }
    }


    public static void onServer(RegisterCommandsEvent event) {
    }

    public static ResourceLocation rl(String editNbt) {
        return RLUtils.rl(MOD_ID, editNbt);
    }
}