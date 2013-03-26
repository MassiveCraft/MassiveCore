package com.massivecraft.mcore;

import java.util.LinkedHashMap;
import java.util.Map;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;

public class MCoreConf extends Entity<MCoreConf, String>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MCoreConf get()
	{
		return MCoreConfColl.get().get(MCore.INSTANCE);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public MCoreConf load(MCoreConf that)
	{
		this.permissionDeniedFormats = that.permissionDeniedFormats;
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	// These getters and setters are obnoxious, defensive copying, NPE avoiding and probably thread safe.
	
	private Map<String, String> permissionDeniedFormats = MUtil.map(
		"some.awesome.permission.node", "<b>You must be awesome to %s<b>.",
		"some.derp.permission.node", "<b>Only derp people can %s<b>.\n<i>Ask a moderator to become derp."
	);
	public Map<String, String> getPermissionDeniedFormats() { return this.permissionDeniedFormats == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<String, String>(this.permissionDeniedFormats); }
	public void setPermissionDeniedFormats(Map<String, String> permissionDeniedFormats) { this.permissionDeniedFormats = permissionDeniedFormats == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<String, String>(permissionDeniedFormats); this.changed(); }
	
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
		return this.getPermissionDeniedFormats().get(permissionName);
	}
	

}