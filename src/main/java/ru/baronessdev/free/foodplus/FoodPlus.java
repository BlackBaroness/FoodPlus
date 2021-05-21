package ru.baronessdev.free.foodplus;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.free.foodplus.util.UpdateCheckerUtil;
import ru.baronessdev.free.foodplus.util.logging.LogType;
import ru.baronessdev.free.foodplus.util.logging.Logger;

import java.util.List;

public final class FoodPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Metrics(this, 11263);
        load();
        checkUpdates();
    }

    private void load() {
        Logger.log(LogType.INFO, "Loading...");
        FileConfiguration cfg = getConfig();

        int loaded = 0;

        for (String id : cfg.getKeys(false)) {
            String maybeMaterial = cfg.getString(id + ".material");
            Material material = Material.getMaterial(maybeMaterial);
            if (material == null) {
                Logger.log(LogType.ERROR, "Can't get material (" + maybeMaterial + ")! Handler with id " + id + " is passing.");
                continue;
            }

            int chance = cfg.getInt(id + ".chance");

            List<String> execute = cfg.getStringList(id + ".execute");
            if (execute.isEmpty()) {
                Logger.log(LogType.ERROR, "Empty execute query! Handler with id " + id + " is passing.");
                continue;
            }

            Bukkit.getPluginManager().registerEvents(new FoodListener(new FoodTask(material, chance, execute)), this);
            loaded++;
        }

        Logger.log(LogType.INFO, loaded + " listeners successfully activated.");
    }

    private void checkUpdates() {
        try {
            int i = UpdateCheckerUtil.check(this);
            if (i != -1) {
                Logger.log(LogType.INFO, "New version found: v" + ChatColor.YELLOW + i + ChatColor.GRAY + " (Current: v" + getDescription().getVersion() + ")");
                Logger.log(LogType.INFO, "Update now: " + ChatColor.AQUA + "market.baronessdev.ru/shop/licenses/");
            }
        } catch (UpdateCheckerUtil.UpdateCheckException e) {
            Logger.log(LogType.ERROR, "Could not check for updates: " + e.getRootCause());
            Logger.log(LogType.ERROR, "Please contact Baroness's Dev if this isn't your mistake.");
        }
    }
}
