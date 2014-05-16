package flora.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import flora.core.CommonProxy;
import flora.core.logic.EnumArmorQuality;
import flora.core.logic.EnumArmorType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

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
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			for(FluidTank tank:getFluidTanks(par1ItemStack)){
				par3List.add(tank.getFluid().getFluid().getName()+" : "+tank.getFluidAmount()+"mb");
			}
		}else{
			par3List.add("Sneak to view loaded fluids");
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

	public static final String NBT_FLUID_TAG_LIST="FLORA Armor Fluid Tag List";

	public ArrayList<FluidTank> getFluidTanks(ItemStack item){
		NBTTagCompound nbt = item.stackTagCompound;
		ArrayList<FluidTank> r=new ArrayList<FluidTank>();

		if(nbt==null){
			return r;
		}
		NBTTagList tagList=nbt.getTagList(NBT_FLUID_TAG_LIST,Constants.NBT.TAG_COMPOUND);
		for(int i=0;i<tagList.tagCount();i++){
			NBTTagCompound fluid=tagList.getCompoundTagAt(i);
			FluidTank tank = new FluidTank(quality.storage);
			tank.readFromNBT(fluid);
			if(tank.getFluidAmount()>0){
				r.add(tank.readFromNBT(fluid));
			}
		}
		return r;
	}

	public void setFluidTanks(ItemStack item, ArrayList<FluidTank> fluids){
		if(item.stackTagCompound==null){
			item.stackTagCompound=new NBTTagCompound();
		}

		NBTTagCompound nbt=item.stackTagCompound;

		NBTTagList list=new NBTTagList();
		for(FluidTank tank:fluids){
			NBTTagCompound tankCompound=new NBTTagCompound();
			tank.writeToNBT(tankCompound);
			list.appendTag(tankCompound);
		}
		nbt.setTag(NBT_FLUID_TAG_LIST, list);
	}

	public int getTotalFluidAmount(ItemStack item){
		int i=0;
		for(FluidTank tank:getFluidTanks(item)){
			i+=tank.getFluidAmount();
		}
		return i;
	}

	public int getFluidCapacity(){
		return quality.storage;
	}
}

