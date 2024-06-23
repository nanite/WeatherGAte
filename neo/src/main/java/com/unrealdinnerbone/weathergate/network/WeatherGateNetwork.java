package com.unrealdinnerbone.weathergate.network;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.RemovePosPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.SyncColorsPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.colorsync.UpdateColorPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class WeatherGateNetwork {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(WeatherGate.MOD_ID);


        registrar.playToClient(OpenTerrainControllerPacket.TYPE, OpenTerrainControllerPacket.CODEC, OpenTerrainControllerPacket::handleOpenTerrainControllerPacket);
        registrar.playToClient(RemovePosPacket.TYPE,RemovePosPacket.CODEC, RemovePosPacket::handleRemovePosPacket);
        registrar.playToClient(SyncColorsPacket.TYPE, SyncColorsPacket.CODEC, SyncColorsPacket::handleSyncColorsPacket);

        registrar.playToClient(UpdateColorPacket.TYPE, UpdateColorPacket.CODEC, UpdateColorPacket::handleUpdateColorPacket);

        registrar.playToServer(UpdateControllerPacket.TYPE, UpdateControllerPacket.CODEC, UpdateControllerPacket::handleUpdateColorPacket);




    }
}
