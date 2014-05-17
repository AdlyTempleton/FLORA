package flora.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ConstantsFLORA.modId, name = ConstantsFLORA.modName, version = ConstantsFLORA.modVersion, dependencies = ConstantsFLORA.modDependencies)
public class FLORA {

	@Mod.Instance(ConstantsFLORA.modId)
	public static FLORA instance;

	@SidedProxy(clientSide = ConstantsFLORA.clientProxy, serverSide = ConstantsFLORA.commonProxy)
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
