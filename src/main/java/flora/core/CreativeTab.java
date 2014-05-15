package flora.core;

import flora.core.item.ItemArmor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {
	public CreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return ItemArmor.instance;
	}
}
