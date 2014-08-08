package net.Snicktrix.KingAttack;

import org.bukkit.Location;

/**
 * Created by Luke on 8/7/14.
 */
public class Map {
    private Location blueTeamSpawn;
    private Location redTeamSpawn;

    private Location blueEdge1;
    private Location blueEdge2;

    private Location redEdge1;
    private Location redEdge2;

    private Location spectatorSpawn;

    public Map(Location blueTeamSpawn,
               Location redTeamSpawn, Location blueEdge1,
               Location blueEdge2, Location redEdge1, Location redEdge2,
               Location spectatorSpawn) {
        this.blueTeamSpawn = blueTeamSpawn;
        this.redTeamSpawn = redTeamSpawn;
        this.blueEdge1 = blueEdge1;
        this.blueEdge2 = blueEdge2;
        this.redEdge1 = redEdge1;
        this.redEdge2 = redEdge2;
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

    public Location getBlueEdge1() {
        return blueEdge1;
    }

    public void setBlueEdge1(Location blueEdge1) {
        this.blueEdge1 = blueEdge1;
    }

    public Location getBlueEdge2() {
        return blueEdge2;
    }

    public void setBlueEdge2(Location blueEdge2) {
        this.blueEdge2 = blueEdge2;
    }

    public Location getRedEdge1() {
        return redEdge1;
    }

    public void setRedEdge1(Location redEdge1) {
        this.redEdge1 = redEdge1;
    }

    public Location getRedEdge2() {
        return redEdge2;
    }

    public void setRedEdge2(Location redEdge2) {
        this.redEdge2 = redEdge2;
    }
}
