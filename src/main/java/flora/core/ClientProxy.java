package flora.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import flora.core.pulse.EntityPulse;
import flora.core.pulse.RenderPulse;

public class ClientProxy extends CommonProxy{
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		RenderingRegistry.registerEntityRenderingHandler(EntityPulse.class, new RenderPulse(.5F));
	}
}
