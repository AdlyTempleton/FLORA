package flora.core.logic;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flora.core.item.ItemArmorFLORA;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import thermalfoundation.fluid.TFFluids;

import java.util.HashMap;
import java.util.Random;

public class ArmorEffectsManager{

	public static HashMap<Fluid, Integer> fluidIntegerHashMap=new HashMap<Fluid, Integer>();

	static {
		fluidIntegerHashMap.put(TFFluids.fluidCoal, 0);

		fluidIntegerHashMap.put(TFFluids.fluidPyrotheum, 1);

		fluidIntegerHashMap.put(TFFluids.fluidCryotheum, 2);

		fluidIntegerHashMap.put(TFFluids.fluidMana, 3);

		fluidIntegerHashMap.put(TFFluids.fluidEnder, 4);

		fluidIntegerHashMap.put(TFFluids.fluidRedstone, 5);

		fluidIntegerHashMap.put(TFFluids.fluidGlowstone, 6);
	}

	public static float[][] getEffectMatrix(EntityPlayer player){
		/*
		* Returns a 7x7 matrix of floats corresponding to the intensity of different effects on a player
		* The value at [i][j] corresponds to the strength of the interaction between liquids 'i' and 'j'
		* A value of 1F represents an interaction of 1000 mB of each liquid
		* Fluid-Integer mapplings are given by fluidIntegerHashMap
		*/

		//1-Dimensional array of floats representing total amounts, in buckets, of a fluid on a player
		float[] totalFluidCount = new float[7];
		for(int i=0;i<4;i++){
			ItemStack stack=player.inventory.armorInventory[i];
			if(stack!=null && stack.getItem() instanceof ItemArmorFLORA){
				for(FluidTank tank:((ItemArmorFLORA) stack.getItem()).getFluidTanks(stack)){
					totalFluidCount[fluidIntegerHashMap.get(tank.getFluid().getFluid())]+=(tank.getFluidAmount()/1000F);
				}

			}
		}

		float[][] fluidInteractionMatrix = new float[7][7];
		for(int i=0;i<7;i++){
			for(int j=0;j<7;j++){
				fluidInteractionMatrix[i][j]=totalFluidCount[i]*totalFluidCount[j];
			}
		}
		return fluidInteractionMatrix;
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		float[][] fluidInteractionMatrix=getEffectMatrix(event.player);
		float intensity;
		Random rand=new Random();
		EntityPlayer player=event.player;
		if(!event.player.worldObj.isRemote){
			//Glowstone-Glowstone
			if(fluidInteractionMatrix[6][6]>0){
				intensity=fluidInteractionMatrix[6][6];
				if(rand.nextInt(100)<intensity){
					event.player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 10));
				}
			}

			//Redstone-Redstone
			if(fluidInteractionMatrix[5][5]>0){
				intensity=fluidInteractionMatrix[5][5];
				if(event.player.worldObj.getTotalWorldTime()%600==0){
					event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((intensity*rand.nextGaussian())+20);
				}
			}

			//Mana-Mana
			if(fluidInteractionMatrix[3][3]>0){
				intensity=fluidInteractionMatrix[3][3];
				if(rand.nextInt(2500)<intensity){
					event.player.curePotionEffects(new ItemStack(Items.milk_bucket));
				}
			}
			//Cyrotheum-Cyrotheum
			if(fluidInteractionMatrix[2][2]>0){
				intensity=fluidInteractionMatrix[2][2];
				if(rand.nextInt(10000)<intensity){
					event.player.setAir(200);
				}
			}
			//Redstone-Glowstone
			if(fluidInteractionMatrix[5][6]>0){
				intensity=fluidInteractionMatrix[5][6];
				if(rand.nextInt(720000)<intensity){
					event.player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 20F, true);
				}
			}

			//Glowstone-Mana
			if(fluidInteractionMatrix[3][6]>0){
				intensity=fluidInteractionMatrix[3][6];
				if(rand.nextInt(200000)<intensity){

					player.getFoodStats().func_151686_a((ItemFood) Items.apple, new ItemStack(Items.apple));
				}
			}

			//Glowstone-Cryotheum
			if(fluidInteractionMatrix[2][6]>0){
				intensity=fluidInteractionMatrix[2][6];

				if(player.worldObj.getBiomeGenForCoords(player.chunkCoordX, player.chunkCoordZ).temperature<.3){
					if(rand.nextInt(1000)<intensity){
						player.attackEntityFrom(DamageSource.starve, 1F);
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void onPlayerHurt(LivingFallEvent event){
		//Pyrotheum-Pyrotheum

		if(event.entity instanceof EntityPlayer){
			float[][] fluidInteractionMatrix=getEffectMatrix((EntityPlayer) event.entity);
			float intensity;
			Random rand=new Random();
			if(fluidInteractionMatrix[1][1]>0){
				intensity=fluidInteractionMatrix[1][1];
				if(event.distance>2){
					event.distance+=Math.sqrt(intensity);
				}
			}
		}
	}

}
