/**
 * 
 */
package mod.gottsch.neoforge.ddenizens.client.renderer.entity;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.DaemonModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Daemon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Apr 21, 2022
 *
 * @param <T>
 */
public class DaemonRenderer<T extends Daemon> extends MobRenderer<T, DaemonModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/daemon.png");
	
	/**
	 * 
	 * @param context
	 */
	public DaemonRenderer(EntityRendererProvider.Context context) {
        super(context, new DaemonModel<>(context.bakeLayer(DaemonModel.LAYER_LOCATION)), 0F);
    }

     @Override
    public ResourceLocation getTextureLocation(Daemon entity) {
        return TEXTURE;
    }
}
