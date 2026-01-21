package com.linexstudios.foxtrot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class EnemyHUD {
    private final Minecraft mc = Minecraft.getMinecraft();
    public static List<String> targetList = new ArrayList<String>();
    public static Set<String> playersInLobby = new HashSet<>();
    public static boolean enabled = true;
    public static boolean notificationsEnabled = true;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null || event.phase != TickEvent.Phase.END) return;

        if (mc.thePlayer.ticksExisted % 20 == 0) {
            Collection<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();
            Set<String> currentNames = new HashSet<>();

            for (NetworkPlayerInfo info : players) {
                String name = info.getGameProfile().getName();
                currentNames.add(name);

                if (targetList.stream().anyMatch(name::equalsIgnoreCase)) {
                    if (!playersInLobby.contains(name)) {
                        if (notificationsEnabled) {
                            mc.thePlayer.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.WHITE + "Foxtrot" + EnumChatFormatting.DARK_GRAY + "] " +
                                EnumChatFormatting.RED + EnumChatFormatting.BOLD + "ALERT: " + 
                                getFormattedName(name) + EnumChatFormatting.YELLOW + " has entered your lobby!"
                            ));
                        }
                    }
                }
            }
            playersInLobby = currentNames;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !enabled) return;

        FontRenderer fr = mc.fontRendererObj;
        int x = 5;
        int y = 50; 
        
        boolean hasVisibleEnemy = false;
        
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (targetList.stream().anyMatch(player.getName()::equalsIgnoreCase)) {
                
                if (!hasVisibleEnemy) {
                    fr.drawStringWithShadow(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD.toString() + "Enemies:", x, y, 0xFFFFFF);
                    y += fr.FONT_HEIGHT + 2;
                    hasVisibleEnemy = true;
                }

                // Get formatted name (Prestige + Rank)
                String fullDisplayName = getFormattedName(player.getName());
                
                // Render as Bold to match your style
                fr.drawStringWithShadow(EnumChatFormatting.BOLD.toString() + fullDisplayName, x, y, 0xFFFFFF);
                y += fr.FONT_HEIGHT;
            }
        }
    }

    public static String getFormattedName(String name) {
        // First check Tab List (Highest priority for Prestige)
        if (Minecraft.getMinecraft().getNetHandler() != null) {
            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                if (info.getGameProfile().getName().equalsIgnoreCase(name)) {
                    if (info.getDisplayName() != null) {
                        return info.getDisplayName().getFormattedText();
                    }
                }
            }
        }
        
        // Secondary check: World Entity (If Tab hasn't updated)
        EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(name);
        if (player != null) {
            return player.getDisplayName().getFormattedText();
        }

        return EnumChatFormatting.GRAY + name;
    }
}