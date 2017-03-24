package com.massivecraft.massivecore.command.requirement;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

public class RequirementEditorUse extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static RequirementEditorUse i = new RequirementEditorUse();
	public static RequirementEditorUse get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		if ( ! (command instanceof CommandEditAbstract)) return false;
		CommandEditAbstract<?, ?> commandEditor = (CommandEditAbstract<?, ?>)command;
		return commandEditor.getSettings().getUsed(sender) != null;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		if ( ! (command instanceof CommandEditAbstract)) return Txt.parse("<b>This is not an editor!");
		CommandEditAbstract<?, ?> commandEditor = (CommandEditAbstract<?, ?>)command;
		
		String noun = commandEditor.getSettings().getObjectType().getName();
		String aan = Txt.aan(noun);
		
		return Txt.parse("<b>You must use %s %s to edit it.", aan, noun);
	}
	
}
