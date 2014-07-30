package flora.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import flora.core.block.BlockInfuser;
import flora.core.block.TileInfuser;
import flora.core.gui.ContainerInfuser;
import flora.core.gui.GuiInfuser;
import flora.core.item.ItemArmorFLORA;
import flora.core.logic.ArmorEffectsManager;
import flora.core.logic.KeyHandlerEnder;
import flora.core.pulse.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import redstonearsenal.item.RAItems;
import thermalexpansion.item.TEItems;

public class CommonProxy implements IGuiHandler{

	public static CreativeTab tab=new CreativeTab(ConstantsFLORA.nameCreativeTab);
    public ArmorEffectsManager effectsManager;

	public void preInit(FMLPreInitializationEvent event) {
        ItemArmorFLORA.register();
        BlockInfuser.register();
    }

    public void postInit(FMLPostInitializationEvent event) {

        //Leadstone
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[0]), "AAA", "BCB", "   ", 'A', "blockLead", 'B', TEItems.capacitorBasic, 'C', RAItems.armorFluxHelmet));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[1]), "ACA", "ABA", "ABA", 'A', "blockLead", 'B', TEItems.capacitorBasic, 'C', RAItems.armorFluxPlate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[2]), "AAA", "ACA", "B B", 'A', "blockLead", 'B', TEItems.capacitorBasic, 'C', RAItems.armorFluxLegs));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[3]), "ACA", "B B", "   ", 'A', "blockLead", 'B', TEItems.capacitorBasic, 'C', RAItems.armorFluxBoots));

        //Hardened
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[4]), "AAA", "BCB", "   ", 'A', "blockInvar", 'B', TEItems.capacitorHardened, 'C', new ItemStack(ItemArmorFLORA.armors[0])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[5]), "ACA", "ABA", "ABA", 'A', "blockInvar", 'B', TEItems.capacitorHardened, 'C', new ItemStack(ItemArmorFLORA.armors[1])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[6]), "AAA", "ACA", "B B", 'A', "blockInvar", 'B', TEItems.capacitorHardened, 'C', new ItemStack(ItemArmorFLORA.armors[2])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[7]), "ACA", "B B", "   ", 'A', "blockInvar", 'B', TEItems.capacitorHardened, 'C', new ItemStack(ItemArmorFLORA.armors[3])));

        //Redstone
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[8]), "AAA", "BCB", "   ", 'A', "blockElectrum", 'B', TEItems.capacitorReinforced, 'C', new ItemStack(ItemArmorFLORA.armors[4])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[9]), "ACA", "ABA", "ABA", 'A', "blockElectrum", 'B', TEItems.capacitorReinforced, 'C', new ItemStack(ItemArmorFLORA.armors[5])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[10]), "AAA", "ACA", "B B", 'A', "blockElectrum", 'B', TEItems.capacitorReinforced, 'C', new ItemStack(ItemArmorFLORA.armors[6])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[11]), "ACA", "B B", "   ", 'A', "blockElectrum", 'B', TEItems.capacitorReinforced, 'C', new ItemStack(ItemArmorFLORA.armors[7])));

        //Resonant
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[12]), "AAA", "BCB", "   ", 'A', "blockEnderium", 'B', TEItems.capacitorResonant, 'C', new ItemStack(ItemArmorFLORA.armors[8])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[13]), "ACA", "ABA", "ABA", 'A', "blockEnderium", 'B', TEItems.capacitorResonant, 'C', new ItemStack(ItemArmorFLORA.armors[9])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[14]), "AAA", "ACA", "B B", 'A', "blockEnderium", 'B', TEItems.capacitorResonant, 'C', new ItemStack(ItemArmorFLORA.armors[10])));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemArmorFLORA.armors[15]), "ACA", "B B", "   ", 'A', "blockEnderium", 'B', TEItems.capacitorResonant, 'C', new ItemStack(ItemArmorFLORA.armors[11])));


    }

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(FLORA.instance, FLORA.proxy);
		effectsManager=new ArmorEffectsManager();
		FMLCommonHandler.instance().bus().register(effectsManager);
		MinecraftForge.EVENT_BUS.register(effectsManager);

		FMLCommonHandler.instance().bus().register(new KeyHandlerEnder());

		NetworkRegistry.INSTANCE.newChannel(ConstantsFLORA.modId, new PacketHandler());


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
