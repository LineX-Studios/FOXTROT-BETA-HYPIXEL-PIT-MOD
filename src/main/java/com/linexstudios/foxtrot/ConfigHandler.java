package com.linexstudios.foxtrot;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private static final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/foxtrot_enemies.txt");
    private static final File settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "config/foxtrot_settings.cfg");

    public static void loadConfig() {
        try {
            // Load Enemy Names List
            if (!configFile.exists()) {
                configFile.createNewFile();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                List<String> loadedNames = new ArrayList<String>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        loadedNames.add(line.trim());
                    }
                }
                EnemyHUD.targetList = loadedNames;
                reader.close();
            }

            // Load Settings (Toggles)
            if (settingsFile.exists()) {
                BufferedReader settingsReader = new BufferedReader(new FileReader(settingsFile));
                
                // Line 1: Notifications Toggle
                String notifyLine = settingsReader.readLine();
                if (notifyLine != null) {
                    EnemyHUD.notificationsEnabled = Boolean.parseBoolean(notifyLine);
                }
                
                // Line 2: HUD Toggle
                String hudLine = settingsReader.readLine();
                if (hudLine != null) {
                    EnemyHUD.enabled = Boolean.parseBoolean(hudLine);
                }
                
                settingsReader.close();
            }
        } catch (Exception e) {
            System.err.println("[Foxtrot] Failed to load config!");
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            // Save Enemy Names List
            PrintWriter writer = new PrintWriter(new FileWriter(configFile));
            for (String name : EnemyHUD.targetList) {
                writer.println(name);
            }
            writer.close();

            // Save Settings (Toggles)
            PrintWriter settingsWriter = new PrintWriter(new FileWriter(settingsFile));
            // We save these on separate lines for easy reading
            settingsWriter.println(EnemyHUD.notificationsEnabled);
            settingsWriter.println(EnemyHUD.enabled);
            settingsWriter.close();
            
        } catch (Exception e) {
            System.err.println("[Foxtrot] Failed to save config!");
            e.printStackTrace();
        }
    }
}