package flora.core.gui;

import flora.core.block.TileInfuser;
import flora.core.item.ItemArmorFLORA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInfuser extends Container {

	protected TileInfuser tileEntity;

	public ContainerInfuser (InventoryPlayer inventoryPlayer, TileInfuser te){
		tileEntity = te;



		addSlotToContainer(new SlotFloraArmor(tileEntity, 0, 43, 61, 0));
		addSlotToContainer(new SlotFloraArmor(tileEntity, 1, 63, 61, 1));
		addSlotToContainer(new SlotFloraArmor(tileEntity, 2, 83, 61, 2));
		addSlotToContainer(new SlotFloraArmor(tileEntity, 3, 103, 61, 3));

		addSlotToContainer(new SlotBucket(tileEntity, 4, 11, 26));
		bindPlayerInventory(inventoryPlayer);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			if (slot < 5) {
				if (!this.mergeItemStack(stackInSlot, 0, 31, true)) {
					return null;
				}
			}

			if(TileInfuser.getFluidFromItem(stackInSlot)!=null){
				Slot targetSlot= (Slot) inventorySlots.get(4);
				if(targetSlot.isItemValid(stackInSlot) && !targetSlot.getHasStack()){
					targetSlot.putStack(stackInSlot);
					slotObject.putStack(null);
				}
			}

			if (stackInSlot.getItem() instanceof ItemArmorFLORA && stackInSlot.stackSize==1){
				int i=((ItemArmorFLORA) stackInSlot.getItem()).type.ordinal();
				Slot targetSlot= (Slot) inventorySlots.get(i);
				if(targetSlot.isItemValid(stackInSlot) && !targetSlot.getHasStack()){
					targetSlot.putStack(stackInSlot);
					slotObject.putStack(null);
				}
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}
}
