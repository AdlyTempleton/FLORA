package flora.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import flora.core.CommonProxy;
import flora.core.Constants;
import flora.core.logic.EnumArmorQuality;
import flora.core.logic.EnumArmorType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

public class ItemArmor extends Item implements ISpecialArmor{

	public static ItemArmor instance;

	public static void register(){
		instance=new ItemArmor();
		instance.setUnlocalizedName(Constants.nameArmor);
		instance.setCreativeTab(CommonProxy.tab);
		GameRegistry.registerItem(instance, Constants.nameArmor);
	}



	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return null;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public String getUnlocalizedName() {
		return "FLORA Armor";
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return EnumArmorQuality.values()[stack.getItemDamage()/4].name+EnumArmorType.values()[stack.getItemDamage()%4].name;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		super.getSubItems(item, tabs, list);
		for(int i=0;i<16;i++){
			list.add(new ItemStack(item, 1, i));
		}
	}
}

