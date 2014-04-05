package com.massivecraft.mcore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
	
	// These getters and setters are obnoxious, defensive copying, NPE avoiding and probably thread safe.
	
	private boolean usingRecipientChatEvent = true;
	public boolean isUsingRecipientChatEvent() { return this.usingRecipientChatEvent; }
	public void setUsingRecipientChatEvent(boolean usingRecipientChatEvent) { this.usingRecipientChatEvent = usingRecipientChatEvent; this.changed(); }
	
	private boolean forcingOnePlayerNameCase = true;
	public boolean isForcingOnePlayerNameCase() { return this.forcingOnePlayerNameCase; }
	public void setForcingOnePlayerNameCase(boolean forcingOnePlayerNameCase) { this.forcingOnePlayerNameCase = forcingOnePlayerNameCase; this.changed(); }
	
	private Map<String, String> permissionDeniedFormats = MUtil.map(
		"some.awesome.permission.node", "<b>You must be awesome to %s<b>.",
		"some.derp.permission.node.1", "derp",
		"some.derp.permission.node.2", "derp",
		"some.derp.permission.node.3", "derp",
		"derp", "<b>Only derp people can %s<b>.\n<i>Ask a moderator to become derp."
	);
	public Map<String, String> getPermissionDeniedFormats() { return this.permissionDeniedFormats == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<String, String>(this.permissionDeniedFormats); }
	public void setPermissionDeniedFormats(Map<String, String> permissionDeniedFormats) { this.permissionDeniedFormats = permissionDeniedFormats == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<String, String>(permissionDeniedFormats); this.changed(); }
	
	private Map<String, Integer> permissionToTpdelay = MUtil.map(
		"mcore.notpdelay", 0,
		"default", 10
	);
	public Map<String, Integer> getPermissionToTpdelay() { return this.permissionToTpdelay == null ? new LinkedHashMap<String, Integer>() : new LinkedHashMap<String, Integer>(this.permissionToTpdelay); }
	public void setPermissionToTpdelay(Map<String, Integer> permissionToTpdelay) { this.permissionToTpdelay = permissionToTpdelay == null ? new LinkedHashMap<String, Integer>() : new LinkedHashMap<String, Integer>(permissionToTpdelay); this.changed(); }
	
	private List<String> deleteFiles = new ArrayList<String>();
	public List<String> getDeleteFiles() { return this.deleteFiles == null ? new ArrayList<String>() : new ArrayList<String>(this.deleteFiles); }
	public void setDeleteFiles(List<String> deleteFiles) { this.deleteFiles = deleteFiles == null ? new ArrayList<String>() : new ArrayList<String>(deleteFiles); this.changed(); }
	
	// Used in the MongoDB mstore driver.
	private WriteConcern mongoDbWriteConcern = WriteConcern.ACKNOWLEDGED;
	public WriteConcern getMongoDbWriteConcern() { return this.mongoDbWriteConcern; }
	public void setMongoDbWriteConcern(WriteConcern mongoDbWriteConcern) { this.mongoDbWriteConcern = mongoDbWriteConcern; this.changed(); }
	
	// -------------------------------------------- //
	// HELP ACCESS
	// -------------------------------------------- //
	
	public String setPermissionDeniedFormat(String permissionName, String permissionDeniedFormat)
	{
		Map<String, String> temp = this.getPermissionDeniedFormats();
		String ret = temp.put(permissionName, permissionDeniedFormat);
		this.setPermissionDeniedFormats(temp);
		return ret;
	}
	
	public String removePermissionDeniedFormat(String permissionName)
	{
		Map<String, String> temp = this.getPermissionDeniedFormats();
		String ret = temp.remove(permissionName);
		this.setPermissionDeniedFormats(temp);
		return ret;
	}
	
	public String getPermissionDeniedFormat(String permissionName)
	{
		Map<String, String> map = this.getPermissionDeniedFormats();
		String ret = map.get(permissionName);
		if (ret == null) return null;
		ret = MUtil.recurseResolveMap(ret, map);
		return ret;
	}
	
	public int getTpdelay(Permissible permissible)
	{
		Integer ret = PermUtil.pickFirstVal(permissible, this.getPermissionToTpdelay());
		if (ret == null) ret = 0;
		return ret;
	}
	
}