package com.massivecraft.mcore5.cmd;

import java.util.ArrayList;

import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.cmd.arg.ARInteger;
import com.massivecraft.mcore5.util.Txt;

public class HelpCommand extends MCommand
{
	private HelpCommand()
	{
		super();
		this.addAliases("?", "h", "help");
		this.setDesc("");
		this.addOptionalArg("page","1");
	}
	
	@Override
	public void perform()
	{
		if (this.commandChain.size() == 0) return;
		MCommand parentCommand = this.commandChain.get(this.commandChain.size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		for (String helpline : parentCommand.getHelp())
		{
			lines.add(Txt.parse("<a>#<i> "+helpline));
		}
		
		for(MCommand subCommand : parentCommand.getSubCommands())
		{
			if (subCommand.visibleTo(sender))
			{
				lines.add(subCommand.getUseageTemplate(this.commandChain, true, true));
			}
		}
		
		Integer pagenumber = this.arg(0, ARInteger.get(), 1);
		if (pagenumber == null) return;
		sendMessage(Txt.getPage(lines, pagenumber, "Help for command \""+parentCommand.getAliases().get(0)+"\"", sender));
	}

	@Override
	public MPlugin p()
	{
		if (this.commandChain.size() == 0) return null;
		return this.commandChain.get(this.commandChain.size()-1).p();
	}
	
	private static HelpCommand instance = new HelpCommand();
	public static HelpCommand getInstance()
	{
		return instance;
	}
}
