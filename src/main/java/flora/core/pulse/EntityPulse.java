package flora.core.pulse;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class EntityPulse extends EntityFireball {

	EntityLivingBase sender;

	public EntityPulse(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e.posX+.1*par8, e.posY+1+.1*par10, e.posZ+.1*par12, par8, par10, par12);
		sender=e;

		this.setSize(0.3125F, 0.3125F);
	}
	public EntityPulse(World par1World) {
		super(par1World);

		this.setSize(0.3125F, 0.3125F);
	}

	public void onUpdate(){
		super.onUpdate();
		this.setFlag(0, false);
	}

	@Override
	public void setFire(int par1) {}

	public abstract IIcon getRenderIcon();

	public abstract ResourceLocation getResourceLocation();
}
