package flora.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import flora.core.block.BlockInfuser;
import flora.core.block.TileInfuser;
import flora.core.gui.ContainerInfuser;
import flora.core.gui.GuiInfuser;
import flora.core.item.ItemArmorFLORA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler{

	public static CreativeTab tab=new CreativeTab(Constants.nameCreativeTab);

	public void preInit(FMLPreInitializationEvent event) {
		ItemArmorFLORA.register();
		BlockInfuser.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(FLORA.instance, FLORA.proxy);
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
