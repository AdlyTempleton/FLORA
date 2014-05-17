package flora.core;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import flora.core.logic.ArmorEffectsManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.HashMap;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {

	private static HashMap<String, Long> fireballCooldown=new HashMap<String, Long>();
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception {
		if (packet.channel().equals(Constants.modId)) {
			ByteBuf payload = packet.payload();
			if (payload.readableBytes() == 4) {
				int number = payload.readInt();
				INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
				EntityPlayer player = ((NetHandlerPlayServer) netHandler).playerEntity;
				if(number==0){
					if(player.isSneaking()){

						if(ArmorEffectsManager.getEffectMatrix(player)[4][6]>0){

							float intensity=ArmorEffectsManager.getEffectMatrix(player)[4][6];

							for(int i=1;i<intensity;i++){
								if(player.worldObj.checkBlockCollision(player.boundingBox.getOffsetBoundingBox(0, -i, 0))){
									player.setPositionAndUpdate(player.posX, player.posY-i, player.posZ);
									return;
								}

							}
						}
					}else{
						if(ArmorEffectsManager.getEffectMatrix(player)[4][4]>0){
							float intensity=ArmorEffectsManager.getEffectMatrix(player)[4][4];
							//Raytrace based off code from MachineMuse
							Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
							Vec3 playerLook = player.getLookVec();

							Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * intensity, playerPosition.yCoord
									+ playerLook.yCoord * intensity, playerPosition.zCoord + playerLook.zCoord * intensity);
							MovingObjectPosition movingObjectPosition=player.worldObj.rayTraceBlocks(playerPosition, playerViewOffset);

							if(movingObjectPosition!=null){
								player.setPositionAndUpdate(movingObjectPosition.blockX, movingObjectPosition.blockY+1, movingObjectPosition.blockZ);
							}

						}
					}
				}
				if(number==1){
					if(ArmorEffectsManager.getEffectMatrix(player)[0][0]>0){
						float intensity=ArmorEffectsManager.getEffectMatrix(player)[0][0];
						if(!fireballCooldown.containsKey(player.getDisplayName()) || fireballCooldown.get(player.getDisplayName())<player.ticksExisted){
							fireballCooldown.put(player.getDisplayName(), (long) (player.ticksExisted + 600 - intensity));

							EntityLargeFireball entitylargefireball = new EntityLargeFireball(player.worldObj, player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
							entitylargefireball.field_92057_e = 1;
							double d8 = 4.0D;
							Vec3 vec3 = player.getLook(1.0F);
							entitylargefireball.posX = player.posX + vec3.xCoord * d8;
							entitylargefireball.posY = player.posY + (double)(player.height / 2.0F) + 0.5D;
							entitylargefireball.posZ = player.posZ + vec3.zCoord * d8;
							player.worldObj.spawnEntityInWorld(entitylargefireball);
						}



					}
				}
			}
		}
	}
}

