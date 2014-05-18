package flora.core.block;

import cofh.item.ItemBucket;
import cpw.mods.fml.common.registry.GameRegistry;
import flora.core.CommonProxy;
import flora.core.ConstantsFLORA;
import flora.core.FLORA;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class BlockInfuser extends Block {
	public BlockInfuser() {
		super(Material.piston);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
		int l = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0){
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1){
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2){
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3){
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

	public static BlockInfuser instance;
	public static final String blockName="FLORAInfuser";
	public static void register(){
		instance=new BlockInfuser();
		instance.setBlockName(blockName);
		instance.setCreativeTab(CommonProxy.tab);
		GameRegistry.registerBlock(instance, instance.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileInfuser.class, blockName);
	}

	private IIcon iconSide;
	private IIcon iconBottom;
	private IIcon iconFace;
	private IIcon iconTop;

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		iconSide= iconRegister.registerIcon(ConstantsFLORA.PREFIX_MOD+"Machine_Side");

		iconTop= iconRegister.registerIcon(ConstantsFLORA.PREFIX_MOD+"Machine_Top");

		iconFace= iconRegister.registerIcon(ConstantsFLORA.PREFIX_MOD+"Machine_Face");
		iconBottom= iconRegister.registerIcon(ConstantsFLORA.PREFIX_MOD+"Machine_Bottom");

	}

	@Override
	public IIcon getIcon(int side, int meta) {

		if(side==1){
			return iconTop;
		}
		if(side==0){
			return iconBottom;
		}
		if(side==meta || (side==3 && meta==0)){
			return iconFace;
		}
		return iconSide;
	}

	@Override
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
		return super.getIcon(p_149673_1_, p_149673_2_, p_149673_3_, p_149673_4_, p_149673_5_);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileInfuser();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, p_149749_6_);
	}
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null) {
			return false;
		}
		if(player.inventory.getCurrentItem() !=null && (player.inventory.getCurrentItem().getItem() instanceof ItemBucket)){
			FluidStack fluid=null;
			String fluidName=null;
			String stackName=player.inventory.getCurrentItem().getUnlocalizedName();
			if(stackName.contains("bucketRedstone")){
				fluidName="redstone";
			}
			if(stackName.contains("bucketGlowstone")){
				fluidName="glowstone";
			}
			if(stackName.contains("bucketEnder")){
				fluidName="ender";
			}
			if(stackName.contains("bucketPyrotheum")){
				fluidName="pyrotheum";
			}
			if(stackName.contains("bucketCryotheum")){
				fluidName="cryotheum";
			}
			if(stackName.contains("bucketCoal")){
				fluidName="coal";
			}
			if(stackName.contains("bucketMana")){
				fluidName="mana";
			}
			if (fluidName==null){
				return false;
			}

			FluidStack fluidStack=new FluidStack(FluidRegistry.getFluid(fluidName), 1000);
			if(((TileInfuser)tileEntity).fillArmorWithFluid(fluidStack, true)){
				player.getHeldItem().stackSize--;
				if(player.getHeldItem().stackSize==0){
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				player.inventory.addItemStackToInventory(new ItemStack(Items.bucket));
			}
			return true;
		}
		if(!player.isSneaking()){
			player.openGui(FLORA.instance, 0, world, x, y, z);
		}
		return true;
	}


	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}


}
