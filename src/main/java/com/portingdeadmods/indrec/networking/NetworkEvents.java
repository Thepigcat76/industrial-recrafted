package com.portingdeadmods.indrec.networking;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.networking.clientbound.AnalyzeProgressPayload;
import com.portingdeadmods.indrec.networking.clientbound.ClearCropDropsCachePayload;
import com.portingdeadmods.indrec.networking.clientbound.CropDropsPayload;
import com.portingdeadmods.indrec.networking.clientbound.SetCropAgePayload;
import com.portingdeadmods.indrec.networking.clientbound.SetCropPayload;
import com.portingdeadmods.indrec.networking.clientbound.SetEnergyPayload;
import com.portingdeadmods.indrec.networking.clientbound.SetProgressPayload;
import com.portingdeadmods.indrec.networking.serverbound.RequestCropDropsPayload;
import com.portingdeadmods.indrec.networking.serverbound.UpdateInputPayload;
import com.portingdeadmods.indrec.networking.serverbound.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.indrec.networking.serverbound.UpgradeWidgetTogglePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = IndustrialRecrafted.MODID)
public final class NetworkEvents {
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(IndustrialRecrafted.MODID);

        registrar.playToServer(
                UpdateInputPayload.TYPE,
                UpdateInputPayload.STREAM_CODEC,
                UpdateInputPayload::handle
        );
        registrar.playToServer(
                UpgradeWidgetTogglePayload.TYPE,
                UpgradeWidgetTogglePayload.STREAM_CODEC,
                UpgradeWidgetTogglePayload::handle
        );
        registrar.playToServer(
                UpgradeWidgetSetSlotPositionsPayload.TYPE,
                UpgradeWidgetSetSlotPositionsPayload.STREAM_CODEC,
                UpgradeWidgetSetSlotPositionsPayload::handle
        );
        registrar.playToClient(
                SetEnergyPayload.TYPE,
                SetEnergyPayload.STREAM_CODEC,
                SetEnergyPayload::handle
        );
        registrar.playToClient(
                SetProgressPayload.TYPE,
                SetProgressPayload.STREAM_CODEC,
                SetProgressPayload::handle
        );
        registrar.playToClient(
                SetCropAgePayload.TYPE,
                SetCropAgePayload.STREAM_CODEC,
                SetCropAgePayload::handle
        );
        registrar.playToServer(
                RequestCropDropsPayload.TYPE,
                RequestCropDropsPayload.STREAM_CODEC,
                RequestCropDropsPayload::handle
        );
        registrar.playToClient(
                CropDropsPayload.TYPE,
                CropDropsPayload.STREAM_CODEC,
                CropDropsPayload::handle
        );
        registrar.playToClient(
                AnalyzeProgressPayload.TYPE,
                AnalyzeProgressPayload.STREAM_CODEC,
                AnalyzeProgressPayload::handle
        );
        registrar.playToClient(
                ClearCropDropsCachePayload.TYPE,
                ClearCropDropsCachePayload.STREAM_CODEC,
                ClearCropDropsCachePayload::handle
        );
        registrar.playToClient(
                SetCropPayload.TYPE,
                SetCropPayload.STREAM_CODEC,
                SetCropPayload::handle
        );
    }
}