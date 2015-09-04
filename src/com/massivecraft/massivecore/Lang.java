package com.massivecraft.massivecore;

import org.bukkit.ChatColor;

import com.massivecraft.massivecore.mson.Mson;
import static com.massivecraft.massivecore.mson.Mson.mson;

public class Lang
{
	public static final String PERM_DEFAULT_DENIED_FORMAT = "<b>You don't have permission to <h>%s<b>.";
	public static final String PERM_DEFAULT_DESCRIPTION = "do that";
	
	public static final String COMMAND_SENDER_MUST_BE_PLAYER = "<b>This command can only be used by ingame players.";
	public static final String COMMAND_SENDER_MUSNT_BE_PLAYER = "<b>This command can not be used by ingame players.";
	public static final String COMMAND_TITLES_MUST_BE_AVAILABLE = "<b>This command requires the Minecraft 1.8 titles.";
	public static final String COMMAND_TOO_FEW_ARGS = "<b>Not enough command input. <i>You should use it like this:";
	public static final String COMMAND_TOO_MANY_ARGS = "<b>Too much command input %s<b>.";
	public static final String COMMAND_TOO_MANY_ARGS2 = "<i>You should use the command like this:";
	public static final Mson COMMAND_REPLACEMENT = mson("REPLACEMENT");
	public static final Mson COMMAND_NO_SUCH_SUB = mson("Couldn't find the command ", COMMAND_REPLACEMENT).color(ChatColor.YELLOW);
	public static final Mson COMMAND_SUGGEST_SUB = mson("Maybe you could try ", COMMAND_REPLACEMENT).color(ChatColor.YELLOW);
	public static final Mson COMMAND_GET_HELP = mson("Use ", COMMAND_REPLACEMENT, " to see commands.").color(ChatColor.YELLOW);
	public static final String COMMAND_TOO_MANY_TAB_SUGGESTIONS = "<h>%d <b>tab completions available. Be more specific and try again.";
}
