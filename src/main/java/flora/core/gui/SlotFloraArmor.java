package flora.core.gui;

import flora.core.item.ItemArmorFLORA;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFloraArmor extends Slot {

	private int armorType;

	public SlotFloraArmor(IInventory par1IInventory, int par2, int par3, int par4, int armorType) {
		super(par1IInventory, par2, par3, par4);
		this.armorType=armorType;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {

		return par1ItemStack.getItem() instanceof ItemArmorFLORA && ((ItemArmorFLORA) par1ItemStack.getItem()).type.ordinal()==armorType;
	}
}
