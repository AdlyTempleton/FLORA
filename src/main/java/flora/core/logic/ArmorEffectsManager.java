package flora.core.logic;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flora.core.item.ItemArmorFLORA;
import flora.core.pulse.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import thermalfoundation.fluid.TFFluids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public static float[][] getEffectMatrix(ItemStack[] armor){
		/*
		* Returns a 7x7 matrix of floats corresponding to the intensity of different effects on a player
		* The value at [i][j] corresponds to the strength of the interaction between liquids 'i' and 'j'
		* A value of 1F represents an interaction of 1000 mB of each liquid
		* Fluid-Integer mapplings are given by fluidIntegerHashMap
		*/


		//1-Dimensional array of floats representing total amounts, in buckets, of a fluid on a player
		float[] totalFluidCount = new float[7];
		for(int i=0;i<armor.length;i++){
			ItemStack stack=armor[i];
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

	public static float[][] getEffectMatrix(EntityPlayer player){
		return getEffectMatrix(player.inventory.armorInventory);
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
					event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Math.max(4, (intensity*rand.nextGaussian())+20));
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

				if(player.worldObj.getBiomeGenForCoords(player.chunkCoordX, player.chunkCoordZ).temperature<=.2){
					if(rand.nextInt(1000)<intensity){
						player.attackEntityFrom(DamageSource.starve, 1F);
					}
				}
			}

			//Redstone-Mana
			if(fluidInteractionMatrix[3][5]>0){
				intensity=fluidInteractionMatrix[3][5];
				if(rand.nextInt(5000)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){
						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						if(target!=player){
							EntityPulse e = new EntityPulseMana(player.worldObj, player, target.posX-player.posX, target.posY-player.posY, target.posZ-player.posZ);
							player.worldObj.spawnEntityInWorld(e);
						}
					}
				}
			}

			//Coal-Mana
			if(fluidInteractionMatrix[0][5]>0){
				intensity=fluidInteractionMatrix[0][5];
				if(rand.nextInt(5000)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){
						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						if(target!=player){
							EntityPulse e = new EntityPulseCoal(player.worldObj, player, target.posX-player.posX, target.posY-player.posY, target.posZ-player.posZ);
							player.worldObj.spawnEntityInWorld(e);
						}
					}
				}
			}

			//Redstone-Mana
			if(fluidInteractionMatrix[1][5]>0){
				intensity=fluidInteractionMatrix[1][5];
				if(rand.nextInt(5000)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){
						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						if(target!=player){
							EntityPulse e = new EntityPulsePyrotheum(player.worldObj, player, target.posX-player.posX, target.posY-player.posY, target.posZ-player.posZ);
							player.worldObj.spawnEntityInWorld(e);
						}
					}
				}
			}

			//Redstone-Mana
			if(fluidInteractionMatrix[2][5]>0){
				intensity=fluidInteractionMatrix[2][5];
				if(rand.nextInt(5000)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){
						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						if(target!=player){
							EntityPulse e = new EntityPulseSlow(player.worldObj, player, target.posX-player.posX, target.posY-player.posY, target.posZ-player.posZ);
							player.worldObj.spawnEntityInWorld(e);
						}
					}
				}
			}

			//Redstone-Mana
			if(fluidInteractionMatrix[4][5]>0){
				intensity=fluidInteractionMatrix[4][5];
				if(rand.nextInt(5000)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){
						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						if(target!=player){
							EntityPulse e = new EntityPulseEnder(player.worldObj, player, target.posX-player.posX, target.posY-player.posY, target.posZ-player.posZ);
							player.worldObj.spawnEntityInWorld(e);
						}
					}
				}
			}

			//Cyrotheum-Mana
			if(fluidInteractionMatrix[3][2]>0){
				intensity=fluidInteractionMatrix[3][2];
				if(rand.nextInt(50000)<intensity){

					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);
				}
			}

			//Pyrotheum-Mana
			if(fluidInteractionMatrix[3][1]>0){
				intensity=fluidInteractionMatrix[3][1];
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, (int)Math.log10(intensity)));
			}

			//Pyrotheum-Cyrotheum
			if(fluidInteractionMatrix[2][1]>0){
				intensity=fluidInteractionMatrix[2][1];
				player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, (int)Math.log10(intensity)));
			}
			//Coal-Cyrotheum
			if(fluidInteractionMatrix[2][0]>0){
				intensity=fluidInteractionMatrix[2][0];
				player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1, (int)Math.log10(intensity)));
			}
			//Ender-Mana
			if(fluidInteractionMatrix[3][4]>0){
				intensity=fluidInteractionMatrix[3][4];
				player.addPotionEffect(new PotionEffect(Potion.jump.id, 1, (int)Math.log10(intensity)));
			}
			//Coal-Mana
			if(fluidInteractionMatrix[0][3]>0){
				intensity=fluidInteractionMatrix[0][3];
				if(rand.nextInt(500)<intensity){
					List<EntityLivingBase> nearbyEntities=player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(20, 3, 20));
					if(nearbyEntities.size()>1){

						EntityLivingBase target=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));

						EntityLivingBase newTarget=nearbyEntities.get(rand.nextInt(nearbyEntities.size()));
						target.setRevengeTarget(newTarget);
					}
				}
			}

			HashMap<String, Long> fireResistenceCooldownNextIteration=new HashMap<String, Long>();
			for(Map.Entry<String, Long> entry:fireResistenceCooldown.entrySet()){
				fireResistenceCooldownNextIteration.put(entry.getKey(), entry.getValue()-1);
			}
			fireResistenceCooldown=fireResistenceCooldownNextIteration;
		}

	}


	private static HashMap<String, Long> fireResistenceCooldown=new HashMap<String, Long>();
	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event){
		Entity attacker=event.source.getEntity();
		if(attacker instanceof EntityPlayer){
			if(!attacker.worldObj.isRemote){
				EntityPlayer attackerPlayer= (EntityPlayer) attacker;
				float[][] fluidInteractionMatrix=getEffectMatrix(attackerPlayer);
				float intensity;
				Random rand=new Random();

				//Glowstone-Coal
				if(fluidInteractionMatrix[0][6]>0){
					intensity=fluidInteractionMatrix[0][6];
					event.entity.setFire((int) (intensity*20));
				}

				//Coal-Pyrotheum
				if(fluidInteractionMatrix[1][0]>0){
					intensity=fluidInteractionMatrix[1][0];
					if(event.entity.isBurning()){

						event.entity.worldObj.createExplosion(event.entity, event.entity.posX, event.entity.posY, event.entity.posZ, (float) Math.sqrt(intensity), true);
					}
				}
			}
		}

		if(event.entity instanceof EntityPlayer){

			EntityPlayer player= (EntityPlayer) event.entity;
			float[][] fluidInteractionMatrix=getEffectMatrix(player);
			float intensity;
			Random rand=new Random();
			if(!player.worldObj.isRemote){



				//Cyrotheum-Ender
				if(fluidInteractionMatrix[2][4]>0){
					intensity=fluidInteractionMatrix[2][4];
					if(player.getHealth()-event.ammount<=4){
						event.ammount=0;
						int x= (int) ((int) player.posX + rand.nextInt((int) (6*intensity)) - ((int)3*intensity));
						int z= (int) ((int) player.posZ + rand.nextInt((int) (6*intensity)) - ((int)3*intensity));
						int y=255;
						while(player.worldObj.isAirBlock(x, y-1, z)){
							y--;
						}
						player.setPositionAndUpdate(x+.5, y, z+.5);
					}
				}

				//Coal-Ender
				if(fluidInteractionMatrix[0][4]>0){
					intensity=fluidInteractionMatrix[0][4];
					if(event.source.getEntity() instanceof EntityLiving){
						event.ammount=0;
						int x= (int) ((int) player.posX + rand.nextInt((int) (2*intensity)) - ((int)intensity));
						int z= (int) ((int) player.posZ + rand.nextInt((int) (2*intensity)) - ((int)intensity));
						int y=255;
						while(player.worldObj.isAirBlock(x, y-1, z)){
							y--;
						}
						((EntityLiving)event.source.getEntity()).setPositionAndUpdate(x + .5, y, z + .5);
					}
				}

				//Pyrotheum-Glowstone
				if(fluidInteractionMatrix[1][6]>0){
					intensity=fluidInteractionMatrix[1][6];
					if(event.source.isFireDamage()){
						if(!fireResistenceCooldown.containsKey(player.getDisplayName())||fireResistenceCooldown.get(player.getDisplayName())<intensity*10){
							long currentCooldown=0;
							if(fireResistenceCooldown.containsKey(player.getDisplayName())){
								currentCooldown=fireResistenceCooldown.get(player.getDisplayName());
							}
							fireResistenceCooldown.put(player.getDisplayName(), Math.max(40, currentCooldown+40));
							event.ammount=0;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event){

		if(event.entity instanceof EntityPlayer){
			float[][] fluidInteractionMatrix=getEffectMatrix((EntityPlayer) event.entity);
			float intensity;
			Random rand=new Random();

			//Pyrotheum-Pyrotheum
			if(fluidInteractionMatrix[1][1]>0){
				intensity=fluidInteractionMatrix[1][1];
				if(event.distance>2){
					event.distance+=Math.sqrt(intensity);
				}
			}
			//Ender-Pyrotheum
			if(fluidInteractionMatrix[1][4]>0){
				intensity=fluidInteractionMatrix[1][4];
				event.distance/=intensity;

			}
		}




	}

}
