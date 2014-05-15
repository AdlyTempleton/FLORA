package flora.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.modId, name = Constants.modName, version = Constants.modVersion, dependencies = Constants.modDependencies)
public class FLORA {

	@Mod.Instance(Constants.modId)
	public static FLORA instance;

	@SidedProxy(clientSide = Constants.clientProxy, serverSide = Constants.commonProxy)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);

	}

}
