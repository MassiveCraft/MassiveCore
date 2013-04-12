package com.massivecraft.mcore;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.PermUtil;

public class MCoreConf extends Entity<MCoreConf>
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
	
	private Map<String, Integer> permissionToTpdelay = MUtil.map(
		"mcore.notpdelay", 0,
		"default", 10
	);
	public Map<String, Integer> getPermissionToTpdelay() { return this.permissionToTpdelay == null ? new LinkedHashMap<String, Integer>() : new LinkedHashMap<String, Integer>(this.permissionToTpdelay); }
	public void setPermissionToTpdelay(Map<String, Integer> permissionToTpdelay) { this.permissionToTpdelay = permissionToTpdelay == null ? new LinkedHashMap<String, Integer>() : new LinkedHashMap<String, Integer>(permissionToTpdelay); this.changed(); }
	
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
	
	public int getTpdelay(Permissible permissible)
	{
		Integer ret = PermUtil.pickFirstVal(permissible, this.getPermissionToTpdelay());
		if (ret == null) ret = 0;
		return ret;
	}
	
}