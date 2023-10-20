/**
 * 
 */
package mod.gottsch.neoforge.ddenizens.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.gottsch.neoforge.ddenizens.DD;
import mod.gottsch.neoforge.ddenizens.client.model.GazerModel;
import mod.gottsch.neoforge.ddenizens.entity.monster.Gazer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Apr 6, 2022
 *
 * @param <T>
 */
public class GazerRenderer<T extends Gazer> extends MobRenderer<T, GazerModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DD.MODID, "textures/entity/gazer.png");
	private final float scale;
	
	/**
	 * 
	 * @param context
	 */
	public GazerRenderer(EntityRendererProvider.Context context) {
		super(context, new GazerModel<>(context.bakeLayer(GazerModel.LAYER_LOCATION)), 0.8F);
		this.scale = 0.88F; // makes the body approx 16x16
	}

	@Override
	protected void scale(Gazer gazer, PoseStack pose, float scale) {
		pose.scale(this.scale, this.scale, this.scale);
	}

	@Override
	public ResourceLocation getTextureLocation(Gazer entity) {
		return TEXTURE;
	}
}
