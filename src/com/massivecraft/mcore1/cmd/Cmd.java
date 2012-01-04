package com.massivecraft.mcore1.cmd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.cmd.arg.AHBoolean;
import com.massivecraft.mcore1.cmd.arg.AHDouble;
import com.massivecraft.mcore1.cmd.arg.AHFloat;
import com.massivecraft.mcore1.cmd.arg.AHInteger;
import com.massivecraft.mcore1.cmd.arg.AHMaterial;
import com.massivecraft.mcore1.cmd.arg.AHPlayer;
import com.massivecraft.mcore1.cmd.arg.IArgHandler;

public class Cmd
{
	protected Map<Class<?>, IArgHandler<?>> argHandlers = new HashMap<Class<?>, IArgHandler<?>>();
	public Map<Class<?>, IArgHandler<?>> getArgHandlers() { return this.argHandlers; }
	@SuppressWarnings("unchecked")
	public <T> IArgHandler<T> getArgHandler(Class<T> clazz) { return (IArgHandler<T>) this.argHandlers.get(clazz); }
	public <T> void setArgHandler(Class<T> clazz, IArgHandler<T> handler) { this.argHandlers.put(clazz, handler); }
	
	protected Set<MCommand> commands = new HashSet<MCommand>();
	public Set<MCommand> getCommands() { return this.commands; }
	public void addCommand(MCommand mcommand) { this.commands.add(mcommand); }
	public MCommand getCommand(String alias)
	{
		for (MCommand command : this.commands)
		{
			if (command.aliases.contains(alias)) return command;
		}
		return null;
	}
	
	public boolean handleCommand(CommandSender sender, String alias, List<String> args, boolean testOnly)
	{
		MCommand mcommand = this.getCommand(alias);
		if (mcommand == null) return false;
		if (testOnly) return true;
		mcommand.execute(sender, args);
		return true;
	}
	
	public Cmd()
	{
		this.setArgHandler(Boolean.class, new AHBoolean());
		this.setArgHandler(Double.class, new AHDouble());
		this.setArgHandler(Float.class, new AHFloat());
		this.setArgHandler(Integer.class, new AHInteger());
		this.setArgHandler(Material.class, new AHMaterial());
		this.setArgHandler(Player.class, new AHPlayer());
	}
}
