package flora.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import flora.core.block.BlockInfuser;
import flora.core.block.TileInfuser;
import flora.core.gui.ContainerInfuser;
import flora.core.gui.GuiInfuser;
import flora.core.item.ItemArmorFLORA;
import flora.core.logic.ArmorEffectsManager;
import flora.core.logic.KeyHandlerEnder;
import flora.core.pulse.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IGuiHandler{

	public static CreativeTab tab=new CreativeTab(Constants.nameCreativeTab);

	public void preInit(FMLPreInitializationEvent event) {
		ItemArmorFLORA.register();
		BlockInfuser.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public ArmorEffectsManager effectsManager;

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(FLORA.instance, FLORA.proxy);
		effectsManager=new ArmorEffectsManager();
		FMLCommonHandler.instance().bus().register(effectsManager);
		MinecraftForge.EVENT_BUS.register(effectsManager);

		FMLCommonHandler.instance().bus().register(new KeyHandlerEnder());

		NetworkRegistry.INSTANCE.newChannel(Constants.modId, new PacketHandler());


		EntityRegistry.registerModEntity(EntityPulseMana.class, "Destabilized Mana Pulse", 0, FLORA.instance, 224, 1, true);

		EntityRegistry.registerModEntity(EntityPulseSlow.class, "Destabilized Cryotheum Pulse", 1, FLORA.instance, 224, 1, true);

		EntityRegistry.registerModEntity(EntityPulsePyrotheum.class, "Destabilized Pyrotheum Pulse", 2, FLORA.instance, 224, 1, true);

		EntityRegistry.registerModEntity(EntityPulseCoal.class, "Destabilized Coal Pulse", 3, FLORA.instance, 224, 1, true);

		EntityRegistry.registerModEntity(EntityPulseEnder.class, "Destabilized Ender Pulse", 4, FLORA.instance, 224, 1, true);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileInfuser){
			return new ContainerInfuser(player.inventory, (TileInfuser) world.getTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileInfuser){
			return new GuiInfuser((TileInfuser) tileEntity, player.inventory);
		}
		return null;
	}
}
