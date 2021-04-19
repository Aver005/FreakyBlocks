package kiviuly.FreakyBlocks;

import org.bukkit.Material;

public class FreakBlock
{
	private int ID = 0;
	private String name;
	private String desc = "";
	
	private Material material = Material.LOG;
	private FreakCategory category = FreakCategory.Wood;
	
	private int price = 1;
	
	private int health = 1;
	private int damage = 1;
	
	
	
	
	
	public int getID()
	{
		return ID;
	}
	public void setID(int iD)
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
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public Material getMaterial()
	{
		return material;
	}
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	public FreakCategory getCategory()
	{
		return category;
	}
	public void setCategory(FreakCategory category)
	{
		this.category = category;
	}
	public int getPrice()
	{
		return price;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public int getDamage()
	{
		return damage;
	}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
}
