package com.massivecraft.mcore;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.massivecraft.mcore.util.MUtil;


public class ConfServer extends SimpleConfig
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ConfServer i = new ConfServer();
	public static ConfServer get() { return i; }
	public ConfServer() { super(MCore.get()); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static String serverid = UUID.randomUUID().toString();
	
	public static Map<String, String> alias2uri = MUtil.map(
		"default", "flatfile",
	    "flatfile", "flatfile://mstore",
	    "mongodb", "mongodb://localhost:27017/mstore"
	);
	
	public static String dburi = "default";
	
	public static List<String> aliasesOuterMCore = MUtil.list("mcore");
	public static List<String> aliasesOuterMCoreUsys = MUtil.list("usys");
	public static List<String> aliasesOuterMCoreMStore = MUtil.list("mstore");

}
