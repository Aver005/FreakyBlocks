package kiviuly.FreakyBlocks;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Commands implements CommandExecutor
{
	private Main main;
	public Commands(Main m) {main = m;}
	
	public void SM(Player p, String msg) {p.sendMessage(msg);}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
	{
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			int count = args.length;
			
			if (count == 0)
			{
				
				return true;
			}
			
			if (count >= 1)
			{
				String sub = args[0].toLowerCase();
				
				if (count >= 2)
				{
					String name = args[1].toUpperCase();
					
					if (sub.equals("create"))
					{
						if (main.getArenas().containsKey(name)) {SM(p, "�c����� ����� ��� ����������."); return true;}
						Arena arena = new Arena(name);
						main.getArenas().put(name, arena);
						
						ItemStack is = new ItemStack(Material.BEDROCK);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName("�a�������� ������ 1-�� ������");
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						
						is = is.clone();
						im.setDisplayName("�a�������� ������ 2-�� ������");
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						
						is = new ItemStack(Material.CHEST);
						im = is.getItemMeta();
						im.setDisplayName("�a�������� ������� 1-�� ������");
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						

						is = new ItemStack(Material.CHEST);
						im = is.getItemMeta();
						im.setDisplayName("�a�������� ������� 2-�� ������");
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						
						SM(p, "�2����� �a" + name + "�2 �������.");
						return true;
					}
					
					if (sub.equals("setspawn1"))
					{
						if (!main.getArenas().containsKey(name)) {SM(p, "�c������ ����� �� ����������."); return true;}
						Arena arena = main.getArenas().get(name);
						arena.setPlayer1Spawn(p.getLocation());
						SM(p, "�2����� ������� ������ �� ����� �a" + name + "�2 ����������.");
						return true;
					}

					if (sub.equals("setspawn2"))
					{
						if (!main.getArenas().containsKey(name)) {SM(p, "�c������ ����� �� ����������."); return true;}
						Arena arena = main.getArenas().get(name);
						arena.setPlayer2Spawn(p.getLocation());
						SM(p, "�2����� ������� ������ �� ����� �a" + name + "�2 ����������.");
						return true;
					}
					
					if (sub.equals("setlobby"))
					{
						if (!main.getArenas().containsKey(name)) {SM(p, "�c������ ����� �� ����������."); return true;}
						Arena arena = main.getArenas().get(name);
						arena.setLobbyLocation(p.getLocation());
						SM(p, "�2����� ����� ��� ����� �a" + name + "�2 �����������.");
						return true;
					}
					
					if (sub.equals("join"))
					{
						UUID id = p.getUniqueId();
						if (!main.getArenas().containsKey(name)) {SM(p, "�c������ ����� �� ����������."); return true;}
						if (main.getPlayers().containsKey(id)) {SM(p, "�e�� ��� � ����."); return true;}
						
						Arena arena = main.getArenas().get(name);
						arena.addPlayer(p);
						main.getPlayers().put(id, arena);
						
						SM(p, "�a�� ����� �� �����.");
						return true;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}

}