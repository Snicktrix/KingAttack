package net.Snicktrix.KingAttack;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
			System.out.println(str + ": " + kingAttack.getConfig().getDouble(str));
		}

		//These are the X and Z values for the boundaries
		//We will use these to calculate team areas
		double blueEdge1X = this.kingAttack.getConfig().getDouble("BlueEdge1X");
		double blueEdge1Z = this.kingAttack.getConfig().getDouble("BlueEdge1Z");

		double blueEdge2X = this.kingAttack.getConfig().getDouble("BlueEdge2X");
		double blueEdge2Z = this.kingAttack.getConfig().getDouble("BlueEdge2Z");

		double redEdge1X = this.kingAttack.getConfig().getDouble("RedEdge1X");
		double redEdge1Z = this.kingAttack.getConfig().getDouble("RedEdge1Z");

		double redEdge2X = this.kingAttack.getConfig().getDouble("RedEdge2X");
		double redEdge2Z = this.kingAttack.getConfig().getDouble("RedEdge2Z");

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

		//Create Locations for each Spawn Point
		//We will use the default world
		Location blueTeamSpawn = new Location(Bukkit.getWorlds().get(0), blueSpawnX, blueSpawnY, blueSpawnZ);
		Location redTeamSpawn = new Location(Bukkit.getWorlds().get(0), redSpawnX, redSpawnY, redSpawnZ);
		Location spectatorSpawn = new Location(Bukkit.getWorlds().get(0), spectatorSpawnX, spectatorSpawnY, spectatorSpawnZ);

		//Now create a map object
		Map map = new Map(blueTeamSpawn, redTeamSpawn, blueEdge1X, blueEdge1Z, blueEdge2X, blueEdge2Z, redEdge1X, redEdge1Z
				, redEdge2X, redEdge2Z, spectatorSpawn);

		//Finally, lets return the map
		return map;


    }

}

