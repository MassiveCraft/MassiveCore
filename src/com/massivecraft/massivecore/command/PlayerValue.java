package com.massivecraft.massivecore.command;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

import java.io.Serializable;

public interface PlayerValue<T extends Serializable> extends Serializable
{
	T getValue(Player player) throws MassiveException;
	void setValue(T value, Player player) throws MassiveException;
}
