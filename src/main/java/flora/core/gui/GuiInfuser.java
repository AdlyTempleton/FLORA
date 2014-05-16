package flora.core.gui;

import flora.core.Constants;
import flora.core.block.TileInfuser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiInfuser extends GuiContainer {
	TileInfuser tileInfuser;
	public GuiInfuser(TileInfuser tile, InventoryPlayer inventoryPlayer) {
		super(new ContainerInfuser(inventoryPlayer, tile));
		tileInfuser=tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation(Constants.GUI_INFUSER_TEX));
		this.drawTexturedModalRect(42, 25, 0, ySize, 104, 14);
		ArrayList<FluidTank> tanks= tileInfuser.getTotalFluidTank();
		int total=tileInfuser.getTotalFluidAmount();
		int currentX=44;
		for(FluidTank tank:tanks){
			if(tank.getFluid()!=null){
				this.mc.renderEngine.bindTexture(new ResourceLocation(Constants.PREFIX_MOD+"textures/fluid/"+tank.getFluid().getFluid().getName()+".png"));
				float size=1F*tank.getFluidAmount();
				size/=total;
				size*=100;
				drawRectangleXRepeated(currentX, 27, 16, 16, 256, 256, (int)size, 10, 16, 1);
				currentX+=(int)size;
			}
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation(Constants.GUI_INFUSER_TEX));
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	//This method thanks to Paleocrafter
	public static void drawRectangleXRepeated(int x, int y, float u, float v, float uMax, float vMax, int width, int height, int tileWidth, int zLevel)
	{
		float uvHeight = v - vMax;
		int numX = (int) Math.ceil((float) width / tileWidth);

		for (int x2 = 0; x2 < numX; ++x2)
		{
			int w = tileWidth;

			float tileMaxU = uMax;

			int tileX = w * x2;

			if (tileWidth > width)
			{
				w = width;
				tileMaxU -= 0.00390625F * (float) w / tileWidth;
				tileX = w * x2;
			}
			else if (x2 == numX - 1)
			{
				if (tileWidth > width - x2 * tileWidth)
				{
					w = width - x2 * tileWidth;
					tileMaxU -= 0.00390625F * (float) w / tileWidth;
					tileX = tileWidth * x2;
				}
			}

			drawRectangleStretched(x + tileX, y, u, v, w, height, tileMaxU, vMax, zLevel);
		}
	}

	public static void drawRectangleStretched(int x, int y, float u, float v, int width, int height, float uMax, float vMax, int zLevel)
	{
		float scaleU = 0.00390625F;
		float scaleV = 0.00390625F;
		if (u % 1 != 0 || uMax % 1 != 0) scaleU = 1;
		if (v % 1 != 0 || vMax % 1 != 0) scaleV = 1;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x, y + height, zLevel, u * scaleU, vMax * scaleV);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, uMax * scaleU, vMax * scaleV);
		tessellator.addVertexWithUV(x + width, y, zLevel, uMax * scaleU, v * scaleV);
		tessellator.addVertexWithUV(x, y, zLevel, u * scaleU, v * scaleV);
		tessellator.draw();
	}
}

