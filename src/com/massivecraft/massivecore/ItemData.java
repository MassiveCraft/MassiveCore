package com.massivecraft.massivecore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;

import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.collections.MassiveMapDef;

public class ItemData
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// Immutable Defaults
	public static final transient int DEFAULT_ID = 0;
	public static final transient int DEFAULT_COUNT = 1;
	public static final transient int DEFAULT_DAMAGE = 0;
	public static final transient boolean DEFAULT_SCALING = false;
	
	// -------------------------------------------- //
	// FIELDS > BASIC
	// -------------------------------------------- //
	
	private Integer id = null;
	public int getId() { return (this.id == null ? DEFAULT_ID : this.id); }
	public ItemData setId(int id) { this.id = (id == DEFAULT_ID ? null : id); return this; }
	
	private Integer count = null;
	public int getCount() { return (this.count == null ? DEFAULT_COUNT : this.count); }
	public ItemData setCount(int count) { this.count = (count == DEFAULT_COUNT ? null : count); return this; }
	
	private Integer damage = null;
	public int getDamage() { return (this.damage == null ? DEFAULT_DAMAGE : this.damage); }
	public ItemData setDamage(int damage) { this.damage = (damage == DEFAULT_DAMAGE ? null: damage); return this; }
	
	// -------------------------------------------- //
	// FIELDS > UNSPECIFIC
	// -------------------------------------------- //
	
	private String name = null;
	public String getName() { return this.name; }
	public ItemData setName(String name) { this.name = name; return this; }
	
	private MassiveListDef<String> lore = new MassiveListDef<>();
	public List<String> getLore() { return this.lore; }
	public ItemData setLore(Collection<String> lore) { this.lore = new MassiveListDef<>(lore); return this;}
	
	private MassiveMapDef<Enchantment, Integer> enchants = new MassiveMapDef<>();
	public Map<Enchantment, Integer> getEnchants() { return this.enchants; }
	public ItemData setEnchants(Map<Enchantment, Integer> enchants) { this.enchants = new MassiveMapDef<>(enchants); return this; }
	
	private Integer repaircost = null;
	public Integer getRepaircost() { return this.repaircost; }
	public ItemData setRepaircost(int repaircost) { this.repaircost = repaircost; return this; }
	
	// -------------------------------------------- //
	// FIELDS > BOOK
	// -------------------------------------------- //
	
	private String title = null;
	public String getTitle() { return this.title; }
	public ItemData setTitle(String title) { this.title = title; return this; }
	
	private String author = null;
	public String getAuthor() { return this.author; }
	public ItemData setAuthor(String author) { this.author = author; return this; }
	
	private MassiveListDef<String> pages = new MassiveListDef<>();
	public List<String> getPages() { return this.pages; }
	public ItemData setPages(Collection<String> bookPages) { this.pages = new MassiveListDef<>(bookPages); return this; }
	
	// -------------------------------------------- //
	// FIELDS > LEATHER ARMOR
	// -------------------------------------------- //
	
	private Color color = null;
	public Color getColor() { return (this.color == null ? Bukkit.getItemFactory().getDefaultLeatherColor() : this.color); }
	public ItemData setColor(Color color) { this.color = (Bukkit.getItemFactory().getDefaultLeatherColor().equals(color) ? null : color); return this; }
	
	// -------------------------------------------- //
	// FIELDS > MAP
	// -------------------------------------------- //
	
	private Boolean scaling = null;
	public boolean isScaling() { return (this.scaling == null ? DEFAULT_SCALING : this.scaling); }
	public ItemData setScaling(boolean scaling) { this.scaling = (scaling == DEFAULT_SCALING ? null : scaling); return this; }
	
	// -------------------------------------------- //
	// FIELDS > ...
	// -------------------------------------------- //
	
	// TODO: Add all the fields
	
	// -------------------------------------------- //
	// CONVERT
	// -------------------------------------------- //
	
	// TODO: Add in covert methods... they will have to use mixins for transfering!
	
	// -------------------------------------------- //
	// HASH CODE & EQUALS
	// -------------------------------------------- //
	
	// TODO
	
}
