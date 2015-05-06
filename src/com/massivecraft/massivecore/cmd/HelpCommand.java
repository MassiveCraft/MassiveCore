package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
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
		this.addArg(ARInteger.get(), "page", "1");
		
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
		int pagenumber = this.readArg(1); 
		
		// Get parent command
		if ( ! this.hasParentCommand()) return;
		MassiveCommand parentCommand = this.getParentCommand();
		
		// Create Lines
		ArrayList<String> lines = new ArrayList<String>();
		for (String helpline : parentCommand.getHelp())
		{
			lines.add(Txt.parse("<a>#<i> " + helpline));
		}
		
		for (MassiveCommand subCommand : parentCommand.getSubCommands())
		{
			if (subCommand.isVisibleTo(sender))
			{
				lines.add(subCommand.getUseageTemplate(this.getCommandChain(), true, true, sender));
			}
		}
		
		// Send Lines
		sendMessage(Txt.getPage(lines, pagenumber, "Help for command \"" + parentCommand.getAliases().get(0) + "\"", sender));
	}
	
}
