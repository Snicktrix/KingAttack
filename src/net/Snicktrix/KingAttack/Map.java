package net.Snicktrix.KingAttack;

import org.bukkit.Location;

/**
 * Created by Luke on 8/7/14.
 */
public class Map {
	private String worldName;

	private Location blueTeamSpawn;
	private Location redTeamSpawn;

	private double buildEdge1X;
	private double buildEdge1Z;

	private double buildEdge2X;
	private double buildEdge2Z;

	private Location spectatorSpawn;

	public double getBuildEdge1X() {
		return buildEdge1X;
	}

	public double getBuildEdge1Z() {
		return buildEdge1Z;
	}

	public double getBuildEdge2X() {
		return buildEdge2X;
	}

	public double getBuildEdge2Z() {
		return buildEdge2Z;
	}

	public Map(String worldName, Location blueTeamSpawn, Location redTeamSpawn, double buildEdge1X, double buildEdge1Z, double buildEdge2X, double buildEdge2Z, Location spectatorSpawn) {
		this.worldName = worldName;
		this.blueTeamSpawn = blueTeamSpawn;
		this.redTeamSpawn = redTeamSpawn;
		this.buildEdge1X = buildEdge1X;
		this.buildEdge1Z = buildEdge1Z;
		this.buildEdge2X = buildEdge2X;
		this.buildEdge2Z = buildEdge2Z;

		this.spectatorSpawn = spectatorSpawn;
	}

	public Location getSpectatorSpawn() {
		return spectatorSpawn;
	}

	public void setSpectatorSpawn(Location spectatorSpawn) {
		this.spectatorSpawn = spectatorSpawn;
	}

	public String getWorldName() {
		return worldName;
	}


	public Location getBlueTeamSpawn() {
		return blueTeamSpawn;
	}

	public Location getRedTeamSpawn() {
		return redTeamSpawn;
	}

}

