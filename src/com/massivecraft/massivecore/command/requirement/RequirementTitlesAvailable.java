package com.massivecraft.massivecore.command.requirement;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.util.Txt;

public class RequirementTitlesAvailable extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static RequirementTitlesAvailable i = new RequirementTitlesAvailable();
	public static RequirementTitlesAvailable get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return MixinTitle.get().isTitlesAvailable();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return Txt.parse(Lang.COMMAND_TITLES_MUST_BE_AVAILABLE);
	}
	
}
