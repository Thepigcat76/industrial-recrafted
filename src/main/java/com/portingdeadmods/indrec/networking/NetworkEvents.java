package com.portingdeadmods.indrec.networking;

import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.networking.bidirectional.ExampleBidirectionalPayload;
import com.portingdeadmods.indrec.networking.clientbound.ExampleClientboundPayload;
import com.portingdeadmods.indrec.networking.serverbound.ExampleServerboundPayload;
import com.portingdeadmods.indrec.networking.serverbound.UpdateInputPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = IndustrialRecrafted.MODID)
public class NetworkEvents {
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(IndustrialRecrafted.MODID);

        registrar.playToServer(
                ExampleServerboundPayload.TYPE,
                ExampleServerboundPayload.STREAM_CODEC,
                ExampleServerboundPayload::exampleServerboundAction
        );
        registrar.playToServer(
                UpdateInputPayload.TYPE,
                UpdateInputPayload.STREAM_CODEC,
                UpdateInputPayload::handle
        );

        registrar.playToClient(
                ExampleClientboundPayload.TYPE,
                ExampleClientboundPayload.STREAM_CODEC,
                ExampleClientboundPayload::exampleClientboundAction
        );

        registrar.playBidirectional(
                ExampleBidirectionalPayload.TYPE,
                ExampleBidirectionalPayload.STREAM_CODEC,
                ExampleBidirectionalPayload::exampleBidirectionalAction
        );
    }
}