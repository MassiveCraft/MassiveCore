package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.util.Txt;

public class HelpCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static HelpCommand i = new HelpCommand();
	public static HelpCommand get() { return i; }
	private HelpCommand()
	{
		this.addAliases("?", "h", "help");
		this.setDesc("");
		this.addOptionalArg("page","1");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		if (this.getCommandChain().size() == 0) return;
		MassiveCommand parentCommand = this.getCommandChain().get(this.getCommandChain().size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for (String helpline : parentCommand.getHelp())
		{
			lines.add(Txt.parse("<a>#<i> "+helpline));
		}
		
		for (MassiveCommand subCommand : parentCommand.getSubCommands())
		{
			if (subCommand.isVisibleTo(sender))
			{
				lines.add(subCommand.getUseageTemplate(this.getCommandChain(), true, true, sender));
			}
		}
		
		Integer pagenumber = this.arg(0, ARInteger.get(), 1);
		if (pagenumber == null) return;
		sendMessage(Txt.getPage(lines, pagenumber, "Help for command \""+parentCommand.getAliases().get(0)+"\"", sender));
	}
	
}
