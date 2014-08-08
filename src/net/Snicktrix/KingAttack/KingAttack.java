package net.Snicktrix.KingAttack;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Luke on 8/6/14.
 */
public class KingAttack extends JavaPlugin {
    public Events events;
    public ConfigData configData;
    public GameManager gameManager;

    @Override
    public void onEnable() {
        //First, lets set up our Event handler
        this.events = new Events(this);
        Bukkit.getPluginManager().registerEvents(events, this);

        //Next we will setup our config l
        this.configData = new ConfigData(this);

        //TODO Pull Map Details from config
        //Make sure to use ConfigData load method

        //TODO Create gameManager instance with new Map

        //Finished!
        System.out.println("KingAttack successfully loaded");
    }

}
