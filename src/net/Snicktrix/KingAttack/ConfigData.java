package net.Snicktrix.KingAttack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;

/**
 * Legacy Config Class made by Luke on 8/1/14.
 */
public class ConfigData {
    private KingAttack kingAttack;

    public ConfigData (KingAttack kingAttack) {
        this.kingAttack = kingAttack;

    }


    public Map generateMapFromConfig() {

		//Debug logging
		for (String str : this.kingAttack.getConfig().getKeys(false)) {
			//Check if its a string
			//Only string is the world name
			if (str.equalsIgnoreCase("WorldName")) {
				System.out.println("WorldName: " + kingAttack.getConfig().getString(str));
			} else {
				System.out.println(str + ": " + kingAttack.getConfig().getDouble(str));
			}
		}

		//These are the X and Z values for the boundaries
		//We will use these to calculate team areas
		String worldName = this.kingAttack.getConfig().getString("WorldName");

		double buildEdge1X = this.kingAttack.getConfig().getDouble("BuildEdge1X");
		double buildEdge1Z = this.kingAttack.getConfig().getDouble("BuildEdge1Z");

		double buildEdge2X = this.kingAttack.getConfig().getDouble("BuildEdge2X");
		double buildEdge2Z = this.kingAttack.getConfig().getDouble("BuildEdge2Z");

		//Spawn Points
		double blueSpawnX = this.kingAttack.getConfig().getDouble("BlueSpawnX");
		double blueSpawnY = this.kingAttack.getConfig().getDouble("BlueSpawnY");
		double blueSpawnZ = this.kingAttack.getConfig().getDouble("BlueSpawnZ");

		double redSpawnX = this.kingAttack.getConfig().getDouble("RedSpawnX");
		double redSpawnY = this.kingAttack.getConfig().getDouble("RedSpawnY");
		double redSpawnZ = this.kingAttack.getConfig().getDouble("RedSpawnZ");

		double spectatorSpawnX = this.kingAttack.getConfig().getDouble("SpectatorSpawnX");
		double spectatorSpawnY = this.kingAttack.getConfig().getDouble("SpectatorSpawnY");
		double spectatorSpawnZ = this.kingAttack.getConfig().getDouble("SpectatorSpawnZ");

		//Enable World
		Bukkit.getServer().createWorld(new WorldCreator(worldName));

		//Make sure world does not auto save
		Bukkit.getWorld(worldName).setAutoSave(false);

		//Create Locations for each Spawn Point
		//We will use the default world
		Location blueTeamSpawn = new Location(Bukkit.getWorld(worldName), blueSpawnX, blueSpawnY, blueSpawnZ);
		Location redTeamSpawn = new Location(Bukkit.getWorld(worldName), redSpawnX, redSpawnY, redSpawnZ);
		Location spectatorSpawn = new Location(Bukkit.getWorld(worldName), spectatorSpawnX, spectatorSpawnY, spectatorSpawnZ);

		//Now create a map object
		Map map = new Map(worldName, blueTeamSpawn, redTeamSpawn, buildEdge1X, buildEdge1Z, buildEdge2X, buildEdge2Z, spectatorSpawn);

		//Finally, lets return the map
		return map;


    }

}

