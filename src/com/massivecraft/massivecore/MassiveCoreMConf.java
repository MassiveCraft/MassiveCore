package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorNullable;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.xlib.mongodb.WriteConcern;

@EditorName("config")
public class MassiveCoreMConf extends Entity<MassiveCoreMConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MassiveCoreMConf i;
	public static MassiveCoreMConf get() { return i; }
	
	// -------------------------------------------- //
	// ALIASES
	// -------------------------------------------- //
	// Base command aliases.
	
	@EditorNullable(false)
	public List<String> aliasesMcore = MUtil.list("massivecore", "mcore");
	@EditorNullable(false)
	public List<String> aliasesUsys = MUtil.list("usys");
	@EditorNullable(false)
	public List<String> aliasesMstore = MUtil.list("massivestore", "mstore");
	@EditorNullable(false)
	public List<String> aliasesBuffer = MUtil.list("buffer");
	@EditorNullable(false)
	public List<String> aliasesCmdurl = MUtil.list("cmdurl");
	
	// -------------------------------------------- //
	// GENERAL
	// -------------------------------------------- //
	// General configuration options.
	
	public String taskServerId = null;
	public boolean versionSynchronizationEnabled = true;
	public int tabCompletionLimit = 100;
	public boolean recipientChatEventEnabled = true;
	
	// -------------------------------------------- //
	// PERMISSIONS FORMATS
	// -------------------------------------------- //
	// Permission denied formatting.
	
	@EditorNullable(false)
	public Map<String, String> permissionDeniedFormats = MUtil.map(
		"some.awesome.permission.node", "<b>You must be awesome to %s<b>.",
		"some.derp.permission.node.1", "derp",
		"some.derp.permission.node.2", "derp",
		"some.derp.permission.node.3", "derp",
		"derp", "<b>Only derp people can %s<b>.\n<i>Ask a moderator to become derp."
	);
	
	public String getPermissionDeniedFormat(String permissionName)
	{
		Map<String, String> map = this.permissionDeniedFormats;
		String ret = map.get(permissionName);
		if (ret == null) return null;
		ret = MUtil.recurseResolveMap(ret, map);
		return ret;
	}
	
	// -------------------------------------------- //
	// TP DELAY
	// -------------------------------------------- //
	// Teleportation delay permissions.
	
	@EditorNullable(false)
	public Map<String, Integer> permissionToTpdelay = MUtil.map(
		"massivecore.notpdelay", 0,
		"default", 10
	);
	
	public int getTpdelay(Permissible permissible)
	{
		Integer ret = PermUtil.pickFirstVal(permissible, permissionToTpdelay);
		if (ret == null) ret = 0;
		return ret;
	}
	
	// -------------------------------------------- //
	// DELETE FILES
	// -------------------------------------------- //
	// Delete certain files for system cleanliness.
	
	public List<String> deleteFiles = new ArrayList<String>();
	
	// -------------------------------------------- //
	// VARIABLES
	// -------------------------------------------- //
	// Chat and command variables.
	
	public String variableBookName = "***book***";
	public boolean variableBookEnabled = true;
	
	public String variableBufferName = "***buffer***";
	public boolean variableBufferEnabled = true;
	
	// -------------------------------------------- //
	// CLICK
	// -------------------------------------------- //
	// Button click sound configuration.
	
	public SoundEffect clickSound = SoundEffect.valueOf("UI_BUTTON_CLICK", 0.75f, 1.0f);
	
	// -------------------------------------------- //
	// MSTORE
	// -------------------------------------------- //
	// The database system.
	
	public volatile long millisBetweenLocalPoll = TimeUnit.MILLIS_PER_MINUTE * 5;
	public volatile long millisBetweenRemotePollWithoutPusher = TimeUnit.MILLIS_PER_SECOND * 10;
	public volatile long millisBetweenRemotePollWithPusher = TimeUnit.MILLIS_PER_MINUTE * 1;
	
	@EditorType(fieldName = "iOn")
	public boolean warnOnLocalAlter = false;
	
	// -------------------------------------------- //
	// MONGODB
	// -------------------------------------------- //
	// The database system MongoDB driver.
	
	public boolean catchingMongoDbErrorsOnSave = true;
	public boolean catchingMongoDbErrorsOnDelete = true;
	
	public static WriteConcern getMongoDbWriteConcern(boolean catchingErrors) { return catchingErrors ? WriteConcern.ACKNOWLEDGED : WriteConcern.UNACKNOWLEDGED; }
	public WriteConcern getMongoDbWriteConcernSave() { return getMongoDbWriteConcern(this.catchingMongoDbErrorsOnSave); }
	public WriteConcern getMongoDbWriteConcernDelete() { return getMongoDbWriteConcern(this.catchingMongoDbErrorsOnDelete); }
	
	// -------------------------------------------- //
	// DEBUG
	// -------------------------------------------- //
	
	public boolean debugWriters = false;
	
	// -------------------------------------------- //
	// SPONSOR
	// -------------------------------------------- //
	// URL connections to http://sponsorinfo.massivecraft.com/
	
	public long sponsorUpdateMillis = 0;
	public boolean sponsorEnabled = true;
	
	// -------------------------------------------- //
	// MCSTATS
	// -------------------------------------------- //
	// URL connections to http://mcstats.org/
	
	public boolean mcstatsEnabled = true;

}
