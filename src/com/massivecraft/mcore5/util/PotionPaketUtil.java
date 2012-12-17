package com.massivecraft.mcore5.util;

import net.minecraft.server.Packet41MobEffect;
import net.minecraft.server.Packet42RemoveMobEffect;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionPaketUtil
{	
	// -------------------------------------------- //
	// ASSUMING THE PLAYER SELF
	// -------------------------------------------- //
	
	public static void add(Player player, PotionEffectType potionEffectType, int amplifier, int duration)
	{
		add(player, player, potionEffectType, amplifier, duration);
	}
	
	public static void add(Player player, PotionEffect potionEffect)
	{
		add(player, player, potionEffect);
	}
	
	public static void remove(Player player, PotionEffectType potionEffectType) 
	{
		remove(player, player, potionEffectType);
	}
	
	// -------------------------------------------- //
	// USING BUKKIT STUFF
	// -------------------------------------------- //
	
	public static void add(Player player, Entity entity, PotionEffectType potionEffectType, int amplifier, int duration)
	{
		add(player, entity.getEntityId(), (byte)potionEffectType.getId(), (byte)amplifier, (short)duration);
	}
	
	public static void add(Player player, Entity entity, PotionEffect potionEffect)
	{
		add(player, entity.getEntityId(), (byte)potionEffect.getType().getId(), (byte)potionEffect.getAmplifier(), (short)potionEffect.getDuration());
	}
	
	public static void remove(Player player, Entity entity, PotionEffectType potionEffectType) 
	{
		remove(player, entity.getEntityId(), (byte)potionEffectType.getId());
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public static void add(Player player, int entityId, byte effectId, byte amplifier, short duration)
	{
		Packet41MobEffect pm = new Packet41MobEffect();
		pm.a = entityId;
		pm.b = effectId;
		pm.c = amplifier;
		pm.d = duration;
		((CraftPlayer)player).getHandle().netServerHandler.sendPacket(pm);
	}
	
	public static void remove(Player player, int entityId, byte effectId) 
	{
		Packet42RemoveMobEffect pr = new Packet42RemoveMobEffect();
		pr.a = entityId;
		pr.b = effectId;
		((CraftPlayer)player).getHandle().netServerHandler.sendPacket(pr);
	}

}
