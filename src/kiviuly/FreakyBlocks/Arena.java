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
		
		shopBlocksCountByTier.put(1, 4);
		shopBlocksCountByTier.put(2, 5);
		shopBlocksCountByTier.put(3, 6);
		shopBlocksCountByTier.put(4, 7);
		
		ArrayList<FreakBlock> firstTier = new ArrayList<>();
		ArrayList<FreakBlock> secondTier = new ArrayList<>();
		ArrayList<FreakBlock> thirdTier = new ArrayList<>();
		ArrayList<FreakBlock> fourTier = new ArrayList<>();
		
		FreakBlock b = new FreakBlock();
		b.setCategory(FreakCategory.Wood);
		b.setDamage(1);
		b.setHealth(1);
		b.setName("Деревянные доски");
		b.setMaterial(Material.WOOD);
		b.setPrice(1);
		b.setID(0);
		firstTier.add(b);
		
		b = new FreakBlock();
		b.setDamage(1);
		b.setHealth(1);
		b.setName("Дуб");
		b.setPrice(2);
		b.setID(1);
		firstTier.add(b);
		
		b = new FreakBlock();
		b.setCategory(FreakCategory.Stone);
		b.setMaterial(Material.COBBLESTONE);
		b.setHealth(1);
		b.setDamage(2);
		b.setName("Булыжник");
		b.setPrice(1);
		b.setID(2);
		firstTier.add(b);

		b = new FreakBlock();
		b.setCategory(FreakCategory.Weak);
		b.setMaterial(Material.GLASS);
		b.setHealth(2);
		b.setDamage(1);
		b.setName("Стекло");
		b.setPrice(1);
		b.setID(3);
		firstTier.add(b);
		
		secondTier = (ArrayList<FreakBlock>) firstTier.clone();

		b = new FreakBlock();
		b.setCategory(FreakCategory.Metal);
		b.setMaterial(Material.IRON_BLOCK);
		b.setHealth(2);
		b.setDamage(3);
		b.setName("Железный блок");
		b.setPrice(2);
		b.setID(4);
		secondTier.add(b);
		
		thirdTier = (ArrayList<FreakBlock>) secondTier.clone();
		
		b = new FreakBlock();
		b.setCategory(FreakCategory.Wood);
		b.setMaterial(Material.CHEST);
		b.setHealth(4);
		b.setDamage(3);
		b.setName("Сундук");
		b.setPrice(3);
		b.setID(5);
		thirdTier.add(b);
		
		fourTier = (ArrayList<FreakBlock>) thirdTier.clone();
		
		b = new FreakBlock();
		b.setCategory(FreakCategory.Stone);
		b.setMaterial(Material.FURNACE);
		b.setHealth(2);
		b.setDamage(5);
		b.setName("Я печка");
		b.setPrice(3);
		b.setID(6);
		fourTier.add(b);

//		b = new FreakBlock();
//		b.setCategory(FreakCategory.Wood);
//		b.setMaterial(Material.FENCE);
//		b.setHealth(1);
//		b.setDamage(2);
//		b.setName("Дер. забро");
//		b.setPrice(2);
//		b.setID(7);
//		secondTier.add(b);
//
//		b = new FreakBlock();
//		b.setCategory(FreakCategory.Wood);
//		b.setMaterial(Material.FENCE);
//		b.setHealth(1);
//		b.setDamage(2);
//		b.setName("Дер. забро");
//		b.setPrice(2);
//		b.setID(7);
//		secondTier.add(b);
		
		allShopBlocksByTier.put(1, firstTier);
		allShopBlocksByTier.put(2, secondTier);
		allShopBlocksByTier.put(3, thirdTier);
		allShopBlocksByTier.put(4, fourTier);
	}
	
	public Main main = Main.main;
	
	private String ID;
	private String name;
	
	private int maxPlayers = 2;
	private int playersNow = 0;
	
	private int rounds = 0;
	private int step = 0;
	
	private int startMoney = 5;
	
	private int firstPlayerMoney = startMoney;
	private int secondPlayerMoney = startMoney;
	
	private Player firstPlayer = null;
	private Player secondPlayer = null;
	
	private ArrayList<Player> playersList = new ArrayList<>();
	private ArrayList<Player> lobbyList = new ArrayList<>();
	
	private FreakStage matchStage = FreakStage.Lobby;
	private Location lobbyLocation = null;
	
	private Location player1Spawn = null;
	private Location player2Spawn = null;
	
	private ArrayList<Location> deck1Locations = new ArrayList<>();
	private ArrayList<Location> deck2Locations = new ArrayList<>();
	
	private ArrayList<Location> craft1Locations = new ArrayList<>();
	private ArrayList<Location> craft2Locations = new ArrayList<>();
	
	private ArrayList<Location> shop1Locations = new ArrayList<>();
	private ArrayList<Location> shop2Locations = new ArrayList<>();
	
	private ArrayList<FreakBlock> aviabledBlocks = new ArrayList<>();
	
	private int firstShopTier = 1;
	private int secondShopTier = 1;
	
	private ArrayList<FreakBlock> firstShop = new ArrayList<>();
	private ArrayList<FreakBlock> secondShop = new ArrayList<>();
	
	private HashMap<Integer, Integer> shopBlocksCountByTier = new HashMap<>();
	private HashMap<Integer, ArrayList<FreakBlock>> shopBlocksByTier = new HashMap<>();
	private HashMap<Integer, ArrayList<FreakBlock>> allShopBlocksByTier = new HashMap<>();
	
	private ArrayList<FreakBlock> deckPlayer1 = new ArrayList<>();
	private ArrayList<FreakBlock> deckPlayer2 = new ArrayList<>();
	
	
	
	public boolean addPlayer(Player p)
	{
		if (playersNow == maxPlayers) {return false;}
		
		getLobbyList().add(p);
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
				
				for(Player p : lobbyList)
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
						firstPlayer = lobbyList.get(0);
						secondPlayer = lobbyList.get(1);
						
						firstPlayer.sendMessage("§2Ваш баланс => §e§l" + firstPlayerMoney + " §eтугриков.");
						secondPlayer.sendMessage("§2Ваш баланс => §e§l" + secondPlayerMoney + " §eтугриков.");
						
						for(int i = 0; i < 4; i++)
						{
							int rnd = main.randomInt(0, 4);
							FreakBlock fb = allShopBlocksByTier.get(1).get(rnd);
							firstShop.add(fb);
							shop1Locations.get(i).getBlock().setType(fb.getMaterial());
							
							rnd = main.randomInt(0, 4);
							fb = allShopBlocksByTier.get(1).get(rnd);
							secondShop.add(fb);
							shop2Locations.get(i).getBlock().setType(fb.getMaterial());
						}
						
						sec = 60;
						matchStage = FreakStage.Prepare;
						lobbyList.clear();
					}
					
					if (matchStage == FreakStage.Prepare)
					{
						firstPlayer.sendMessage("§2Раунд "+rounds+" начался.");
						secondPlayer.sendMessage("§2Раунд "+rounds+" начался.");
						
						matchStage = FreakStage.Round;
						sec = 240;
					}
				}
				
				
				if (sec == 240)
				{
					if (matchStage == FreakStage.Round)
					{
						FreakBlock block = rounds % 2 == 0 ? deckPlayer1 : deckPlayer2;
					}
				}
				
				
				sec--;
			}
			
		}.runTaskTimer(main, 20L, 20L);
		
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

	public ArrayList<Player> getPlayersList()
	{
		return playersList;
	}

	public void setPlayersList(ArrayList<Player> playersList)
	{
		this.playersList = playersList;
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

	public Location getPlayer1Spawn()
	{
		return player1Spawn;
	}

	public void setPlayer1Spawn(Location player1Spawn)
	{
		this.player1Spawn = player1Spawn;
	}

	public Location getPlayer2Spawn()
	{
		return player2Spawn;
	}

	public void setPlayer2Spawn(Location player2Spawn)
	{
		this.player2Spawn = player2Spawn;
	}

	public ArrayList<Location> getDeck1Locations()
	{
		return deck1Locations;
	}

	public void setDeck1Locations(ArrayList<Location> deck1Locations)
	{
		this.deck1Locations = deck1Locations;
	}

	public ArrayList<Location> getDeck2Locations()
	{
		return deck2Locations;
	}

	public void setDeck2Locations(ArrayList<Location> deck2Locations)
	{
		this.deck2Locations = deck2Locations;
	}

	public ArrayList<Location> getCraft1Locations()
	{
		return craft1Locations;
	}

	public void setCraft1Locations(ArrayList<Location> craft1Locations)
	{
		this.craft1Locations = craft1Locations;
	}

	public ArrayList<Location> getCraft2Locations()
	{
		return craft2Locations;
	}

	public void setCraft2Locations(ArrayList<Location> craft2Locations)
	{
		this.craft2Locations = craft2Locations;
	}

	public ArrayList<Location> getShop1Locations()
	{
		return shop1Locations;
	}

	public void setShop1Locations(ArrayList<Location> shop1Locations)
	{
		this.shop1Locations = shop1Locations;
	}

	public ArrayList<Location> getShop2Locations()
	{
		return shop2Locations;
	}

	public void setShop2Locations(ArrayList<Location> shop2Locations)
	{
		this.shop2Locations = shop2Locations;
	}

	public ArrayList<FreakBlock> getAviabledBlocks()
	{
		return aviabledBlocks;
	}

	public void setAviabledBlocks(ArrayList<FreakBlock> aviabledBlocks)
	{
		this.aviabledBlocks = aviabledBlocks;
	}

	public ArrayList<Player> getLobbyList()
	{
		return lobbyList;
	}

	public void setLobbyList(ArrayList<Player> lobbyList)
	{
		this.lobbyList = lobbyList;
	}

	public int getFirstPlayerMoney()
	{
		return firstPlayerMoney;
	}

	public void setFirstPlayerMoney(int firstPlayerMoney)
	{
		this.firstPlayerMoney = firstPlayerMoney;
	}

	public int getSecondPlayerMoney()
	{
		return secondPlayerMoney;
	}

	public void setSecondPlayerMoney(int secondPlayerMoney)
	{
		this.secondPlayerMoney = secondPlayerMoney;
	}

	public Player getFirstPlayer()
	{
		return firstPlayer;
	}

	public void setFirstPlayer(Player firstPlayer)
	{
		this.firstPlayer = firstPlayer;
	}

	public Player getSecondPlayer()
	{
		return secondPlayer;
	}

	public void setSecondPlayer(Player secondPlayer)
	{
		this.secondPlayer = secondPlayer;
	}

	public HashMap<Integer, Integer> getShopBlocksCountByTier()
	{
		return shopBlocksCountByTier;
	}

	public void setShopBlocksCountByTier(HashMap<Integer, Integer> shopBlocksCountByTier)
	{
		this.shopBlocksCountByTier = shopBlocksCountByTier;
	}

	public HashMap<Integer, ArrayList<FreakBlock>> getShopBlocksByTier()
	{
		return shopBlocksByTier;
	}

	public void setShopBlocksByTier(HashMap<Integer, ArrayList<FreakBlock>> shopBlocksByTier)
	{
		this.shopBlocksByTier = shopBlocksByTier;
	}

	public HashMap<Integer, ArrayList<FreakBlock>> getAllShopBlocksByTier()
	{
		return allShopBlocksByTier;
	}

	public void setAllShopBlocksByTier(HashMap<Integer, ArrayList<FreakBlock>> allShopBlocksByTier)
	{
		this.allShopBlocksByTier = allShopBlocksByTier;
	}

	public int getFirstShopTier()
	{
		return firstShopTier;
	}

	public void setFirstShopTier(int firstShopTier)
	{
		this.firstShopTier = firstShopTier;
	}

	public int getSecondShopTier()
	{
		return secondShopTier;
	}

	public void setSecondShopTier(int secondShopTier)
	{
		this.secondShopTier = secondShopTier;
	}

	public ArrayList<FreakBlock> getSecondShop()
	{
		return secondShop;
	}

	public void setSecondShop(ArrayList<FreakBlock> secondShop)
	{
		this.secondShop = secondShop;
	}

	public ArrayList<FreakBlock> getFirstShop()
	{
		return firstShop;
	}

	public void setFirstShop(ArrayList<FreakBlock> firstShop)
	{
		this.firstShop = firstShop;
	}

	public int getRounds()
	{
		return rounds;
	}

	public void setRounds(int rounds)
	{
		this.rounds = rounds;
	}

	public ArrayList<FreakBlock> getDeckPlayer1()
	{
		return deckPlayer1;
	}

	public void setDeckPlayer1(ArrayList<FreakBlock> deckPlayer1)
	{
		this.deckPlayer1 = deckPlayer1;
	}

	public ArrayList<FreakBlock> getDeckPlayer2()
	{
		return deckPlayer2;
	}

	public void setDeckPlayer2(ArrayList<FreakBlock> deckPlayer2)
	{
		this.deckPlayer2 = deckPlayer2;
	}

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}
}
