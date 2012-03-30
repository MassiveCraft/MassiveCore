package com.massivecraft.mcore2.cmd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import com.massivecraft.mcore2.cmd.arg.AHBoolean;
import com.massivecraft.mcore2.cmd.arg.AHByte;
import com.massivecraft.mcore2.cmd.arg.AHDate;
import com.massivecraft.mcore2.cmd.arg.AHDouble;
import com.massivecraft.mcore2.cmd.arg.AHFloat;
import com.massivecraft.mcore2.cmd.arg.AHInteger;
import com.massivecraft.mcore2.cmd.arg.AHMaterial;
import com.massivecraft.mcore2.cmd.arg.AHPlayer;
import com.massivecraft.mcore2.cmd.arg.AHWorld;
import com.massivecraft.mcore2.cmd.arg.IArgHandler;

public class Cmd
{
	protected Map<Class<?>, IArgHandler<?>> argHandlers = new HashMap<Class<?>, IArgHandler<?>>();
	public Map<Class<?>, IArgHandler<?>> getArgHandlers() { return this.argHandlers; }
	@SuppressWarnings("unchecked")
	public <T> IArgHandler<T> getArgHandler(Class<T> clazz) { return (IArgHandler<T>) this.argHandlers.get(clazz); }
	public <T> void setArgHandler(Class<T> clazz, IArgHandler<T> handler) { this.argHandlers.put(clazz, handler); }
	
	/**
	 * @deprecated As of MCore 3, replaced by by {@link MCommand#register()}
	 */
	@Deprecated
	public void addCommand(MCommand mcommand)
	{
		mcommand.register();
	}
	
	public Cmd()
	{
		this.setArgHandler(Boolean.class, new AHBoolean());
		this.setArgHandler(Byte.class, new AHByte());
		this.setArgHandler(Double.class, new AHDouble());
		this.setArgHandler(Date.class, new AHDate());
		this.setArgHandler(Float.class, new AHFloat());
		this.setArgHandler(Integer.class, new AHInteger());
		this.setArgHandler(Material.class, new AHMaterial());
		this.setArgHandler(Player.class, new AHPlayer());
		this.setArgHandler(World.class, new AHWorld());
	}
	
	public static SimpleCommandMap getBukkitCommandMap()
	{
		CraftServer craftServer = (CraftServer)Bukkit.getServer();
		return craftServer.getCommandMap();
	}
}
