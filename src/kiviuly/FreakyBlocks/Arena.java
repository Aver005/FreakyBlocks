package kiviuly.FreakyBlocks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Arena
{
	public Arena(String ID)
	{
		this.ID = ID;
		this.name = ID;
	}
	
	// Переменные карты
	
	public Main main = Main.main;
	
	private String ID;
	private String name;
	
	private int maxPlayers = 2;
	private int startMoney = 5;
	private int tavernSize = 4;
	
	private Location lobbyLocation = null;
	
	private ArrayList<FreakBlock> aviabledBlocks = new ArrayList<>();
	private HashMap<Integer, ArrayList<FreakBlock>> tavernBlocks = new HashMap<>();
	
	// Переменные матча
	
	private FreakBlock attackingBlock = null;	
	private FreakBlock defendingBlock = null;	
	private int attackingTeamID = 0;
	private int defendingTeamID = 0;
	
	private int playersNow = 0;
	private int rounds = 0;
	private int step = 0;
	private int circles = 0;
	
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Player> lobby = new ArrayList<>();
	
	private ArrayList<Integer> playersMoney = new ArrayList<>();
	private ArrayList<Location> playersSpawns = new ArrayList<>();
	
	private HashMap<Integer, ArrayList<Location>> deckLocations = new HashMap<>();
	private HashMap<Integer, ArrayList<Location>> tavernLocations = new HashMap<>();
	private HashMap<Integer, ArrayList<Location>> craftLocations = new HashMap<>();
	private HashMap<Integer, ArrayList<FreakBlock>> playersDecks = new HashMap<>();
	
	private FreakStage matchStage = FreakStage.Lobby;
	
	
	
	public boolean addPlayer(Player p)
	{
		if (playersNow == maxPlayers) {return false;}
		
		lobby.add(p);
		playersNow++;
		
		p.getInventory().clear();
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.teleport(getLobbyLocation());
		
		ItemStack is = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§cВыйти");
		is.setItemMeta(im);
		p.getInventory().addItem(is);
		
		if (playersNow == maxPlayers) {Start(10);}
		return true;
	}
	
	public void Start(int time)
	{
		new BukkitRunnable() 
		{
			int sec = time;
			
			@Override
			public void run() 
			{
				if (playersNow < maxPlayers) {cancel(); return;}
				
				
				for(Player p : lobby)
				{
					if (p == null) {continue;}
					p.setLevel(sec);
					
					if (sec % 10 == 0 || sec < 10) 
					{
						if (matchStage == FreakStage.Lobby) {p.sendMessage("§e[Оповещение] §bНачало через "+sec+" секунд...");}
						if (matchStage == FreakStage.Prepare) {p.sendMessage("§e[Оповещение] §bРаунд начнётся через "+sec+" секунд...");}
					}
				}
				
				if (sec == 1)
				{
					if (matchStage == FreakStage.Lobby)
					{
						for(Player p : lobby)
						{
							if (p == null) {continue;}
							playersMoney.add(startMoney);
							players.add(p);
							p.sendMessage("§2Ваш баланс => §e§l" + startMoney + " §eтугриков.");
						}
						
						int team = 0;
						ArrayList<FreakBlock> tavern = getBlocksByTavernID(0);
						
						for(int i = 0; i < 4 * players.size(); i++)
						{
							ArrayList<FreakBlock> deck = playersDecks.get(team);
							int rnd = main.randomInt(0, tavern.size());
							FreakBlock fb = tavern.get(rnd);
							deck.add(fb);
							playersDecks.put(team, deck);
							
							if (i % 4 == 0 && i > 0) {team++;}
						}
						
						sec = 60;
						matchStage = FreakStage.Prepare;
						lobby.clear();
					}
					
					if (matchStage == FreakStage.Prepare)
					{
						for(Player p : players)
						{
							p.sendMessage("§2Раунд "+rounds+" начался.");
						}
						
						ArrayList<FreakBlock> deck = playersDecks.get(0);
						for(int i = 1; i < players.size(); i++)
						{
							if (rounds % i == 0) {deck = playersDecks.get(i); attackingTeamID = i;}
						}
						
						attackingBlock = deck.get(0);
						step = 0;
						circles = 0;
						
						matchStage = FreakStage.Round;
						sec = 242;
						return;
					}
				}
				
				if (matchStage == FreakStage.Round)
				{
					if (sec % 3 == 0)
					{
						if (step == 0)
						{
							ArrayList<FreakBlock> otherDeck = playersDecks.getOrDefault(attackingTeamID + 1, null);
							if (circles % 2 != 0) {otherDeck = playersDecks.getOrDefault(players.size() - 1, null);}
							if (otherDeck == null) {otherDeck = playersDecks.get(0);}
							
							for(int i = otherDeck.size() - 1; i > -1; i--)
							{
								FreakBlock fb = otherDeck.get(i);
								if (fb == null) {continue;}
								
								defendingBlock = fb;
								if (fb.getCategory() == FreakCategory.Taunt) {break;}
							}
						}
					}
				}
				
				
				sec--;
			}
			
		}.runTaskTimer(main, 20L, 20L);
		
	}
	
	public ArrayList<FreakBlock> getBlocksByTavernID(int lvl)
	{
		return tavernBlocks.getOrDefault(lvl, new ArrayList<>());
	}



	public String getID()
	{
		return ID;
	}

	public void setID(String iD)
	{
		ID = iD;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		this.maxPlayers = maxPlayers;
	}

	public int getPlayersNow()
	{
		return playersNow;
	}

	public void setPlayersNow(int playersNow)
	{
		this.playersNow = playersNow;
	}

	public int getStartMoney()
	{
		return startMoney;
	}

	public void setStartMoney(int startMoney)
	{
		this.startMoney = startMoney;
	}

	public FreakStage getMatchStage()
	{
		return matchStage;
	}

	public void setMatchStage(FreakStage matchStage)
	{
		this.matchStage = matchStage;
	}

	public Location getLobbyLocation()
	{
		return lobbyLocation;
	}

	public void setLobbyLocation(Location lobbyLocation)
	{
		this.lobbyLocation = lobbyLocation;
	}

	public ArrayList<FreakBlock> getAviabledBlocks()
	{
		return aviabledBlocks;
	}

	public void setAviabledBlocks(ArrayList<FreakBlock> aviabledBlocks)
	{
		this.aviabledBlocks = aviabledBlocks;
	}

	public int getRounds()
	{
		return rounds;
	}

	public void setRounds(int rounds)
	{
		this.rounds = rounds;
	}

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}

	public int getTavernSize()
	{
		return tavernSize;
	}

	public void setTavernSize(int tavernSize)
	{
		this.tavernSize = tavernSize;
	}

	public HashMap<Integer, ArrayList<FreakBlock>> getTavernBlocks()
	{
		return tavernBlocks;
	}

	public void setTavernBlocks(HashMap<Integer, ArrayList<FreakBlock>> tavernBlocks)
	{
		this.tavernBlocks = tavernBlocks;
	}

	public ArrayList<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<Player> players)
	{
		this.players = players;
	}

	public ArrayList<Player> getLobby()
	{
		return lobby;
	}

	public void setLobby(ArrayList<Player> lobby)
	{
		this.lobby = lobby;
	}

	public ArrayList<Integer> getPlayersMoney()
	{
		return playersMoney;
	}

	public void setPlayersMoney(ArrayList<Integer> playersMoney)
	{
		this.playersMoney = playersMoney;
	}

	public ArrayList<Location> getPlayersSpawns()
	{
		return playersSpawns;
	}

	public void setPlayersSpawns(ArrayList<Location> playersSpawns)
	{
		this.playersSpawns = playersSpawns;
	}

	public HashMap<Integer, ArrayList<Location>> getDeckLocations()
	{
		return deckLocations;
	}

	public void setDeckLocations(HashMap<Integer, ArrayList<Location>> deckLocations)
	{
		this.deckLocations = deckLocations;
	}

	public HashMap<Integer, ArrayList<Location>> getTavernLocations()
	{
		return tavernLocations;
	}

	public void setTavernLocations(HashMap<Integer, ArrayList<Location>> tavernLocations)
	{
		this.tavernLocations = tavernLocations;
	}

	public HashMap<Integer, ArrayList<Location>> getCraftLocations()
	{
		return craftLocations;
	}

	public void setCraftLocations(HashMap<Integer, ArrayList<Location>> craftLocations)
	{
		this.craftLocations = craftLocations;
	}

	public HashMap<Integer, ArrayList<FreakBlock>> getPlayersDecks()
	{
		return playersDecks;
	}

	public void setPlayersDecks(HashMap<Integer, ArrayList<FreakBlock>> playersDecks)
	{
		this.playersDecks = playersDecks;
	}

	public FreakBlock getAttackingBlock()
	{
		return attackingBlock;
	}

	public void setAttackingBlock(FreakBlock attackingBlock)
	{
		this.attackingBlock = attackingBlock;
	}

	public FreakBlock getDefendingBlock()
	{
		return defendingBlock;
	}

	public void setDefendingBlock(FreakBlock defendingBlock)
	{
		this.defendingBlock = defendingBlock;
	}

	public int getAttackingTeamID()
	{
		return attackingTeamID;
	}

	public void setAttackingTeamID(int attackingTeamID)
	{
		this.attackingTeamID = attackingTeamID;
	}

	public int getDefendingTeamID()
	{
		return defendingTeamID;
	}

	public void setDefendingTeamID(int defendingTeamID)
	{
		this.defendingTeamID = defendingTeamID;
	}

	public int getCircles()
	{
		return circles;
	}

	public void setCircles(int circles)
	{
		this.circles = circles;
	}
}
