package flora.core.block;

import flora.core.item.ItemArmorFLORA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;

public class TileInfuser extends TileEntity implements IInventory{
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

	public boolean fillArmorWithFluid(FluidStack fluid){
		for(int i=0;i<4;i++){
			ItemStack item=inv[i];
			if(item.getItem() instanceof ItemArmorFLORA){
				//Check if total fluid amount is less than capacity
				int space= ((ItemArmorFLORA) item.getItem()).getFluidCapacity() - ((ItemArmorFLORA) item.getItem()).getTotalFluidAmount(item) ;
				if(space > 0){
					ArrayList<FluidTank> tanks=((ItemArmorFLORA) item.getItem()).getFluidTanks(item);
					for (FluidTank tank:tanks){
						if(tank.getFluid().getFluid()==fluid.getFluid()){
							int drain=Math.min(space, fluid.amount);
							tank.fill(new FluidStack(fluid.getFluid(), drain), true);
							fluid.amount-=drain;
							((ItemArmorFLORA) item.getItem()).setFluidTanks(item, tanks);
							if(fluid.amount<=0){
								return true;
							}

						}
					}
					if(space>=fluid.amount){
						tanks.add(new FluidTank(fluid, ((ItemArmorFLORA) item.getItem()).getFluidCapacity()));
						((ItemArmorFLORA) item.getItem()).setFluidTanks(item, tanks);
						return true;
					}
				}
			}
		}

		return false;
	}
}
