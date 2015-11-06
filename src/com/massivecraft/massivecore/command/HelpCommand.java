package com.massivecraft.massivecore.command;

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
		
		// Parameters
		this.addParameter(Parameter.getPage());
		
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
		if ( ! this.hasParent()) return;
		MassiveCommand parent = this.getParent();
		
		// Create Lines
		List<Mson> lines = new ArrayList<Mson>();
		for (String helpline : parent.getHelp())
		{
			lines.add(Mson.parse("<a>#<i> " + helpline));
		}
		
		for (MassiveCommand child : parent.getChildren())
		{
			if ( ! child.isVisibleTo(sender)) continue;
			lines.add(child.getTemplate(this.getChain(), true, true, sender));
		}
		
		// Send Lines
		message(Txt.getPage(lines, page, "Help for command \"" + parent.getAliases().get(0) + "\"", this));
	}
	
	@Override
	public boolean isVisibleTo(CommandSender sender)
	{
		boolean visible = super.isVisibleTo(sender);
		if ( ! (this.hasParent() && visible)) return visible;
		
		int pageHeight = (sender instanceof Player) ? Txt.PAGEHEIGHT_PLAYER : Txt.PAGEHEIGHT_CONSOLE;
		int size = this.getParent().getChildren().size();
		
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
