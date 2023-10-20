/**
 * 
 */
package mod.gottsch.neoforge.ddenizens.client.renderer.entity;

import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.GhoulModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Ghoul;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling on Apr 1, 2022
 *
 */
public class GhoulRenderer<T extends Ghoul> extends MobRenderer<T, GhoulModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/ghoul.png");
	
	/**
	 * 
	 * @param context
	 */
	public GhoulRenderer(EntityRendererProvider.Context context) {
        super(context, new GhoulModel<>(context.bakeLayer(GhoulModel.LAYER_LOCATION)), 0.3F);
    }

     @Override
    public ResourceLocation getTextureLocation(Ghoul entity) {
        return TEXTURE;
    }
}
