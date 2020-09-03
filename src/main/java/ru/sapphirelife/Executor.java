package ru.sapphirelife;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayList;
import java.util.List;

public class Executor implements Listener {

    private final List<String> commands;
    private final Material food;
    private final int chance;
    private final boolean log;

    public Executor(List<String> commands, Material food, int chance, boolean log) {
        this.commands = commands;
        this.food = food;
        this.chance = chance;
        this.log = log;
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem().getType().equals(food)) {
            int random = (int) (Math.random() * chance);

            if (random == chance - 1) {
                Player p = e.getPlayer();
                List<String> queue = new ArrayList<>(commands);

                for (String cmd : queue) {
                    if (cmd.contains("NARRATE ")) {
                        String s = replace(cmd, p);
                        p.sendMessage(s);
                        if (log) log(ChatColor.GREEN + "NARRATING: " + ChatColor.GRAY + s);
                        continue;
                    }

                    runCommand(replace(cmd, p));
                }
            }
        }
    }

    private void runCommand(String cmd) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, cmd);
        if (log) log(ChatColor.GREEN + "EXECUTING: " + ChatColor.GRAY + cmd);
    }

    private String replace(String s, Player p) {
        return s.replace("{PLAYER}", p.getName()).replace("NARRATE ", "");
    }

    private void log(String s) {
        System.out.println(ChatColor.LIGHT_PURPLE + "[FoodPlus] " + ChatColor.WHITE + s);
    }
}
