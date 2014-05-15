package flora.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import flora.core.CommonProxy;
import flora.core.logic.EnumArmorQuality;
import flora.core.logic.EnumArmorType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ItemArmorFLORA extends ItemArmor implements ISpecialArmor{



	static ArmorMaterial[] materials;

	public ItemArmorFLORA(EnumArmorType type, EnumArmorQuality quality) {
		super(materials[quality.ordinal()], 0, type.ordinal());
		this.type=type;
		this.quality=quality;
	}

	public static ItemArmorFLORA[] armors= new ItemArmorFLORA[ 16 ];

	public EnumArmorQuality quality;
	public EnumArmorType type;
	public static void register(){

		materials=new ArmorMaterial[4];
		for(int i=0;i<4;i++){
			EnumArmorQuality quality=EnumArmorQuality.values()[i];
			materials[i]=EnumHelper.addArmorMaterial(quality.name, 0, new int[]{ quality.protection,quality.protection, quality.protection, quality.protection}, 0);
		}
		for(int i=0; i<4;i++){
			for(int j=0;j<4;j++){

				ItemArmorFLORA itemArmorFLORA=new ItemArmorFLORA(EnumArmorType.values()[i], EnumArmorQuality.values()[j]);
				itemArmorFLORA.setUnlocalizedName(itemArmorFLORA.quality.name+itemArmorFLORA.type.name);
				itemArmorFLORA.setCreativeTab(CommonProxy.tab);
				GameRegistry.registerItem(itemArmorFLORA, itemArmorFLORA.getUnlocalizedName());
				armors[i+j*4]=itemArmorFLORA;
			}
		}
	}


	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return quality.name+" "+type.name;
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(0, getArmorMaterial().getDamageReductionAmount(slot) * 0.0425, Integer.MAX_VALUE);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

	}
}

