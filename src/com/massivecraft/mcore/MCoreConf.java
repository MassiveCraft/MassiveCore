package com.massivecraft.mcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.PermUtil;
import com.massivecraft.mcore.xlib.mongodb.WriteConcern;

public class MCoreConf extends Entity<MCoreConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MCoreConf i;
	public static MCoreConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public List<String> aliasesOuterMCore = MUtil.list("mcore");
	
	public List<String> aliasesOuterMCoreUsys = MUtil.list("usys");
	
	public List<String> aliasesOuterMCoreMStore = MUtil.list("mstore");
	
	public boolean usingRecipientChatEvent = true;
	
	public boolean forcingOnePlayerNameCase = true;
	
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
	
	public Map<String, Integer> permissionToTpdelay = MUtil.map(
		"mcore.notpdelay", 0,
		"default", 10
	);
	
	public int getTpdelay(Permissible permissible)
	{
		Integer ret = PermUtil.pickFirstVal(permissible, permissionToTpdelay);
		if (ret == null) ret = 0;
		return ret;
	}
	
	public List<String> deleteFiles = new ArrayList<String>();
	
	// Used in the MongoDB mstore driver.
	public boolean catchingMongoDbErrorsOnSave = true;
	
	public boolean catchingMongoDbErrorsOnDelete = true;
	
	public static WriteConcern getMongoDbWriteConcern(boolean catchingErrors)
	{
		return catchingErrors ? WriteConcern.ACKNOWLEDGED : WriteConcern.ERRORS_IGNORED;
	}
	public WriteConcern getMongoDbWriteConcernSave()
	{
		return getMongoDbWriteConcern(this.catchingMongoDbErrorsOnSave);
	}
	public WriteConcern getMongoDbWriteConcernDelete()
	{
		return getMongoDbWriteConcern(this.catchingMongoDbErrorsOnDelete);
	}
	
	public String variableBook = "***book***";
	public boolean usingVariableBook = true;
	
}