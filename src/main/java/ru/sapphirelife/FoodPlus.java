package ru.sapphirelife;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public final class FoodPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }

        checkUpdates();
        load();
    }

    private void load() {
        log("Loading...");
        FileConfiguration cfg = getConfig();
        int i = 1;
        int progress = 0;

        while (!cfg.getStringList(String.valueOf(i)).toString().equals("[]")) {
            List<String> queue = cfg.getStringList(String.valueOf(i));
            i++;

            Material material = Material.getMaterial(queue.get(0));
            if (material == null) {
                log(ChatColor.RED + "Can't get material (" + queue.get(0) + ")! Handler with id " + (i - 1) + " is passing.");
                continue;
            }

            int chance;
            try {
                chance = Integer.parseInt(queue.get(1));
            } catch (NumberFormatException | NullPointerException e) {
                log(ChatColor.RED + "Can't load chance! Handler with id " + (i - 1) + " is passing.");
                continue;
            }

            queue.remove(0);
            queue.remove(0);

            Executor executor = new Executor(queue, material, chance, cfg.getBoolean("debug"));
            Bukkit.getPluginManager().registerEvents(executor, this);
            progress++;
        }
        log(progress + " listeners successfully activated.");
    }

    private void log(String s) {
        System.out.println(ChatColor.LIGHT_PURPLE + "[FoodPlus] " + ChatColor.WHITE + s);
    }

    private void checkUpdates() {
        try {
            URL updateUrl = new URL("https://github.com/SiriusWhite74/FoodPlus/blob/master/FoodPlus.jar?raw=true");
            URLConnection urlConnection = updateUrl.openConnection();
            urlConnection.connect();
            int actual = urlConnection.getContentLength();
            int now = (int) getFile().length();
            if (actual != now) {
                log(ChatColor.YELLOW + "New update available! I advise you to download it: ");
                log(ChatColor.YELLOW + "https://devmc.ru/resources/foodplus.23/download");
            }
        } catch (Exception ignored) {
        }
    }

}
