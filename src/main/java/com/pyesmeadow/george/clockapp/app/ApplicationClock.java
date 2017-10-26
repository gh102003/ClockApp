package com.pyesmeadow.george.clockapp.app;

import com.mrcrayfish.device.api.app.*;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ComboBox;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.object.Picture;
import com.pyesmeadow.george.clockapp.component.ClockFace;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Function;

public class ApplicationClock extends Application {

	private Layout layoutMain;
	private Label labelTime;
	private ClockFace clockFace;
	private Button btnOptions;

	private Layout layoutOptions;
	private Button btnBack;
	private Label labelOptions;
	private ComboBox<TimeFormatting> comboTimeFormatting;
	private Button btnChooseClockFace;
	private Button btnResetClockFace;

	private TimeFormatting timeFormatting;

	@Override
	public void init()
	{
		timeFormatting = TimeFormatting.TWELVE_HOUR;

		// ==================================== MAIN ====================================
		layoutMain = new Layout(88, 130);

		labelTime = new Label("", 44, 10) {
			@Override
			public void handleTick()
			{
				super.handleTick();
				setText(timeFormatting.formatter.apply(Minecraft.getMinecraft().world.getWorldTime()));
			}
		};
		labelTime.setAlignment(Component.ALIGN_CENTER);
		layoutMain.addComponent(labelTime);


		clockFace = new ClockFace(12, 26, null);
		layoutMain.addComponent(clockFace);


		btnOptions = new Button(10, 100, 68, 20, "Options", Icon.WRENCH);
		btnOptions.setClickListener((Component component, int mouseButton) ->
		{
			if (mouseButton == 0)
			{
				setCurrentLayout(layoutOptions);
			}
		});
		layoutMain.addComponent(btnOptions);

		// ==================================== OPTIONS ====================================
		layoutOptions = new Layout(88, 93);

		btnBack = new Button(5, 5, Icon.ARROW_LEFT);
		btnBack.setClickListener((Component component, int mouseButton) ->
		{
			if (mouseButton == 0)
			{
				setCurrentLayout(layoutMain);
			}
		});
		btnBack.setToolTip("Back", "Go back to the clock");
		layoutOptions.addComponent(btnBack);

		labelOptions = new Label("Options", 44, 9);
		labelOptions.setAlignment(Component.ALIGN_CENTER);
		layoutOptions.addComponent(labelOptions);

		comboTimeFormatting = new ComboBox.List<>(5, 25, 78, TimeFormatting.values());
		comboTimeFormatting.setChangeListener((oldValue, newValue) ->
		{
			timeFormatting = newValue;
		});
		layoutOptions.addComponent(comboTimeFormatting);

		btnChooseClockFace = new Button(5, 43, 78, 20, "Choose face");
		btnChooseClockFace.setToolTip("Choose clock face", "Choose a Pixel Painter file to set as your clock face");
		btnChooseClockFace.setClickListener((c, mouseButton) ->
		{
			Dialog.OpenFile dialog = new Dialog.OpenFile(this);
			dialog.setResponseHandler((success, file) ->
			{
				if (file.getOpeningApp() != null && file.getOpeningApp().equals("cdm.pixel_painter"))
				{
					Picture picture = Picture.fromFile(file);
					clockFace.changeClockFace(picture);
					return true;
				}
				else
				{
					Dialog.Message dialog2 = new Dialog.Message("Invalid image file. It must be created in Pixel Painter");
					openDialog(dialog2);
				}
				return false;
			});
			openDialog(dialog);
			markDirty();
		});
		layoutOptions.addComponent(btnChooseClockFace);

		btnResetClockFace = new Button(5, 68, 78, 20, "Reset face");
		btnResetClockFace.setToolTip("Reset clock face", "Reset your clock face to the default.");
		btnResetClockFace.setClickListener((c, mouseButton) ->
		{
			clockFace.changeClockFace(null);
			markDirty();
		});
		layoutOptions.addComponent(btnResetClockFace);

		setCurrentLayout(layoutMain);
	}

	@Override
	public void load(NBTTagCompound nbtTagCompound)
	{

	}

	@Override
	public void save(NBTTagCompound nbtTagCompound)
	{

	}

	private enum TimeFormatting {
		TWELVE_HOUR("12 hour", time ->
		{
			int hours = (int) ((Math.floor((double) time / 1000.0D) + 7.0D)) % 24;
			int minutes = (int) Math.floor((double) (time % 1000L) / 1000.0D * 60.0D);
			String suffix = hours < 12 ? "am" : "pm";

			hours %= 12;

			if (hours == 0)
			{
				hours = 12;
			}

			return String.format("%02d:%02d%s", hours, minutes, suffix);
		}), TWENTY_FOUR_HOUR("24 hour", time ->
		{
			int hours = (int) ((Math.floor((double) time / 1000.0D) + 7.0D)) % 24;
			int minutes = (int) Math.floor((double) (time % 1000L) / 1000.0D * 60.0D);

			return String.format("%02d:%02d", hours, minutes);
		}), TWENTY_FOUR_HOUR_WITH_SECONDS("24 hour with seconds", time ->
		{
			int hours = (int) ((Math.floor((double) time / 1000.0D) + 7.0D)) % 24;
			int minutes = (int) Math.floor((double) (time % 1000L) / 1000.0D * 60.0D);
			int seconds = (int) Math.floor(time % (1000L / 60) / (1000.0F / 60) * 60);

			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		});

		private final String name;
		/**
		 * Takes a time value (in ticks) and returns a time-formatted String
		 */
		private final Function<Long, String> formatter;

		TimeFormatting(String name, Function<Long, String> formatter)
		{
			this.name = name;
			this.formatter = formatter;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
