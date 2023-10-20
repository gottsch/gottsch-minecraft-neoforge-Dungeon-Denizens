/**
 * 
 */
package mod.gottsch.neoforge.ddenizens.client.renderer.entity;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.HeadlessModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Headless;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling on Apr 1, 2022
 *
 */
public class HeadlessRenderer<T extends Headless> extends MobRenderer<T, HeadlessModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/headless.png");
	
	/**
	 * 
	 * @param context
	 */
	public HeadlessRenderer(EntityRendererProvider.Context context) {
        super(context, new HeadlessModel<>(context.bakeLayer(HeadlessModel.LAYER_LOCATION)), 0.8F);
    }

     @Override
    public ResourceLocation getTextureLocation(Headless entity) {
        return TEXTURE;
    }
}
