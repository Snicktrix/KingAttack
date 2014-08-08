package net.Snicktrix.KingAttack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Luke on 8/7/14.
 */
public class Events implements Listener {
    private KingAttack kingAttack;

    public Events(KingAttack kingAttack) {
        this.kingAttack = kingAttack;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        kingAttack.gameManager.JoinGame(event.getPlayer());
    }

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		kingAttack.gameManager.leaveGame(event.getPlayer());
	}

}
