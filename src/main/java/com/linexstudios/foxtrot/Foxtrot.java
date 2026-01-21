package com.linexstudios.foxtrot;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "foxtrot", name = "Foxtrot", version = "0.0.3-E", acceptedMinecraftVersions = "[1.8.9]")
public class Foxtrot {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 1. Load the enemy list and toggle settings from your .txt and .cfg files
        ConfigHandler.loadConfig();
        
        // 2. Register the HUD to the Forge Event Bus so it can draw on your screen
        MinecraftForge.EVENT_BUS.register(new EnemyHUD());
        
        // 3. Register the /foxtrot command so the game recognizes it
        ClientCommandHandler.instance.registerCommand(new CommandFoxtrot());
        
        // Console log to confirm everything loaded correctly for 0.0.2
        System.out.println("[Foxtrot] Version 0.0.3 initialized successfully!");
    }
}