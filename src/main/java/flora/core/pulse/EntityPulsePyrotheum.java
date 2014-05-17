package flora.core.pulse;

import flora.core.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thermalfoundation.block.TFBlocks;

public class EntityPulsePyrotheum extends EntityPulse{
	public EntityPulsePyrotheum(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return TFBlocks.blockFluidPyrotheum.getIcon(0, 0);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(Constants.PREFIX_MOD+"textures/fluid/pyrotheum.png");
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
