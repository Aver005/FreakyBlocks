package kiviuly.FreakyBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	public static Main main = null;
	private HashMap<String, Arena> arenas = new HashMap<>();
	private HashMap<UUID, Arena> players = new HashMap<>();
	
	@Override
	public void onEnable()
	{
		List<String> aliases = new ArrayList<String>();
		aliases.add("fb");
		getCommand("freaksblocks").setExecutor(new Commands(this));
		getCommand("freaksblocks").setAliases(aliases);
		getServer().getPluginManager().registerEvents(new Events(this), this);
		main = this;
	}

	public HashMap<String, Arena> getArenas() {return arenas;}
	public void setArenas(HashMap<String, Arena> arenas) {this.arenas = arenas;}
	public HashMap<UUID, Arena> getPlayers() {return players;}
	public void setPlayers(HashMap<UUID, Arena> players) {this.players = players;}
	

	public int randomInt(int min, int max) 
	{
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
