package ru.baronessdev.free.foodplus;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.lib.common.log.LogLevel;
import ru.baronessdev.lib.common.log.Logger;
import ru.baronessdev.lib.common.update.UpdateCheckerUtil;

import java.util.List;

public final class FoodPlus extends JavaPlugin {

    private Logger logger;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Metrics(this, 11263);
        logger = new Logger.Builder("[FoodPlus]").build();
        load();
        checkUpdates();
    }

    private void load() {
        logger.log(LogLevel.INFO, "Loading...");
        FileConfiguration cfg = getConfig();

        int loaded = 0;

        for (String id : cfg.getKeys(false)) {
            String maybeMaterial = cfg.getString(id + ".material");
            Material material = Material.getMaterial(maybeMaterial);
            if (material == null) {
                logger.log(LogLevel.ERROR, "Can't get material (" + maybeMaterial + ")! Handler with id " + id + " is passing.");
                continue;
            }

            int chance = cfg.getInt(id + ".chance");

            List<String> execute = cfg.getStringList(id + ".execute");
            if (execute.isEmpty()) {
                logger.log(LogLevel.ERROR, "Empty execute query! Handler with id " + id + " is passing.");
                continue;
            }

            Bukkit.getPluginManager().registerEvents(new FoodListener(new FoodTask(material, chance, execute)), this);
            loaded++;
        }

        logger.log(LogLevel.INFO, loaded + " listeners successfully activated.");
    }

    private void checkUpdates() {
        UpdateCheckerUtil.checkAsynchronously(this, "https://market.baronessdev.ru/shop/foodplus.4/", logger);
    }
}
