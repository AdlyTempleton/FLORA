package flora.core.pulse;

import flora.core.ClientProxy;
import flora.core.ConstantsFLORA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityPulsePyrotheum extends EntityPulse{
	public EntityPulsePyrotheum(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return ClientProxy.pyrotheumPulseIcon;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(ConstantsFLORA.PREFIX_MOD+"textures/fluid/pyrotheum_pulse.png");
	}

	public EntityPulsePyrotheum(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=sender&&var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase){
			var1.entityHit.setFire(200);
		}
		this.setDead();
	}
}
