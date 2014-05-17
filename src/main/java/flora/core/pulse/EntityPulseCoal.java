package flora.core.pulse;

import flora.core.ConstantsFLORA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thermalfoundation.block.TFBlocks;

public class EntityPulseCoal extends EntityPulse{

	public EntityPulseCoal(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return TFBlocks.blockFluidCoal.getIcon(0, 0);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(ConstantsFLORA.PREFIX_MOD+"textures/fluid/coal.png");
	}

	public EntityPulseCoal(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=sender&&var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase){
			var1.entityHit.attackEntityFrom(DamageSource.onFire, 3F);
		}
		this.setDead();
	}
	
}
