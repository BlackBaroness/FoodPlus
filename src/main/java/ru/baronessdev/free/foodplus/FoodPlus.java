package ru.baronessdev.free.foodplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.free.foodplus.metrics.Metrics;

import java.util.List;

public final class FoodPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Metrics(this, 11263);
        load();
    }

    private void load() {
        log("Loading...");
        FileConfiguration cfg = getConfig();

        int loaded = 0;

        for (String id : cfg.getKeys(false)) {
            String maybeMaterial = cfg.getString(id + ".material");
            Material material = Material.getMaterial(maybeMaterial);
            if (material == null) {
                log(ChatColor.RED + "Can't get material (" + maybeMaterial + ")! Handler with id " + id + " is passing.");
                continue;
            }

            int chance = cfg.getInt(id + ".chance");

            List<String> execute = cfg.getStringList(id + ".execute");
            if (execute.isEmpty()) {
                log(ChatColor.RED + "Empty execute query! Handler with id " + id + " is passing.");
                continue;
            }

            Bukkit.getPluginManager().registerEvents(new FoodListener(new FoodTask(material, chance, execute)), this);
            loaded++;
        }

        log(loaded + " listeners successfully activated.");
    }

    private void log(String s) {
        System.out.println(ChatColor.LIGHT_PURPLE + "[FoodPlus] " + ChatColor.WHITE + s);
    }
}
