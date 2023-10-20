package mod.gottsch.neoforge.ddenizens.setup;


import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.BoulderModel;
import mod.gottsch.neoforge.ddenizens.client.model.DaemonModel;
import mod.gottsch.neoforge.ddenizens.client.model.EttinModel;
import mod.gottsch.neoforge.ddenizens.client.model.GazerModel;
import mod.gottsch.neoforge.ddenizens.client.model.GhoulModel;
import mod.gottsch.neoforge.ddenizens.client.model.HeadlessModel;
import mod.gottsch.neoforge.ddenizens.client.model.OrcModel;
import mod.gottsch.neoforge.ddenizens.client.model.ShadowModel;
import mod.gottsch.neoforge.ddenizens.client.model.ShadowlordModel;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.BoulderRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.DaemonRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.GazerRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.GhoulRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.HeadlessRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.OrcRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.ShadowRenderer;
import mod.gottsch.neoforge.ddenizens.client.renderer.entity.ShadowlordRenderer;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client only event bus subscriber.
 * @author Mark Gottschling on Apr 2, 2022
 *
 */
@Mod.EventBusSubscriber(modid = DD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
    	
    }
    
	/**
	 * register layers
	 * @param event
	 */
	@SubscribeEvent()
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(HeadlessModel.LAYER_LOCATION, HeadlessModel::createBodyLayer);
		event.registerLayerDefinition(OrcModel.LAYER_LOCATION, OrcModel::createBodyLayer);
		event.registerLayerDefinition(GhoulModel.LAYER_LOCATION, GhoulModel::createBodyLayer);
		event.registerLayerDefinition(EttinModel.LAYER_LOCATION, EttinModel::createBodyLayer);
		event.registerLayerDefinition(GazerModel.LAYER_LOCATION, GazerModel::createBodyLayer);
		event.registerLayerDefinition(BoulderModel.LAYER_LOCATION, BoulderModel::createBodyLayer);
		event.registerLayerDefinition(ShadowModel.LAYER_LOCATION, ShadowModel::createBodyLayer);
		event.registerLayerDefinition(ShadowlordModel.LAYER_LOCATION, ShadowlordModel::createBodyLayer);
		event.registerLayerDefinition(DaemonModel.LAYER_LOCATION, DaemonModel::createBodyLayer);
	}

	/**
	 * register renderers
	 * @param event
	 */
	@SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.HEADLESS_ENTITY_TYPE.get(), HeadlessRenderer::new);
        event.registerEntityRenderer(Registration.ORC_ENTITY_TYPE.get(), OrcRenderer::new);
        event.registerEntityRenderer(Registration.GHOUL_ENTITY_TYPE.get(), GhoulRenderer::new);
//        event.registerEntityRenderer(Registration.ETTIN_ENTITY_TYPE.get(), EttinRenderer::new);
        event.registerEntityRenderer(Registration.GAZER_ENTITY_TYPE.get(), GazerRenderer::new);
        event.registerEntityRenderer(Registration.BOULDER_ENTITY_TYPE.get(), BoulderRenderer::new);
        event.registerEntityRenderer(Registration.SHADOW_ENTITY_TYPE.get(), ShadowRenderer::new);
        event.registerEntityRenderer(Registration.SHADOWLORD_ENTITY_TYPE.get(), ShadowlordRenderer::new);
        event.registerEntityRenderer(Registration.DAEMON_ENTITY_TYPE.get(), DaemonRenderer::new);
        
        event.registerEntityRenderer(Registration.SLOWBALL_ENTITY_TYPE.get(), (provider) -> {
            // 1.0 = scale, true = full bright
        	return new ThrownItemRenderer<>(provider, 1.0F, true);
         });
        event.registerEntityRenderer(Registration.HARMBALL_ENTITY_TYPE.get(), (provider) -> {
        	return new ThrownItemRenderer<>(provider, 1.0F, true);
         });
        event.registerEntityRenderer(Registration.FIRESPOUT_ENTITY_TYPE.get(), (provider) -> {
        	return new ThrownItemRenderer<>(provider, 1.5F, true);
         });
        event.registerEntityRenderer(Registration.ROCK_ENTITY_TYPE.get(), (provider) -> {
        	return new ThrownItemRenderer<>(provider, 1.0F, true);
         });
	}
}
