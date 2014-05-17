package flora.core.pulse;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPulse extends RenderFireball {
	public RenderPulse(float par1) {
		super(par1);
		size=par1;
	}
	float size;
	@Override
	protected ResourceLocation getEntityTexture(EntityFireball entityPulse) {
		return ((EntityPulse)entityPulse).getResourceLocation();
	}

	@Override
	public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {}

	public void doRender(EntityPulse entityPulse, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		this.bindEntityTexture(entityPulse);
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f2 = size;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		IIcon iicon = entityPulse.getRenderIcon();
		Tessellator tessellator = Tessellator.instance;
		float f3 = iicon.getMinU();
		float f4 = iicon.getMaxU();
		float f5 = iicon.getMinV();
		float f6 = iicon.getMaxV();
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
		tessellator.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
