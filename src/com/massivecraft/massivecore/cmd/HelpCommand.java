package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;

public class HelpCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	protected static HelpCommand i = new HelpCommand();
	public static HelpCommand get() { return i; }
	private HelpCommand()
	{
		// Aliases
		this.addAliases("?", "h", "help");
		
		// Args
		this.addArg(ArgSetting.getPage());
		
		// Other
		this.setDesc("");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Args
		int page = this.readArg(); 
		
		// Get parent command
		if ( ! this.hasParentCommand()) return;
		MassiveCommand parentCommand = this.getParentCommand();
		
		// Create Lines
		List<Mson> lines = new ArrayList<Mson>();
		for (String helpline : parentCommand.getHelp())
		{
			lines.add(Mson.parse("<a>#<i> " + helpline));
		}
		
		for (MassiveCommand subCommand : parentCommand.getSubCommands())
		{
			if ( ! subCommand.isVisibleTo(sender)) continue;
			lines.add(subCommand.getUseageTemplate(this.getCommandChain(), true, true, sender));
		}
		
		// Send Lines
		message(Txt.getPage(lines, page, "Help for command \"" + parentCommand.getAliases().get(0) + "\"", this));
	}
	
	@Override
	public boolean isVisibleTo(CommandSender sender)
	{
		boolean visible = super.isVisibleTo(sender);
		if ( ! (this.hasParentCommand() && visible)) return visible;
		
		int pageHeight = (sender instanceof Player) ? Txt.PAGEHEIGHT_PLAYER : Txt.PAGEHEIGHT_CONSOLE;
		int size = this.getParentCommand().getSubCommands().size();
		
		if (size <= pageHeight)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
}
