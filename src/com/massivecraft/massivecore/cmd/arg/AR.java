package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public interface AR<T>
{	
	// Human friendly name
	public String getTypeName();
	
	// Read
	public T read(String arg, CommandSender sender) throws MassiveException;
	public T read(CommandSender sender) throws MassiveException;
	public T read(String arg) throws MassiveException;
	public T read() throws MassiveException;

	// Valid (used for arbitary argument order)
	public boolean isValid(String arg, CommandSender sender);
	
	// Tab list
	// The sender is the one that tried to tab complete.
	// The arg is beginning the word they are trying to tab complete.
	public Collection<String> getTabList(CommandSender sender, String arg);
	public List<String> getTabListFiltered(CommandSender sender, String arg);
	
	// Sometimes we put a space after a tab completion.
	// That would however not make sense with all ArgReaders.
	// Default is true;
	public boolean allowSpaceAfterTab();
}
