package flora.core.pulse;

import flora.core.ConstantsFLORA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thermalfoundation.block.TFBlocks;

import java.util.Random;

public class EntityPulseEnder extends EntityPulse {
	public EntityPulseEnder(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return TFBlocks.blockFluidEnder.getIcon(0, 0);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(ConstantsFLORA.PREFIX_MOD+"textures/fluid/ender.png");
	}

	public EntityPulseEnder(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=sender&&var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase){
			Random rand=new Random();
			((EntityLivingBase) var1.entityHit).setPositionAndUpdate(var1.entityHit.posX+(rand.nextDouble()*10)-5, var1.entityHit.posY+5, var1.entityHit.posZ+(rand.nextDouble()*10)-5);
		}
		this.setDead();
	}
}
