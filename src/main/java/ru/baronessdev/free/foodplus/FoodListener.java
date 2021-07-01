package ru.baronessdev.free.foodplus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class FoodListener implements Listener {

    private final FoodTask task;

    public FoodListener(FoodTask task) {
        this.task = task;
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent e) {
        if (!e.getItem().getType().equals(task.getMaterial())) return;
        if (!isLucky()) return;

        Player p = e.getPlayer();
        task.getExecute().forEach(s -> {
            s = replace(s, p);

            if (s.startsWith("NARRATE ")) {
                p.sendMessage(s.substring(8));
            } else {
                runCommand(s);
            }
        });
    }

    private boolean isLucky() {
        // method NOT including bound so we using 101 instead 100
        return ThreadLocalRandom.current().nextInt(1, 101) <= task.getChance();
    }

    private void runCommand(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
    }

    private String replace(String s, Player p) {
        return s.replace("{PLAYER}", p.getName()).replace("NARRATE ", "");
    }
}
