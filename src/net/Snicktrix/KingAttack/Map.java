package net.Snicktrix.KingAttack;

import org.bukkit.Location;

/**
 * Created by Luke on 8/7/14.
 */
public class Map {
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

	public Map(Location blueTeamSpawn, Location redTeamSpawn, double blueEdge1X, double blueEdge1Z, double blueEdge2X, double blueEdge2Z, double redEdge1X, double redEdge1Z, double redEdge2X, double redEdge2Z, Location spectatorSpawn) {
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

	public Location getBlueTeamSpawn() {
		return blueTeamSpawn;
	}

	public void setBlueTeamSpawn(Location blueTeamSpawn) {
		this.blueTeamSpawn = blueTeamSpawn;
	}

	public Location getRedTeamSpawn() {
		return redTeamSpawn;
	}

	public void setRedTeamSpawn(Location redTeamSpawn) {
		this.redTeamSpawn = redTeamSpawn;
	}

}

