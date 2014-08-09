package net.Snicktrix.KingAttack;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Luke on 8/6/14.
 */
public class GameManager {
	private KingAttack kingAttack;

    private boolean gameStarted;
    private ArrayList<GamePlayer> blueTeam = new ArrayList<GamePlayer>();
    private ArrayList<GamePlayer> redTeam = new ArrayList<GamePlayer>();
    private ArrayList<GamePlayer> gamePlayerList = new ArrayList<GamePlayer>();

    private Map map;

    private int minimumGameSize;
    private int maxGameSize;

    public GameManager(KingAttack kingAttack, Map map, int minimumGameSize, int maxGameSize) {
		this.kingAttack = kingAttack;
        this.map = map;
		this.minimumGameSize = minimumGameSize;
		this.maxGameSize = maxGameSize;
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

	public Location getRespawnLocation(Player player) {
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);

		//If player is on the blue team
		if (gamePlayer.getTeam() == GamePlayer.Team.Blue) {
			return map.getBlueTeamSpawn();
		} else {
			//If player is on the red team
			return map.getRedTeamSpawn();
		}
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
		debug(ChatColor.RED + "STARTING GAME");
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

	public void leaveGame(Player player) {
		//Get the gamePlayer
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);
		//Remove from our tracking list
		gamePlayerList.remove(gamePlayer);

		//Remove player from team
		if (blueTeam.contains(gamePlayer)) {
			blueTeam.remove(gamePlayer);
		} else if (redTeam.contains(gamePlayer)) {
			redTeam.remove(gamePlayer);
		}

		if (gamePlayer.getType() == GamePlayer.Type.King) {
			//Tell the players what happen
			Bukkit.broadcastMessage(ChatColor.AQUA + gamePlayer.getPlayer().getName()
					+ ChatColor.DARK_GRAY + " left while being the king. The game will now end");

			//Make this player's team lose
			endGameWithLosingTeam(gamePlayer.getTeam());
		}

	}

	private void endGameWithLosingTeam(GamePlayer.Team losingTeam) {
		GamePlayer.Team winningTeam;

		if (losingTeam == GamePlayer.Team.Blue) {
			winningTeam = GamePlayer.Team.Red;
		} else {
			winningTeam = GamePlayer.Team.Blue;
		}

		//Alert the players if they won or lost
		for (GamePlayer gamePlayer : this.gamePlayerList) {
			if (gamePlayer.getTeam() == winningTeam) {
				gamePlayer.getPlayer().sendMessage(ChatColor.GREEN + "Your team won the game!");
			} else {
				gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Your team lost the game!");
			}
		}

		//Fireworks for everyone!
		for (GamePlayer gamePlayer : gamePlayerList) {
			randomFirework(gamePlayer.getPlayer().getLocation());
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(kingAttack, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.kickPlayer("Restarting Game");
					resetGame();
				}
			}
		}, 3 * 20);

	}

	private void resetGame() {
		//Clear all our lists
		this.blueTeam = new ArrayList<GamePlayer>();
		this.redTeam = new ArrayList<GamePlayer>();
		this.gamePlayerList = new ArrayList<GamePlayer>();
		this.gameStarted = false;

		//Unload world
		Bukkit.unloadWorld(map.getWorldName(), false);

		//Load world
		Bukkit.getServer().createWorld(new WorldCreator(map.getWorldName()));

		//Make sure world does not auto save
		Bukkit.getWorld(map.getWorldName()).setAutoSave(false);
	}

	//*************************************************//

	//******* Killing *******//

	// Player is responsible for kill
	public void playerDeath(Player deadPlayer, Player killerPlayer) {
		GamePlayer deadGamePlayer = getGamePlayerFromPlayer(deadPlayer);

		if (deadGamePlayer.getType() == GamePlayer.Type.King) {
			//Give the killer some credit for the kill
			Bukkit.broadcastMessage(ChatColor.AQUA + killerPlayer.getName()
					+ ChatColor.DARK_GRAY + " killed the King!");

			//Make this players team lose
			endGameWithLosingTeam(deadGamePlayer.getTeam());
		}
	}

	//King is a retard and kills himself
	public void playerDeath(Player deadPlayer) {
		GamePlayer deadGamePlayer = getGamePlayerFromPlayer(deadPlayer);

		if (deadGamePlayer.getType() == GamePlayer.Type.King) {

			//Make this players team lose
			endGameWithLosingTeam(deadGamePlayer.getTeam());
		}
	}




    //*************************************************//

    //******* BLUE PLAYERS *******//

    //Blue Knight
    private void setupBlueKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.LAPIS_ORE));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue Knight");
    }

    //Blue King
    private void setupBlueKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue King");
    }

    //******* RED PLAYERS *******//

    //Red Knight
    private void setupRedKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE));
		gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red Knight");
		gamePlayer.getPlayer();
    }

    //Red King
    private void setupRedKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        gamePlayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK));
        gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red King");
    }

    //******* SPECTATOR *******//

    private void setupSpectator(GamePlayer gamePlayer) {

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        gamePlayer.getPlayer().teleport(map.getSpectatorSpawn());
        gamePlayer.getPlayer().sendMessage("You are a spectator");
    }

    //*************************************************//

	//******* BOUNDARIES  *******//

	public boolean insideBuildZone(Location location) {
		double x = location.getX();
		double z = location.getZ();

		double boundHighX = Math.max(map.getBuildEdge1X(), map.getBuildEdge2X());
		double boundLowX = Math.min(map.getBuildEdge1X(), map.getBuildEdge2X());

		double boundHighZ = Math.max(map.getBuildEdge1Z(), map.getBuildEdge2Z());
		double boundLowZ = Math.min(map.getBuildEdge1Z(), map.getBuildEdge2Z());

		//Check if location is inside the build region
		if (x >= boundLowX && x <= boundHighX
				&& z >= boundLowZ && z <= boundHighZ) {
			return true;
		} else {
			//Not inside the region
			return false;
		}
	}

	public boolean canWalk(Player player, Location toLocation) {
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);

		if (gamePlayer.getType() == GamePlayer.Type.King) {
			if (insideBuildZone(toLocation)) {
				return false;
			}
		}
		return true;
	}




	//*************************************************//

	public String getWorldName() {
		return map.getWorldName();
	}

    //Just a little shortcut
    void debug(String msg) {
        System.out.println(msg);
    }

	//Just for fun
	void randomFirework(Location location) {
		//Spawn the Firework, get the FireworkMeta.
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		//Our random generator
		Random r = new Random();

		//Get the type
		int rt = r.nextInt(4) + 1;
		FireworkEffect.Type type = FireworkEffect.Type.BALL;
		if (rt == 1) type = FireworkEffect.Type.BALL;
		if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
		if (rt == 3) type = FireworkEffect.Type.BURST;
		if (rt == 4) type = FireworkEffect.Type.CREEPER;
		if (rt == 5) type = FireworkEffect.Type.STAR;

		//Get our random colours
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);

		//Create our effect with this
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

		//Then apply the effect to the meta
		fwm.addEffect(effect);

		//Generate some random power and set it
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);

		//Then apply this to our rocket
		fw.setFireworkMeta(fwm);
	}

	//Get random color for fireworks
	private Color getColor(int i) {
		Color c = null;
		if(i==1){
			c=Color.AQUA;
		}
		if(i==2){
			c=Color.BLACK;
		}
		if(i==3){
			c=Color.BLUE;
		}
		if(i==4){
			c=Color.FUCHSIA;
		}
		if(i==5){
			c=Color.GRAY;
		}
		if(i==6){
			c=Color.GREEN;
		}
		if(i==7){
			c=Color.LIME;
		}
		if(i==8){
			c=Color.MAROON;
		}
		if(i==9){
			c=Color.NAVY;
		}
		if(i==10){
			c=Color.OLIVE;
		}
		if(i==11){
			c=Color.ORANGE;
		}
		if(i==12){
			c=Color.PURPLE;
		}
		if(i==13){
			c=Color.RED;
		}
		if(i==14){
			c=Color.SILVER;
		}
		if(i==15){
			c=Color.TEAL;
		}
		if(i==16){
			c=Color.WHITE;
		}
		if(i==17){
			c=Color.YELLOW;
		}

		return c;
	}




}
