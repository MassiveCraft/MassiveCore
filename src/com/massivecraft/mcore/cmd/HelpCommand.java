package com.massivecraft.mcore.cmd;

import java.util.ArrayList;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.util.Txt;

public class HelpCommand extends MCommand
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
		MCommand parentCommand = this.getCommandChain().get(this.getCommandChain().size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for (String helpline : parentCommand.getHelp())
		{
			lines.add(Txt.parse("<a>#<i> "+helpline));
		}
		
		for (MCommand subCommand : parentCommand.getSubCommands())
		{
			if (subCommand.visibleTo(sender))
			{
				lines.add(subCommand.getUseageTemplate(this.getCommandChain(), true, true, sender));
			}
		}
		
		Integer pagenumber = this.arg(0, ARInteger.get(), 1);
		if (pagenumber == null) return;
		sendMessage(Txt.getPage(lines, pagenumber, "Help for command \""+parentCommand.getAliases().get(0)+"\"", sender));
	}
	
}
