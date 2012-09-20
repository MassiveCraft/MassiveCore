package com.massivecraft.mcore4.cmd;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import com.massivecraft.mcore4.cmd.arg.old.AHAspect;
import com.massivecraft.mcore4.cmd.arg.old.AHBoolean;
import com.massivecraft.mcore4.cmd.arg.old.AHByte;
import com.massivecraft.mcore4.cmd.arg.old.AHDate;
import com.massivecraft.mcore4.cmd.arg.old.AHDouble;
import com.massivecraft.mcore4.cmd.arg.old.AHEnvironment;
import com.massivecraft.mcore4.cmd.arg.old.AHFloat;
import com.massivecraft.mcore4.cmd.arg.old.AHInteger;
import com.massivecraft.mcore4.cmd.arg.old.AHMaterial;
import com.massivecraft.mcore4.cmd.arg.old.AHMultiverse;
import com.massivecraft.mcore4.cmd.arg.old.AHPlayer;
import com.massivecraft.mcore4.cmd.arg.old.AHWorld;
import com.massivecraft.mcore4.cmd.arg.old.AHWorldType;
import com.massivecraft.mcore4.cmd.arg.old.IArgHandler;
import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.usys.Multiverse;

public class Cmd
{
	// TODO: The cmd will no longer need to be instantiated.
	// TODO: Perhaps I should remove the cmd system soon.
	
	protected Map<Class<?>, IArgHandler<?>> argHandlers = new HashMap<Class<?>, IArgHandler<?>>();
	public Map<Class<?>, IArgHandler<?>> getArgHandlers() { return this.argHandlers; }
	@SuppressWarnings("unchecked")
	public <T> IArgHandler<T> getArgHandler(Class<T> clazz) { return (IArgHandler<T>) this.argHandlers.get(clazz); }
	public <T> void setArgHandler(Class<T> clazz, IArgHandler<T> handler) { this.argHandlers.put(clazz, handler); }
	
	public Cmd()
	{
		this.setArgHandler(Aspect.class, new AHAspect());
		this.setArgHandler(Boolean.class, new AHBoolean());
		this.setArgHandler(Byte.class, new AHByte());
		this.setArgHandler(Date.class, new AHDate());
		this.setArgHandler(Double.class, new AHDouble());
		this.setArgHandler(Environment.class, new AHEnvironment());
		this.setArgHandler(Float.class, new AHFloat());
		this.setArgHandler(Integer.class, new AHInteger());
		this.setArgHandler(Material.class, new AHMaterial());
		this.setArgHandler(Multiverse.class, new AHMultiverse());
		this.setArgHandler(Player.class, new AHPlayer());
		this.setArgHandler(World.class, new AHWorld());
		this.setArgHandler(WorldType.class, new AHWorldType());
	}
	
	public static SimpleCommandMap getBukkitCommandMap()
	{
		CraftServer craftServer = (CraftServer)Bukkit.getServer();
		return craftServer.getCommandMap();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Command> getKnownCommandsFromSimpleCommandMap(SimpleCommandMap scm)
	{
		try
		{
			Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
			field.setAccessible(true);
			return (Map<String, Command>) field.get(scm);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
