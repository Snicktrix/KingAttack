package net.Snicktrix.KingAttack;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
		if (this.kingAttack.gameManager.isDenyJoin()) {
			event.getPlayer().kickPlayer("Restarting Game");
			return;
		}
		kingAttack.gameManager.JoinGame(event.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		kingAttack.gameManager.leaveGame(event.getPlayer());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			kingAttack.gameManager.playerDeath(event.getEntity(), event.getEntity().getKiller());
		} else {
			kingAttack.gameManager.playerDeath(event.getEntity());
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(kingAttack.gameManager.getRespawnLocation(event.getPlayer()));
	}

	//Friendly Fire
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent event) {
		//No hitting before game is started
		if (!kingAttack.gameManager.isGameStarted()) {
			event.setCancelled(true);
			return;
		}
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player hurt = (Player) event.getEntity();
			Player attacker = (Player) event.getDamager();

			if (this.kingAttack.gameManager.onSameTeam(hurt, attacker)) {
				event.setCancelled(true);
			}
			return;
		}
		//Check to see if a player shot an arrow at another player
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
			Player hurt = (Player) event.getEntity();

			Arrow arrow = (Arrow) event.getDamager();

			if (arrow.getShooter() instanceof Player) {
				Player attacker = (Player) arrow.getShooter();

				if (this.kingAttack.gameManager.onSameTeam(hurt, attacker)) {
					event.setCancelled(true);
				}
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		//Game has not started.
		if (!kingAttack.gameManager.isGameStarted()) {
			event.setCancelled(true);
			return;
		}

		if (kingAttack.gameManager.isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}

		if (!this.kingAttack.gameManager.insideBuildZone(event.getBlock().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can only break blocks in the middle zone");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//Game has not started.
		if (!kingAttack.gameManager.isGameStarted()) {
			event.setCancelled(true);
			return;
		}

		if (kingAttack.gameManager.isSpectator(event.getPlayer())) {
			event.setCancelled(true);
		}

		if (!this.kingAttack.gameManager.insideBuildZone(event.getBlock().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can only place blocks in the middle zone");
		}
	}

}
