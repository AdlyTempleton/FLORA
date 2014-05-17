package flora.core.pulse;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thermalfoundation.block.TFBlocks;

public class EntityPulseMana extends EntityPulse {

	public EntityPulseMana(World par1World, EntityLivingBase e, double par8, double par10, double par12) {
		super(par1World, e, par8, par10, par12);
	}

	@Override
	public IIcon getRenderIcon() {
		return TFBlocks.blockFluidMana.getIcon(0, 0);
	}
	public EntityPulseMana(World par1World) {
		super(par1World);
	}


	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=null && var1.entityHit instanceof EntityLivingBase){
			((EntityLivingBase) var1.entityHit).curePotionEffects(new ItemStack(Items.milk_bucket));
		}
		this.setDead();
	}

}
