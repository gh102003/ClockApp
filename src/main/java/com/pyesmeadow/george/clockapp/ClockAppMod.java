package com.pyesmeadow.george.clockapp;

import com.mrcrayfish.device.api.ApplicationManager;
import com.pyesmeadow.george.clockapp.app.ApplicationClock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION, dependencies = Reference.DEPENDENCIES)
public class ClockAppMod {

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerApplications();
	}

	private void registerApplications()
	{
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "clock"), ApplicationClock.class);
	}
}
