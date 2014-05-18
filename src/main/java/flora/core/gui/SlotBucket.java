package flora.core.gui;

import flora.core.block.TileInfuser;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBucket extends Slot {
	public SlotBucket(IInventory par1IInventory, int par2, int par3, int par4) {
		super(par1IInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return TileInfuser.getFluidFromItem(itemStack)!=null;
	}
}
