package net.Snicktrix.KingAttack;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Luke on 8/6/14.
 */
public class GameManager {
	private KingAttack kingAttack;

	private boolean denyJoin = false;

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
		this.minimumGameSize = map.getMinPlayers();
		this.maxGameSize = maxGameSize;

		this.checkMovement();
    }

    public void JoinGame(Player player) {
        //Create a GamePlayer and keep track of it
        GamePlayer gamePlayer = new GamePlayer(player, GamePlayer.Type.Spectator, findTeam());
        this.gamePlayerList.add(gamePlayer);

		//Add them to the team lists
		if (gamePlayer.getTeam() == GamePlayer.Team.Blue) {
			blueTeam.add(gamePlayer);
			player.setPlayerListName(ChatColor.BLUE + player.getName());
		} else if (gamePlayer.getTeam() == GamePlayer.Team.Red) {
			redTeam.add(gamePlayer);
			player.setPlayerListName(ChatColor.RED + player.getName());
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

	//Use to start the actual game
	private void startGame() {
		debug(ChatColor.RED + "STARTING GAME");
		gameStarted = true;

		//Make first player a King
		blueTeam.get(0).setType(GamePlayer.Type.King);
		redTeam.get(0).setType(GamePlayer.Type.King);

		for (GamePlayer gamePlayer : blueTeam) {
			if (gamePlayer.getType() == GamePlayer.Type.King) {
				setupBlueKing(gamePlayer);
			} else {
				setupBlueKnight(gamePlayer);
			}
		}
		for (GamePlayer gamePlayer : redTeam) {
			if (gamePlayer.getType() == GamePlayer.Type.King) {
				setupRedKing(gamePlayer);
			} else {
				setupRedKnight(gamePlayer);
			}
		}

	}

	public GamePlayer.Team getTeam(Player player) {
		return getGamePlayerFromPlayer(player).getTeam();
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

	public boolean isGameStarted() {
		return gameStarted;
	}

	public boolean isSpectator(Player player) {
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);

		if (gamePlayer.getType() == GamePlayer.Type.Spectator) {
			return true;
		}
		return false;
	}

	public boolean isKing(Player player) {
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);

		if (gamePlayer.getType() == GamePlayer.Type.King) {
			return true;
		}
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

	public void leaveGame(Player player) {
		//The player was kicked from the game
		//Because the game is ending
		if(this.denyJoin) return;

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

				//Kick all players into lobby server
				//BungeeLobbyKick plugin
				if (!Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lobbykick")) {
					//In case the above method doesn't work
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.kickPlayer("Restarting Game");
					}
				}
				resetGame();
			}
		}, 10 * 20);

		//Add a second delay to give enough time for lobbykick command to take finish
		Bukkit.getScheduler().scheduleSyncDelayedTask(kingAttack, new Runnable() {
			@Override
			public void run() {
				resetGame();
			}
		}, 13 * 20);

	}

	public boolean isDenyJoin() {
		return denyJoin;
	}

	private void resetGame() {
		this.denyJoin = true;
		//Clear all our lists
		this.blueTeam.clear();
		this.redTeam.clear();
		this.gamePlayerList.clear();
		this.gameStarted = false;

		//Unload world
		Bukkit.unloadWorld(map.getWorldName(), false);

		//Load world
		Bukkit.getServer().createWorld(new WorldCreator(map.getWorldName()));

		//Make sure world does not auto save
		Bukkit.getWorld(map.getWorldName()).setAutoSave(false);

		this.denyJoin = false;
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

	public void respawnGear(Player player) {
		GamePlayer gamePlayer = getGamePlayerFromPlayer(player);
		GamePlayer.Type type = gamePlayer.getType();

		if (type == GamePlayer.Type.Knight && gamePlayer.getTeam() == GamePlayer.Team.Blue) {
			setupBlueKnight(gamePlayer);
		} else if (type == GamePlayer.Type.Knight && gamePlayer.getTeam() == GamePlayer.Team.Red) {
			setupRedKnight(gamePlayer);
		}
	}


    //*************************************************//

    //******* BLUE PLAYERS *******//

    //Blue Knight
    private void setupBlueKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

		clearPotions(gamePlayer.getPlayer());
		gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
		gamePlayer.getPlayer().setFoodLevel(20);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

		setArmour(gamePlayer.getPlayer(), Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue Knight");
    }

    //Blue King
    private void setupBlueKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

		gamePlayer.getPlayer().setDisplayName(ChatColor.GOLD + "KING " + ChatColor.BLUE + gamePlayer.getPlayer().getName());

		clearPotions(gamePlayer.getPlayer());
		gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
		gamePlayer.getPlayer().setFoodLevel(20);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

		setArmour(gamePlayer.getPlayer(), Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS);
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        gamePlayer.getPlayer().teleport(map.getBlueTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a blue King");
    }

    //******* RED PLAYERS *******//

    //Red Knight
    private void setupRedKnight(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.Knight);

		clearPotions(gamePlayer.getPlayer());
		gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
		gamePlayer.getPlayer().setFoodLevel(20);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

		setArmour(gamePlayer.getPlayer(), Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));
		gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red Knight");
		gamePlayer.getPlayer();
    }

    //Red King
    private void setupRedKing(GamePlayer gamePlayer) {
        gamePlayer.setType(GamePlayer.Type.King);

		gamePlayer.getPlayer().setDisplayName(ChatColor.GOLD + "KING " + ChatColor.RED + gamePlayer.getPlayer().getName());

		clearPotions(gamePlayer.getPlayer());
		gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
		gamePlayer.getPlayer().setFoodLevel(20);

		gamePlayer.getPlayer().getInventory().clear();
		gamePlayer.getPlayer().getInventory().setArmorContents(null);

        setArmour(gamePlayer.getPlayer(), Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS);
		gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        gamePlayer.getPlayer().teleport(map.getRedTeamSpawn());
        gamePlayer.getPlayer().sendMessage("You are a red King");
    }

	public void clearPotions(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

    //******* SPECTATOR *******//

    private void setupSpectator(GamePlayer gamePlayer) {

		clearPotions(gamePlayer.getPlayer());
		gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
		gamePlayer.getPlayer().setFoodLevel(20);

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

	public void checkMovement() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(kingAttack, new Runnable() {
			@Override
			public void run() {
				for (GamePlayer gamePlayer : gamePlayerList) {
					//Make sure game is started to prevent conflict with spectators
					if (gameStarted && gamePlayer.getType() == GamePlayer.Type.King) {
						//Game just started or he just joined, he cannot be out of bounds
						if (gamePlayer.getPrevLoc() == null) {
							if (!insideBuildZone(gamePlayer.getPlayer().getLocation())) {
								gamePlayer.setPrevLoc(gamePlayer.getPlayer().getLocation());
								return;
							} else {
								gamePlayer.getPlayer().kickPlayer("Illegal movement!");
							}
						}
						//PrevLoc is not null
						if (insideBuildZone(gamePlayer.getPlayer().getLocation())) {
							//Player is out of bounds
							//Move them to previous location
							gamePlayer.getPlayer().teleport(gamePlayer.getPrevLoc());
							gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Kings must stay near their castle");
						} else {
							//Player is good
							//Update their new location
							gamePlayer.setPrevLoc(gamePlayer.getPlayer().getLocation());
						}
					}
				}
			}
		}, 0, 10);
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

	//Set armour shortcut
	private void setArmour(Player player, Material helmet, Material chestplate, Material leggings, Material boots) {
		ItemStack helmetItem = new ItemStack(helmet);
		ItemStack chestplateItem = new ItemStack(chestplate);
		ItemStack leggingsItem = new ItemStack(leggings);
		ItemStack bootsItem = new ItemStack(boots);

		player.getInventory().setHelmet(helmetItem);
		player.getInventory().setChestplate(chestplateItem);
		player.getInventory().setLeggings(leggingsItem);
		player.getInventory().setBoots(bootsItem);
	}




}
