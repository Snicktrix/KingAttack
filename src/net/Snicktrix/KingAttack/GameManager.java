package net.Snicktrix.KingAttack;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by Luke on 8/6/14.
 */
public class GameManager {
    private boolean gameStarted;
    private ArrayList<GamePlayer> blueTeam = new ArrayList<GamePlayer>();
    private ArrayList<GamePlayer> redTeam = new ArrayList<GamePlayer>();
    private ArrayList<GamePlayer> gamePlayerList = new ArrayList<GamePlayer>();

    private Map map;

    private int minimumGameSize;
    private int maxGameSize;

    public GameManager(Map map) {
        this.map = map;
    }

    public void JoinGame(Player player) {
        //Create a GamePlayer and keep track of it
        GamePlayer gamePlayer = new GamePlayer(player, GamePlayer.Type.Spectator, findTeam());
        this.gamePlayerList.add(gamePlayer);

		//Add them to the team lists
		if (gamePlayer.getTeam() == GamePlayer.Team.Blue) {
			blueTeam.add(gamePlayer);
		} else if (gamePlayer.getTeam() == GamePlayer.Team.Red) {
			redTeam.add(gamePlayer);
		}

		//Now check if we should start the game or not
        if (gameStarted) {
            joinStartedGame(gamePlayer);
        } else {
            //The game has not yet started
            //Returns true if the game is going to start
            if (!checkReadyToStart()) {
                //The game is not going to start
                //Setup the player as a spectator
                setupSpectator(gamePlayer);
            }
			//StartGame() will auto assign this player
        }
    }

    public boolean onSameTeam(Player player1, Player player2) {
        GamePlayer gamePlayer1 = getGamePlayerFromPlayer(player1);
        GamePlayer gamePlayer2 = getGamePlayerFromPlayer(player2);

        //I'm loving these enums ^.^
        if(gamePlayer1.getTeam() == gamePlayer2.getTeam()) return true;

        //If not
        return false;
    }

    private GamePlayer getGamePlayerFromPlayer(Player player) {
        for (GamePlayer gamePlayer : this.gamePlayerList) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    private GamePlayer.Team findTeam() {
        if (this.blueTeam.size() <= this.redTeam.size()) {
            //Blue Team
            return GamePlayer.Team.Blue;
        } else {
            //Red Team
            return GamePlayer.Team.Red;
        }
    }

    private int getGameSize() {
        return this.blueTeam.size() + this.redTeam.size();
    }

    private boolean checkReadyToStart() {
        if (getGameSize() >= this.minimumGameSize) {
            this.startGame();
            return true;
        }
        return false;
    }

    //Use to start the actual game
    private void startGame() {
        gameStarted = true;

        //Make first player a King
        blueTeam.get(0).setType(GamePlayer.Type.King);
        redTeam.get(0).setType(GamePlayer.Type.King);

        for (GamePlayer gamePlayer : blueTeam) {
            if (gamePlayer.getType() == GamePlayer.Type.Knight) setupBlueKnight(gamePlayer);
            else if (gamePlayer.getType() == GamePlayer.Type.King) setupBlueKing(gamePlayer);
        }
        for (GamePlayer gamePlayer : redTeam) {
            if (gamePlayer.getType() == GamePlayer.Type.Knight) setupRedKnight(gamePlayer);
            else if (gamePlayer.getType() == GamePlayer.Type.King) setupRedKing(gamePlayer);
        }

    }

    //The game has already started
    //Auto assign the player to be a Knight
    //Game would be over if a knight wasn't there
    private void joinStartedGame(GamePlayer gamePlayer) {
        if (gamePlayer.getTeam() == GamePlayer.Team.Blue) {
            gamePlayer.setType(GamePlayer.Type.Knight);
            setupBlueKnight(gamePlayer);
        } else if (gamePlayer.getTeam() == GamePlayer.Team.Red) {
            gamePlayer.setType(GamePlayer.Type.Knight);
            setupRedKnight(gamePlayer);
        }
    }

    //*************************************************//

    //******* BLUE PLAYERS *******//

    //Blue Knight
    private void setupBlueKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.LAPIS_ORE));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue Knight");
    }

    //Blue King
    private void setupBlueKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue King");
    }

    //******* RED PLAYERS *******//

    //Red Knight
    private void setupRedKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE));
		gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red Knight");
		gamePlayer.getPlayer();
    }

    //Red King
    private void setupRedKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK));
        gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red King");
    }

    //******* SPECTATOR *******//

    private void setupSpectator(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().teleport(map.getSpectatorSpawn());
        gamePlayer.getPlayer().sendMessage("You are a spectator");
    }

    //*************************************************//

    //Just a little shortcut
    void debug(String msg) {
        System.out.println(msg);
    }


}
