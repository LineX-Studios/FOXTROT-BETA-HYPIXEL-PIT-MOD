package com.linexstudios.foxtrot;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandFoxtrot extends CommandBase {
    @Override
    public String func_71517_b() { return "foxtrot"; }

    @Override
    public String func_71518_a(ICommandSender sender) { 
        return "/foxtrot <add|remove|list|notify|toggle|clear>"; 
    }

    @Override
    public int func_82362_a() { return 0; }

    @Override
    public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD.toString() + "[Foxtrot Help Menu]"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot add [name] " + EnumChatFormatting.GRAY + "- Add enemy"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot remove [name] " + EnumChatFormatting.GRAY + "- Remove enemy"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot list " + EnumChatFormatting.GRAY + "- View your enemy list"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot notify " + EnumChatFormatting.GRAY + "- Toggle chat alerts"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot toggle " + EnumChatFormatting.GRAY + "- Toggle screen HUD"));
            sender.func_145747_a(new ChatComponentText(EnumChatFormatting.YELLOW + "/foxtrot clear " + EnumChatFormatting.GRAY + "- Clear your list"));
            return;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                if (args.length < 2) return;
                String nameToAdd = args[1];
                if (!EnemyHUD.targetList.contains(nameToAdd)) {
                    EnemyHUD.targetList.add(nameToAdd);
                    ConfigHandler.saveConfig();
                    sendMessage(sender, EnumChatFormatting.GREEN + "Added " + EnemyHUD.getFormattedName(nameToAdd) + EnumChatFormatting.GREEN + " to your list.");
                }
                break;

            case "remove":
                if (args.length < 2) return;
                String nameToRemove = args[1];
                if (EnemyHUD.targetList.removeIf(n -> n.equalsIgnoreCase(nameToRemove))) {
                    ConfigHandler.saveConfig();
                    sendMessage(sender, EnumChatFormatting.RED + "Removed " + EnemyHUD.getFormattedName(nameToRemove) + EnumChatFormatting.RED + " from your list.");
                }
                break;

            case "list":
                if (EnemyHUD.targetList.isEmpty()) {
                    sendMessage(sender, EnumChatFormatting.RED + "Your list is empty.");
                } else {
                    sender.func_145747_a(new ChatComponentText(EnumChatFormatting.AQUA + "--- Enemy List (" + EnemyHUD.targetList.size() + ") ---"));
                    for (String name : EnemyHUD.targetList) {
                        sender.func_145747_a(new ChatComponentText(EnumChatFormatting.GRAY + "- " + EnemyHUD.getFormattedName(name)));
                    }
                }
                break;

            case "notify":
                EnemyHUD.notificationsEnabled = !EnemyHUD.notificationsEnabled;
                ConfigHandler.saveConfig();
                sendMessage(sender, EnumChatFormatting.YELLOW + "Alerts: " + (EnemyHUD.notificationsEnabled ? "ON" : "OFF"));
                break;

            case "toggle":
                EnemyHUD.enabled = !EnemyHUD.enabled;
                ConfigHandler.saveConfig();
                sendMessage(sender, EnumChatFormatting.YELLOW + "HUD: " + (EnemyHUD.enabled ? "ON" : "OFF"));
                break;

            case "clear":
                EnemyHUD.targetList.clear();
                ConfigHandler.saveConfig();
                sendMessage(sender, EnumChatFormatting.RED + "List cleared.");
                break;
        }
    }

    @Override
    public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return func_71530_a(args, "add", "remove", "list", "notify", "toggle", "clear");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            return func_71530_a(args, Minecraft.func_71410_x().func_147114_u().func_175106_d().stream()
                    .map(info -> info.getGameProfile().getName()).toArray(String[]::new));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return func_175762_a(args, EnemyHUD.targetList);
        }
        return null;
    }

    private void sendMessage(ICommandSender sender, String msg) {
        sender.func_145747_a(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.WHITE + "Foxtrot" + EnumChatFormatting.DARK_GRAY + "] " + msg));
    }
}