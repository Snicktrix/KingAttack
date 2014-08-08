package net.Snicktrix.KingAttack;

import org.bukkit.Location;

/**
 * Created by Luke on 8/7/14.
 */
public class Map {
	private String worldName;

	private Location blueTeamSpawn;
	private Location redTeamSpawn;

	private double blueEdge1X;
	private double blueEdge1Z;

	private double blueEdge2X;
	private double blueEdge2Z;

	private double redEdge1X;
	private double redEdge1Z;

	private double redEdge2X;
	private double redEdge2Z;

	private Location spectatorSpawn;

	public Map(String worldName, Location blueTeamSpawn, Location redTeamSpawn, double blueEdge1X, double blueEdge1Z, double blueEdge2X, double blueEdge2Z, double redEdge1X, double redEdge1Z, double redEdge2X, double redEdge2Z, Location spectatorSpawn) {
		this.worldName = worldName;
		this.blueTeamSpawn = blueTeamSpawn;
		this.redTeamSpawn = redTeamSpawn;
		this.blueEdge1X = blueEdge1X;
		this.blueEdge1Z = blueEdge1Z;
		this.blueEdge2X = blueEdge2X;
		this.blueEdge2Z = blueEdge2Z;
		this.redEdge1X = redEdge1X;
		this.redEdge1Z = redEdge1Z;
		this.redEdge2X = redEdge2X;
		this.redEdge2Z = redEdge2Z;
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

	public double getBlueEdge1X() {
		return blueEdge1X;
	}

	public double getBlueEdge1Z() {
		return blueEdge1Z;
	}

	public double getBlueEdge2X() {
		return blueEdge2X;
	}

	public double getBlueEdge2Z() {
		return blueEdge2Z;
	}

	public double getRedEdge1X() {
		return redEdge1X;
	}

	public double getRedEdge1Z() {
		return redEdge1Z;
	}

	public double getRedEdge2X() {
		return redEdge2X;
	}

	public double getRedEdge2Z() {
		return redEdge2Z;
	}
}

