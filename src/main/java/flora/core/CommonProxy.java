package flora.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import flora.core.item.ItemArmor;

public class CommonProxy {

	public static CreativeTab tab=new CreativeTab(Constants.nameCreativeTab);

	public void preInit(FMLPreInitializationEvent event) {
		ItemArmor.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {

	}
}
