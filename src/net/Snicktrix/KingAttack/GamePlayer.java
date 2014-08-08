package net.Snicktrix.KingAttack;

import org.bukkit.entity.Player;

/**
 * Created by Luke on 8/6/14.
 */
public class GamePlayer {
    public enum Team {
        Blue,
        Red
    }

    public enum Type {
        King,
        Knight,
        Spectator
    }

    private Player player;
    private Type type;
    private Team team;

    public GamePlayer(Player player, Type type, Team team) {
        this.player = player;
        this.type = type;
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Type getType() {

        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
