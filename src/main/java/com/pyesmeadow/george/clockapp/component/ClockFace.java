package com.pyesmeadow.george.clockapp.component;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.object.Picture;
import jline.internal.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ClockFace extends Component {

	private static final int HAND_WIDTH = 5;
	private static final int SECOND_HAND_WIDTH = 3;
	private static final int HOUR_HAND_LENGTH = 18;
	private static final int MINUTE_HAND_LENGTH = 30;
	private static final int SECOND_HAND_LENGTH = 30;
	private static final int HAND_OVERLAP = 5;

	private static final int FACE_SIZE = 64;
	private static final int BORDER_WIDTH = 2;
	@Nullable
	private Picture clockFace;

	private long time;

	public ClockFace(int left, int top, @Nullable Picture clockFace)
	{
		super(left, top);

		this.clockFace = clockFace;
	}

	@Override
	public void handleTick()
	{
		super.handleTick();

		time = Minecraft.getMinecraft().world.getWorldTime();
	}

	@Override
	public void render(Laptop laptop,
					   Minecraft mc,
					   int x,
					   int y,
					   int mouseX,
					   int mouseY,
					   boolean windowActive,
					   float partialTicks)
	{
		// Clock Face
		if (clockFace != null)
		{
			int pixelWidth = Math.floorDiv(FACE_SIZE, clockFace.getWidth());
			int pixelHeight = Math.floorDiv(FACE_SIZE, clockFace.getHeight());

			Gui.drawRect(xPosition - 1,
					yPosition - 1,
					xPosition + clockFace.getWidth() * pixelWidth + 1,
					yPosition + clockFace.getHeight() * pixelHeight + 1,
					Color.DARK_GRAY.getRGB());
			Gui.drawRect(xPosition,
					yPosition,
					xPosition + clockFace.getWidth() * pixelWidth,
					yPosition + clockFace.getHeight() * pixelHeight,
					Color.WHITE.getRGB());
			for (int i = 0; i < clockFace.getHeight(); i++)
			{
				for (int j = 0; j < clockFace.getWidth(); j++)
				{
					int pixelX = xPosition + j * pixelWidth;
					int pixelY = yPosition + i * pixelHeight;
					Gui.drawRect(pixelX, pixelY, pixelX + pixelWidth, pixelY + pixelHeight, clockFace.pixels[j][i]);
				}
			}
		}
		else
		{
			// Background
			drawRect(this.xPosition - BORDER_WIDTH,
					this.yPosition - BORDER_WIDTH,
					this.xPosition + FACE_SIZE + BORDER_WIDTH,
					this.yPosition + FACE_SIZE + BORDER_WIDTH,
					Color.DARK_GRAY.getRGB());
			drawRect(this.xPosition, this.yPosition, this.xPosition + FACE_SIZE, this.yPosition + FACE_SIZE, Color.GRAY.getRGB());

		}

		// Hour hand
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.xPosition + FACE_SIZE / 2, this.yPosition + FACE_SIZE / 2, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(calculateHourAngleFromTime(time), 0.0F, 0.0F, 1.0F);
		drawRect(-(HAND_WIDTH / 2), -HAND_OVERLAP, HAND_WIDTH / 2, HOUR_HAND_LENGTH, Color.DARK_GRAY.getRGB());
		GlStateManager.popMatrix();

		// Minute hand
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.xPosition + FACE_SIZE / 2, this.yPosition + FACE_SIZE / 2, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(calculateMinuteAngleFromTime(time), 0.0F, 0.0F, 1.0F);
		drawRect(-(HAND_WIDTH / 2), -HAND_OVERLAP, HAND_WIDTH / 2, MINUTE_HAND_LENGTH, Color.DARK_GRAY.getRGB());
		GlStateManager.popMatrix();

		// Second hand
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.xPosition + FACE_SIZE / 2, this.yPosition + FACE_SIZE / 2, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(calculateSecondAngleFromTime(time), 0.0F, 0.0F, 1.0F);
		drawRect(-(SECOND_HAND_WIDTH / 2), -HAND_OVERLAP, SECOND_HAND_WIDTH / 2, SECOND_HAND_LENGTH, Color.RED.getRGB());
		GlStateManager.popMatrix();
	}

	public void changeClockFace(@Nullable Picture clockFace)
	{
		this.clockFace = clockFace;
	}

	private float calculateHourAngleFromTime(long time)
	{
		float hours = ((float) time / 1000.0F + 7.0F) % 12;
		return hours * 30;
	}

	private float calculateMinuteAngleFromTime(long time)
	{
		return (float) (time % 1000L) / 1000.0F * 360.0F;
	}

	private float calculateSecondAngleFromTime(long time)
	{
		return (float) (time % (1000L / 60) / (1000.0F / 60) * 360.0F);
	}
}
