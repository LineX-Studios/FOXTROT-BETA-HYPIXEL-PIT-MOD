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
    private final Minecraft mc = Minecraft.func_71410_x();
    public static List<String> targetList = new ArrayList<String>();
    public static Set<String> playersInLobby = new HashSet<>();
    public static boolean enabled = true;
    public static boolean notificationsEnabled = true;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.field_71441_e == null || event.phase != TickEvent.Phase.END) return;

        if (mc.field_71439_g.field_70173_aa % 20 == 0) {
            Collection<NetworkPlayerInfo> players = mc.func_147114_u().func_175106_d();
            Set<String> currentNames = new HashSet<>();

            for (NetworkPlayerInfo info : players) {
                String name = info.func_178845_a().getName();
                currentNames.add(name);

                if (targetList.stream().anyMatch(name::equalsIgnoreCase)) {
                    if (!playersInLobby.contains(name)) {
                        if (notificationsEnabled) {
                            mc.field_71439_g.func_145747_a(new ChatComponentText(
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

        FontRenderer fr = mc.field_71466_p;
        int x = 5;
        int y = 50; 
        
        boolean hasVisibleEnemy = false;
        
        for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
            if (targetList.stream().anyMatch(player.getName()::equalsIgnoreCase)) {
                
                if (!hasVisibleEnemy) {
                    fr.func_175063_a(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD.toString() + "Enemies:", x, y, 0xFFFFFF);
                    y += fr.field_78288_b + 2;
                    hasVisibleEnemy = true;
                }

                // Get formatted name (Prestige + Rank)
                String fullDisplayName = getFormattedName(player.func_70005_c_());
                
                // Render as Bold to match your style
                fr.func_175063_a(EnumChatFormatting.BOLD.toString() + fullDisplayName, x, y, 0xFFFFFF);
                y += fr.field_78288_b;
            }
        }
    }

    public static String getFormattedName(String name) {
        // First check Tab List (Highest priority for Prestige)
        if (Minecraft.func_71410_x().func_147114_u() != null) {
            for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
                if (info.func_178845_a().getName().equalsIgnoreCase(name)) {
                    if (info.func_178854_k() != null) {
                        return info.func_178854_k().func_150254_d();
                    }
                }
            }
        }
        
        // Secondary check: World Entity (If Tab hasn't updated)
        EntityPlayer player = Minecraft.func_71410_x().field_71441_e.func_72924_a(name);
        if (player != null) {
            return player.func_145748_c_().func_150254_d();
        }

        return EnumChatFormatting.GRAY + name;
    }
}