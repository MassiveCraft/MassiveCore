package com.massivecraft.massivecore.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.nms.NmsBoard;

// # RESEARCH > CLEANUP
// The main server scoreboard is the only one that is saved to NBT.
// We must make sure to clean up after ourselves if we use that one.
// The other scoreboards are just temporary.
// For this reason there is a value to avoiding using the main scoreboard at all.
// However so long as we clean up after ourselves properly there is a simplicity to using all known scoreboards.
//
// # RESEARCH > DEFAULT TEAM
// Per default players have no team.
// To disable collisions we must set a team flag.
// This means some sort of default team creation can be useful.
// For this we use so called personal teams with only one member.
//
// # TERMIOLOGY
// Board: the "score board"
// Objective: the score board "objective"
// Id: the unchangeable "name"
// Name: the changeable "display name"
// Slot: the "display slot"
// Entries: Map from key to value
// Key: the player name or stringified entity uuid
// Value: the integer objective score value
// Team: the score board team
// Members: the score board team members. These are of Key type.
//
// # DESIGN
// NoChange: Do not trigger network packets in vain through detecting "same setting".
public class BoardUtil extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BoardUtil i = new BoardUtil();
	public static BoardUtil get() { return i; }
	public BoardUtil()
	{
		this.setPeriod(1L);
	}
	
	// -------------------------------------------- //
	// DATA
	// -------------------------------------------- //
	
	// All online players at the beginning of the tick.
	private static Map<String, Player> players;
	public static Map<String, Player> getPlayers() { return players; }
	
	// The boards based off the players above.
	private static Set<Scoreboard> boards;
	public static Set<Scoreboard> getBoards() { return boards; }
	
	// Ensure things, possibly strictly.
	
	private static boolean ensureBoardEnabled = false;
	public static boolean isEnsureBoardEnabled() { return ensureBoardEnabled; }
	public static void setEnsureBoardEnabled() { ensureBoardEnabled = true; }
	
	private static boolean ensureBoardStrict = false;
	public static boolean isEnsureBoardStrict() { return ensureBoardStrict; }
	public static void setEnsureBoardStrict() { ensureBoardStrict = true; }
	
	private static boolean ensureTeamEnabled = false;
	public static boolean isEnsureTeamEnabled() { return ensureTeamEnabled; }
	public static void setEnsureTeamEnabled() { ensureTeamEnabled = true; }
	
	private static boolean ensureTeamStrict = false;
	public static boolean isEnsureTeamStrict() { return ensureTeamStrict; }
	public static void setEnsureTeamStrict() { ensureTeamStrict = true; }
	
	// Temporary Fake Fields
	public static Set<Objective> temporaryObjectives = new MassiveSet<>();
	public static Set<Objective> getTemporaryObjectives() { return temporaryObjectives; }
	
	public static Set<Team> temporaryTeams = new MassiveSet<>();
	public static Set<Team> getTemporaryTeams() { return temporaryTeams; }
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if (active)
		{
			// We do not trigger an update here.
			// We must wait for the first server tick.
			// Otherwise the Scoreboard manager is null.
		}
		else
		{
			// We delete everything marked as temporary on deactivation.
			
			List<Objective> objectives = new MassiveList<>(getTemporaryObjectives());
			for (Objective objective : objectives)
			{
				deleteObjective(objective);
			}
			
			List<Team> teams = new MassiveList<>(getTemporaryTeams());
			for (Team team : teams)
			{
				deleteTeam(team);
			}
		}
	}
	
	@Override
	public void run()
	{
		update();
	}
	
	public static void update()
	{
		updatePlayers();
		updateBoards();
		updateEnsure();
	}
	
	public static void updatePlayers()
	{
		// Create
		Map<String, Player> players = new MassiveMap<>();
		
		// Fill
		for (Player player : MUtil.getOnlinePlayers())
		{
			players.put(player.getName(), player);
		}
		players = Collections.unmodifiableMap(players);
		
		// Set
		BoardUtil.players = players;
	}
	
	public static void updateBoards()
	{
		// Create
		Set<Scoreboard> boards = new MassiveSet<>();
		
		// Fill > Simple
		boards.add(getBoardMain());
		boards.add(getBoardOur());
		
		// Fill > Players
		for (Player player : getPlayers().values())
		{
			Scoreboard board = getBoard(player);
			boards.add(board);
		}
		
		// Set
		boards = Collections.unmodifiableSet(boards);
		BoardUtil.boards = boards;
	}
	
	public static void updateEnsure()
	{
		for (Player player : getPlayers().values())
		{
			if (isEnsureBoardEnabled())
			{
				ensureBoard(player, isEnsureBoardStrict());
			}
			
			if (isEnsureTeamEnabled())
			{
				for (Scoreboard board : getBoards())
				{
					ensureTeam(board, player, isEnsureTeamStrict());
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// ENSURE
	// -------------------------------------------- //
	
	public static Scoreboard ensureBoard(Player player, boolean strict)
	{
		Scoreboard board = getBoard(player);
		
		if (isBoardOur(board)) return board;
		if ( ! strict && ! isBoardMain(board)) return board;
		
		board = getBoardOur();
		setBoard(player, board);
		
		return board;
	}
	
	public static Team ensureTeam(Scoreboard board, Player player, boolean strict)
	{
		Team team = getKeyTeam(board, player);
		
		if (isPersonalTeam(team, player)) return team;
		if ( ! strict && team != null) return team;
		
		team = getPersonalTeam(board, player, true);
		return team;
	}
	
	// -------------------------------------------- //
	// CLEAN
	// -------------------------------------------- //
	
	public static void clean(Player player)
	{
		// Delete scores for temporary objectives.
		for (Objective objective : getTemporaryObjectives())
		{
			setObjectiveValue(objective, player, 0);
		}
		
		// Delete player team if temporary and sole player.
		for (Scoreboard board : getBoards())
		{
			Team team = getKeyTeam(board, player);
			if (isTeamPersistent(team)) continue;
			if (getTeamMembers(team).size() > 1) continue;
			deleteTeam(team);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void clean(final PlayerQuitEvent event)
	{
		Bukkit.getScheduler().runTask(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				clean(event.getPlayer());
			}
		});
	}
	
	// -------------------------------------------- //
	// KEY
	// -------------------------------------------- //
	
	public static String getKey(Object key)
	{
		if (key == null) return null;
		if (key instanceof String) return (String)key;
		if (key instanceof Player) return ((Player)key).getName();
		if (key instanceof Entity) return ((Entity)key).getUniqueId().toString();
		throw new IllegalArgumentException(key.toString());
	}
	
	// -------------------------------------------- //
	// BOARD
	// -------------------------------------------- //
	
	public static Scoreboard getBoard(Player player)
	{
		return player.getScoreboard();
	}
	
	public static void setBoard(Player player, Scoreboard board)
	{
		player.setScoreboard(board);
	}
	
	// -------------------------------------------- //
	// BOARD > MAIN
	// -------------------------------------------- //
	
	public static Scoreboard getBoardMain()
	{
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}
	
	public static boolean isBoardMain(Scoreboard board)
	{
		return getBoardMain().equals(board);
	}
	
	// -------------------------------------------- //
	// BOARD > OUR
	// -------------------------------------------- //
	
	private static Scoreboard BOARD_OUR = null;
	
	public static Scoreboard getBoardOur()
	{
		if (BOARD_OUR == null) BOARD_OUR = Bukkit.getScoreboardManager().getNewScoreboard();
		return BOARD_OUR;
	}
	
	public static boolean isBoardOur(Scoreboard board)
	{
		return getBoardOur().equals(board);
	}
	
	// -------------------------------------------- //
	// OBJECTIVE
	// -------------------------------------------- //
	
	// Note that "dummy" actually seems to be the right word with a certain meaning to the vanilla server.
	// http://minecraft.gamepedia.com/Scoreboard
	public static final String OBJECTIVE_CRITERIA_DUMMY = "dummy";
	
	public static Objective createObjective(Scoreboard board, String id)
	{
		return board.registerNewObjective(id, OBJECTIVE_CRITERIA_DUMMY);
	}
	
	public static Objective getObjective(Scoreboard board, String id, boolean creative)
	{
		Objective objective = board.getObjective(id);
		if (objective == null && creative) createObjective(board, id);
		return objective;
	}
	
	public static void deleteObjective(Objective objective)
	{
		if (objective == null) return;
		getTemporaryObjectives().remove(objective);
		try
		{
			objective.unregister();
		}
		catch (IllegalStateException e)
		{
			// Already Done
		}
	}
	
	public static void deleteObjective(Scoreboard board, String id)
	{
		Objective objective = board.getObjective(id);
		deleteObjective(objective);
	}
	
	public static boolean setObjective(Objective objective, Boolean persistent, String name, DisplaySlot slot, Map<String, Integer> entries)
	{
		boolean ret = false;
		ret |= setObjectivePersistent(objective, persistent);
		ret |= setObjectiveName(objective, name);
		ret |= setObjectiveSlot(objective, slot);
		ret |= setObjectiveEntries(objective, entries);
		return ret;
	}
	
	public static boolean setObjective(Objective objective, Objective blueprint)
	{
		return setObjective(objective,
			isObjectivePersistent(blueprint),
			getObjectiveName(blueprint),
			getObjectiveSlot(blueprint),
			getObjectiveEntries(blueprint)
		);
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > ID
	// -------------------------------------------- //
	
	public static String getObjectiveId(Objective objective)
	{
		return objective.getName();
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > PERSISTENT
	// -------------------------------------------- //
	
	public static boolean isObjectivePersistent(Objective objective)
	{
		return ! getTemporaryObjectives().contains(objective);
	}
	
	public static boolean setObjectivePersistent(Objective objective, Boolean persistent)
	{
		if (persistent == null) return false;
		
		if (persistent)
		{
			return getTemporaryObjectives().remove(objective);
		}
		else
		{
			return getTemporaryObjectives().add(objective);
		}
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > NAME
	// -------------------------------------------- //
	
	public static String getObjectiveName(Objective objective)
	{
		return objective.getDisplayName();
	}
	
	public static boolean setObjectiveName(Objective objective, String name)
	{
		if (name == null) return false;
		String before = getObjectiveName(objective);
		if (MUtil.equals(before, name)) return false;
		objective.setDisplayName(name);
		return true;
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > SLOT
	// -------------------------------------------- //
	
	public static DisplaySlot getObjectiveSlot(Objective objective)
	{
		return objective.getDisplaySlot();
	}
	
	public static boolean setObjectiveSlot(Objective objective, DisplaySlot slot)
	{
		if (slot == null) return false;
		DisplaySlot before = getObjectiveSlot(objective);
		if (MUtil.equals(before, slot)) return false;
		objective.setDisplaySlot(slot);
		return true;
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > VALUE
	// -------------------------------------------- //
	
	public static int getObjectiveValue(Objective objective, Object key)
	{
		Score score = objective.getScore(getKey(key));
		return getScoreValue(score);
	}
	
	public static boolean setObjectiveValue(Objective objective, Object key, Integer value)
	{
		if (value == null) return false;
		Score score = objective.getScore(getKey(key));
		return setScoreValue(score, value);
	}
	
	// -------------------------------------------- //
	// OBJECTIVE > ENTRIES
	// -------------------------------------------- //
	
	public static Map<String, Integer> getObjectiveEntries(Objective objective)
	{
		// Create
		Map<String, Integer> ret = new MassiveMap<>();
		
		// Fill
		for (String key : objective.getScoreboard().getEntries())
		{
			int value = getObjectiveValue(objective, key); 
			if (value == 0) continue;
			ret.put(key, value);
		}
		
		// Return
		return ret;
	}
	
	public static boolean setObjectiveEntries(Objective objective, Map<String, Integer> entries)
	{
		if (entries == null) return false;
		boolean ret = false;
		
		// Add or Update
		for (Entry<String, Integer> entry : entries.entrySet())
		{
			String key = entry.getKey();
			Integer value = entry.getValue();
			ret |= setObjectiveValue(objective, key, value);
		}
		
		// Remove
		for (String key : objective.getScoreboard().getEntries())
		{
			if (entries.containsKey(key)) continue;
			ret |= setObjectiveValue(objective, key, 0);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// SCORE > VALUE
	// -------------------------------------------- //
	
	public static int getScoreValue(Score score)
	{
		return score.getScore();
	}
	
	public static boolean setScoreValue(Score score, Integer value)
	{
		if (value == null) return false;
		int before = getScoreValue(score);
		if (before == value) return false;
		score.setScore(value);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM
	// -------------------------------------------- //
	
	public static Team createTeam(Scoreboard board, String id)
	{
		return board.registerNewTeam(id);
	}
	
	public static Team getTeam(Scoreboard board, String id, boolean creative)
	{
		Team team = board.getTeam(id);
		if (team == null && creative) team = createTeam(board, id);
		return team;
	}
	
	public static void deleteTeam(Team team)
	{
		if (team == null) return;
		getTemporaryTeams().remove(team);
		try
		{
			team.unregister();
		}
		catch (IllegalStateException e)
		{
			// Already Done
		}
	}
	
	public static void deleteTeam(Scoreboard board, String id)
	{
		Team team = board.getTeam(id);
		deleteTeam(team);
	}
	
	public static boolean setTeam(Team team, Boolean persistent, String name, String prefix, String suffix, ChatColor color, Boolean friendlyFireEnabled, Boolean friendlyTruesightEnabled, Map<Option, OptionStatus> options, Set<String> members)
	{
		boolean ret = false;
		ret |= setTeamPersistent(team, persistent);
		ret |= setTeamName(team, name);
		ret |= setTeamPrefix(team, prefix);
		ret |= setTeamSuffix(team, suffix);
		ret |= setTeamColor(team, color);
		ret |= setTeamFriendlyFireEnabled(team, friendlyFireEnabled);
		ret |= setTeamFriendlyTruesightEnabled(team, friendlyTruesightEnabled);
		ret |= setTeamOptions(team, options);
		ret |= setTeamMembers(team, members);
		return ret;
	}
	
	public static boolean setTeam(Team team, Team blueprint)
	{
		return setTeam(team,
			isTeamPersistent(blueprint),
			getTeamName(blueprint),
			getTeamPrefix(blueprint),
			getTeamSuffix(blueprint),
			getTeamColor(blueprint),
			isTeamFriendlyFireEnabled(blueprint),
			isTeamFriendlyTruesightEnabled(blueprint),
			getTeamOptions(blueprint),
			getTeamMembers(blueprint)
		);
	}
	
	// -------------------------------------------- //
	// TEAM > SEND
	// -------------------------------------------- //
	
	public static void sendTeamUpdate(Team team)
	{
		team.setDisplayName(team.getDisplayName());
	}
	
	public static void sendTeamUpdate(Team team, Player player)
	{
		NmsBoard.get().sendTeamUpdatePacket(team, player);
	}
	
	// -------------------------------------------- //
	// TEAM > ID
	// -------------------------------------------- //
	
	public static String getTeamId(Team team)
	{
		return team.getName();
	}
	
	// -------------------------------------------- //
	// TEAM > PERSISTENT
	// -------------------------------------------- //
	
	public static boolean isTeamPersistent(Team team)
	{
		return ! getTemporaryTeams().contains(team);
	}
	
	public static boolean setTeamPersistent(Team team, Boolean persistent)
	{
		if (persistent == null) return false;
		
		if (persistent)
		{
			return getTemporaryTeams().remove(team);
		}
		else
		{
			return getTemporaryTeams().add(team);
		}
	}
	
	// -------------------------------------------- //
	// TEAM > NAME
	// -------------------------------------------- //
	
	public static String getTeamName(Team team)
	{
		return team.getDisplayName();
	}
	
	public static boolean setTeamName(Team team, String name)
	{
		if (name == null) return false;
		String before = getTeamName(team);
		if (MUtil.equals(before, name)) return false;
		team.setDisplayName(name);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > PREFIX
	// -------------------------------------------- //
	
	public static String getTeamPrefix(Team team)
	{
		return team.getPrefix();
	}
	
	public static boolean setTeamPrefix(Team team, String prefix)
	{
		if (prefix == null) return false;
		String before = getTeamPrefix(team);
		if (MUtil.equals(before, prefix)) return false;
		team.setPrefix(prefix);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > SUFFIX
	// -------------------------------------------- //
	
	public static String getTeamSuffix(Team team)
	{
		return team.getSuffix();
	}
	
	public static boolean setTeamSuffix(Team team, String suffix)
	{
		if (suffix == null) return false;
		String before = getTeamSuffix(team);
		if (MUtil.equals(before, suffix)) return false;
		team.setSuffix(suffix);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > COLOR
	// -------------------------------------------- //
	// SINCE: Minecraft 1.9
	// NOTE: We use reflected NMS implementation since Spigot does not have an implementation yet.
	
	public static ChatColor getTeamColor(Team team)
	{
		return NmsBoard.get().getColor(team);
	}
	
	public static boolean setTeamColor(Team team, ChatColor color)
	{
		if (color == null) return false;
		ChatColor before = getTeamColor(team);
		if (MUtil.equals(before, color)) return false;
		NmsBoard.get().setColor(team, color);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > FRIENDLY FIRE ENABLED
	// -------------------------------------------- //
	
	public static boolean isTeamFriendlyFireEnabled(Team team)
	{
		return team.allowFriendlyFire();
	}
	
	public static boolean setTeamFriendlyFireEnabled(Team team, Boolean friendlyFireEnabled)
	{
		if (friendlyFireEnabled == null) return false;
		boolean before = isTeamFriendlyFireEnabled(team);
		if (MUtil.equals(before, friendlyFireEnabled)) return false;
		team.setAllowFriendlyFire(friendlyFireEnabled);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > FRIENDLY TRUESIGHT ENABLED
	// -------------------------------------------- //
	
	public static boolean isTeamFriendlyTruesightEnabled(Team team)
	{
		return team.canSeeFriendlyInvisibles();
	}
	
	public static boolean setTeamFriendlyTruesightEnabled(Team team, Boolean friendlyTruesightEnabled)
	{
		if (friendlyTruesightEnabled == null) return false;
		boolean before = isTeamFriendlyTruesightEnabled(team);
		if (MUtil.equals(before, friendlyTruesightEnabled)) return false;
		team.setCanSeeFriendlyInvisibles(friendlyTruesightEnabled);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > OPTION
	// -------------------------------------------- //
	
	public static OptionStatus getTeamOption(Team team, Option option)
	{
		return team.getOption(option);
	}
	
	public static boolean setTeamOption(Team team, Option option, OptionStatus status)
	{
		if (status == null) return false;
		OptionStatus before = getTeamOption(team, option);
		if (before == status) return false;
		team.setOption(option, status);
		return true;
	}
	
	// -------------------------------------------- //
	// TEAM > OPTIONS
	// -------------------------------------------- //
	
	public static Map<Option, OptionStatus> getTeamOptions(Team team)
	{
		// Create
		Map<Option, OptionStatus> ret = new MassiveMap<>();
		
		// Fill
		for (Option option : Option.values())
		{
			OptionStatus status = getTeamOption(team, option);
			ret.put(option, status);
		}
		
		// Return
		return ret;
	}
	
	public static boolean setTeamOptions(Team team, Map<Option, OptionStatus> options)
	{
		if (options == null) return false;
		
		boolean ret = false;
		for (Entry<Option, OptionStatus> entry : options.entrySet())
		{
			Option option = entry.getKey();
			OptionStatus status = entry.getValue();
			ret |= setTeamOption(team, option, status);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// TEAM > MEMBERS
	// -------------------------------------------- //
	
	public static boolean addTeamMember(Team team, Object key)
	{
		if (isTeamMember(team, key)) return false;
		team.addEntry(getKey(key));
		return true;
	}
	
	public static boolean removeTeamMember(Team team, Object key)
	{
		if ( ! isTeamMember(team, key)) return false;
		team.removeEntry(getKey(key));
		return true;
	}
	
	public static boolean isTeamMember(Team team, Object key)
	{
		return team.hasEntry(getKey(key));
	}
	
	public static Set<String> getTeamMembers(Team team)
	{
		return team.getEntries();
	}
	
	public static boolean setTeamMembers(Team team, Set<String> members)
	{
		if (members == null) return false;
		Set<String> befores = getTeamMembers(team);
		boolean ret = false;
		
		// Add
		for (String member : members)
		{
			if (befores.contains(member)) continue;
			team.addEntry(member);
			ret = true;
		}
		
		// Remove
		for (String before : befores)
		{
			if (members.contains(before)) continue;
			team.removeEntry(before);
			ret = true;
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// KEY TEAM
	// -------------------------------------------- //
	// Treating the team like a property of the key.
	// Get and set the team for the key.
	
	public static Team getKeyTeam(Scoreboard board, Object key)
	{
		return board.getEntryTeam(getKey(key));
	}
	
	public static void setKeyTeam(Scoreboard board, Object key, Team team)
	{
		Team before = getKeyTeam(board, key);
		if (MUtil.equals(before, team)) return;
		// TODO: Do we really need to remove from the old team first?
		// TODO: Chances are this would be done automatically.
		if (before != null) removeTeamMember(before, key);
		if (team != null) addTeamMember(team, key);
	}
	
	// -------------------------------------------- //
	// PERSONAL TEAM
	// -------------------------------------------- //
	// The id is the player name.
	
	private static final Boolean PERSONAL_DEFAULT_PERSISTENT = false;
	private static final String PERSONAL_DEFAULT_NAME = null;
	private static final String PERSONAL_DEFAULT_PREFIX = "";
	private static final String PERSONAL_DEFAULT_SUFFIX = ChatColor.RESET.toString();
	private static final ChatColor PERSONAL_DEFAULT_COLOR = ChatColor.RESET;
	private static final Boolean PERSONAL_DEFAULT_FRIENDLY_FIRE_ENABLED = true;
	private static final Boolean PERSONAL_DEFAULT_FRIENDLY_TRUESIGHT_ENABLED = false;
	private static final Map<Option, OptionStatus> PERSONAL_DEFAULT_OPTIONS = new MassiveMap<>(
		Option.COLLISION_RULE, OptionStatus.ALWAYS,
		Option.DEATH_MESSAGE_VISIBILITY, OptionStatus.ALWAYS,
		Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS
	);
	
	public static boolean isPersonalTeam(Scoreboard board, Object key)
	{
		Team team = getKeyTeam(board, key);
		return isPersonalTeam(team, key);
	}
	
	public static boolean isPersonalTeam(Team team, Object key)
	{
		if (team == null) return false;
		String id = getTeamId(team);
		return id.equals(getKey(key));
	}
	
	public static Team createPersonalTeam(Scoreboard board, Object key)
	{
		// Create
		String id = getKey(key);
		Team team = createTeam(board, id);
		
		// Fill
		Boolean persistent = PERSONAL_DEFAULT_PERSISTENT;
		String name = PERSONAL_DEFAULT_NAME;
		String prefix = PERSONAL_DEFAULT_PREFIX;
		String suffix = PERSONAL_DEFAULT_SUFFIX;
		ChatColor color = PERSONAL_DEFAULT_COLOR;
		Boolean friendlyFireEnabled = PERSONAL_DEFAULT_FRIENDLY_FIRE_ENABLED;
		Boolean friendlyTruesightEnabled = PERSONAL_DEFAULT_FRIENDLY_TRUESIGHT_ENABLED;
		Map<Option, OptionStatus> options = PERSONAL_DEFAULT_OPTIONS;
		Set<String> members = Collections.singleton(id);
		
		setTeam(team, persistent, name, prefix, suffix, color, friendlyFireEnabled, friendlyTruesightEnabled, options, members);
		
		// Return
		return team;
	}
	
	public static Team getPersonalTeam(Scoreboard board, Object key, boolean creative)
	{
		String id = getKey(key);
		Team team = getTeam(board, id, false);
		if (team == null)
		{
			if (creative)
			{
				team = createPersonalTeam(board, key);
			}
		}
		else
		{
			addTeamMember(team, key);			
		}
		
		return team;
	}
	
	public static void deletePersonalTeam(Scoreboard board, Object key)
	{
		Team team = getPersonalTeam(board, key, false);
		if ( ! isPersonalTeam(team, key)) return;
		deleteTeam(team);
	}
	
}
