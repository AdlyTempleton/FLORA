package flora.core.logic;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import flora.core.ConstantsFLORA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;

public class KeyHandlerEnder{
	//BORROWED CODE FROM SoBioharzardous
	public static final int CUSTOM_INV = 0;
	private static final String[] desc = {"flora.key.Ender", "flora.key.Fire"};
	private static final int[] keyValues = { Keyboard.KEY_I, Keyboard.KEY_K};
	private final KeyBinding[] keys;
	public KeyHandlerEnder() {
		keys = new KeyBinding[desc.length];
		for (int i = 0; i < desc.length; ++i) {
			keys[i] = new KeyBinding(desc[i], keyValues[i], "flora.key.category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			for(int i=0;i<2;i++){
				if (keys[i].isPressed()) {
					ByteBuf data = Unpooled.buffer(4);
					data.writeInt(i);
					C17PacketCustomPayload packet = new C17PacketCustomPayload(ConstantsFLORA.modId, data);
					EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
					player.sendQueue.addToSendQueue(packet);
				}
			}
		}
	}
}