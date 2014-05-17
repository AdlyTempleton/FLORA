package flora.core.pulse;

import flora.core.ClientProxy;
import flora.core.ConstantsFLORA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

public class EntityPulseEnder extends EntityPulse {
	public EntityPulseEnder(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon(){
		return ClientProxy.enderPulseIcon;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(ConstantsFLORA.PREFIX_MOD+"textures/fluid/ender_pulse.png");
	}

	public EntityPulseEnder(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(!(var1.entityHit instanceof EntityPlayer)&&var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase && !var1.entityHit.worldObj.isRemote){
			Random rand=new Random();
			((EntityLivingBase) var1.entityHit).setPositionAndUpdate(var1.entityHit.posX+(rand.nextDouble()*10)-5, var1.entityHit.posY+5, var1.entityHit.posZ+(rand.nextDouble()*10)-5);
		}
		this.setDead();
	}
}
