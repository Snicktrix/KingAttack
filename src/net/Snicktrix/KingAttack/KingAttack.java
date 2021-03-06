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

		//Config preparation
		this.saveDefaultConfig();

        //Next we will setup our config
        this.configData = new ConfigData(this);

        Map map = this.configData.generateMapFromConfig();

		this.gameManager = new GameManager(this, map, 2, 10);

		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //Finished!
        System.out.println("KingAttack successfully loaded");
    }

}
