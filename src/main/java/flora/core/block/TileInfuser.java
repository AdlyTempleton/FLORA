package flora.core.block;

import flora.core.item.ItemArmorFLORA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;

public class TileInfuser extends TileEntity implements IInventory, IFluidHandler{
	public TileInfuser() {
		super();
	}

	private ItemStack[] inv=new ItemStack[4];

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (inv[par1] != null){
			ItemStack itemstack;

			if (inv[par1].stackSize <= par2){
				itemstack = inv[par1];
				inv[par1] = null;
				return itemstack;
			}
			else{
				itemstack = inv[par1].splitStack(par2);

				if (inv[par1].stackSize == 0){
					inv[par1] = null;
				}

				return itemstack;
			}
		}
		else{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (inv[var1] != null){
			ItemStack itemstack = inv[var1];
			inv[var1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv[var1]=var2;
	}

	@Override
	public String getInventoryName() {
		return BlockInfuser.blockName;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return var2.getItem() instanceof ItemArmorFLORA && ((ItemArmorFLORA) var2.getItem()).type.ordinal()==var1;
	}

	public boolean fillArmorWithFluid(FluidStack fluid, boolean doFill){
		for(int i=0;i<4;i++){
			ItemStack item=inv[i];
			if(item!=null && item.getItem() instanceof ItemArmorFLORA){
				//Check if total fluid amount is less than capacity
				int space= ((ItemArmorFLORA) item.getItem()).getFluidCapacity() - ((ItemArmorFLORA) item.getItem()).getTotalFluidAmount(item) ;
				if(space > 0){
					ArrayList<FluidTank> tanks=((ItemArmorFLORA) item.getItem()).getFluidTanks(item);
					for (FluidTank tank:tanks){
						if(tank.getFluid().getFluid()==fluid.getFluid()){
							int drain=Math.min(space, fluid.amount);
							tank.fill(new FluidStack(fluid.getFluid(), drain), true);
							fluid.amount-=drain;
							if(doFill){
								((ItemArmorFLORA) item.getItem()).setFluidTanks(item, tanks);
							}
							if(fluid.amount<=0){
								return true;
							}

						}
					}
					if(space>=fluid.amount){
						tanks.add(new FluidTank(fluid, ((ItemArmorFLORA) item.getItem()).getFluidCapacity()));
						if(doFill){
							((ItemArmorFLORA) item.getItem()).setFluidTanks(item, tanks);
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return fillArmorWithFluid(resource, doFill) ? 0 : 1000;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public ArrayList<FluidTank> getTotalFluidTank(){
		ArrayList<FluidTank> r=new ArrayList<FluidTank>();
		for(int i=0;i<4;i++){
			if(inv[i]!=null && inv[i].getItem() instanceof ItemArmorFLORA){
				r.addAll(((ItemArmorFLORA) inv[i].getItem()).getFluidTanks(inv[i]));
			}
		}

		return r;
	}

	public int getTotalFluidAmount() {
		int r=0;
		for(int i=0;i<4;i++){
			if(inv[i]!=null && inv[i].getItem() instanceof ItemArmorFLORA){
				r+=(((ItemArmorFLORA) inv[i].getItem()).getTotalFluidAmount(inv[i]));
			}
		}

		return r;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		ArrayList<FluidTank> tanks=getTotalFluidTank();
		FluidTankInfo[] r= new FluidTankInfo[tanks.size()];
		for(int i=0;i<tanks.size();i++){
			r[i]=tanks.get(i).getInfo();
		}
		return r;
	}


}
