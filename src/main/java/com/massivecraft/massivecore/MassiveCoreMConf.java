package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.xlib.mongodb.WriteConcern;

public class MassiveCoreMConf extends Entity<MassiveCoreMConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MassiveCoreMConf i;
	public static MassiveCoreMConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public List<String> aliasesOuterMassiveCore = MUtil.list("massivecore", "mcore");
	
	public List<String> aliasesOuterMassiveCoreUsys = MUtil.list("usys");
	
	public List<String> aliasesOuterMassiveCoreStore = MUtil.list("massivestore", "mstore");
	
	public List<String> aliasesOuterMassiveCoreBuffer = MUtil.list("buffer");
	
	public List<String> aliasesOuterMassiveCoreCmdurl = MUtil.list("cmdurl");
	
	public boolean usingRecipientChatEvent = true;
	
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
		"massivecore.notpdelay", 0,
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
		return catchingErrors ? WriteConcern.ACKNOWLEDGED : WriteConcern.UNACKNOWLEDGED;
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
	
	public String variableBuffer = "***buffer***";
	public boolean usingVariableBuffer = true;
	
}