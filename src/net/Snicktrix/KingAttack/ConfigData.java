package net.Snicktrix.KingAttack;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

/**
 * Legacy Config Class made by Luke on 8/1/14.
 */
public class ConfigData {
    private KingAttack kingAttack;

    private ArrayList<String> admins = new ArrayList<String>();
    private ArrayList<String> mods = new ArrayList<String>();
    private ArrayList<String> assistants = new ArrayList<String>();

    private FileConfiguration config;

    public ConfigData (KingAttack kingAttack) {
        this.kingAttack = this.kingAttack;
        this.config = this.kingAttack.getConfig();

        this.loadConfig();
    }

    private void loadConfig() {
        //Set up the config
        this.kingAttack.getConfig().options().copyDefaults(true);
        this.kingAttack.saveDefaultConfig();

    }

}

