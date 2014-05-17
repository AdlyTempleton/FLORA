package flora.core.pulse;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class EntityPulse extends EntityFireball {

	public EntityPulse(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}
	public EntityPulse(World par1World) {
		super(par1World);
	}

	public void onUpdate(){
		super.onUpdate();
		this.setFlag(0, false);
	}

	@Override
	public void setFire(int par1) {}

	public abstract IIcon getRenderIcon();
}
