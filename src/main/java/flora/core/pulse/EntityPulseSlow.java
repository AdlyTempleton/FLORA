package flora.core.pulse;

import flora.core.ClientProxy;
import flora.core.ConstantsFLORA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityPulseSlow extends EntityPulse {

	public EntityPulseSlow(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return ClientProxy.cryotheumPulseIcon;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(ConstantsFLORA.PREFIX_MOD+"textures/fluid/cryotheum_pulse.png");
	}

	public EntityPulseSlow(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=sender&& var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase){
			((EntityLivingBase) var1.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 3));
		}
		this.setDead();
	}
}
