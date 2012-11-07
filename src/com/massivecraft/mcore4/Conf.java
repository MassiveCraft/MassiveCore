package com.massivecraft.mcore4;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.massivecraft.mcore4.usys.cmd.CmdUsys;
import com.massivecraft.mcore4.util.MUtil;

public class Conf extends SimpleConfig
{
	// -------------------------------------------- //
	// CONTENT
	// -------------------------------------------- //
	
	public static String dburi = "gson://./mstore";
	public static String serverid = UUID.randomUUID().toString();
	public static Map<String, List<String>> cmdaliases = MUtil.map(CmdUsys.USYS, MUtil.list(CmdUsys.USYS));
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static transient Conf i = new Conf();
	private Conf()
	{
		super(MCore.p, new File("plugins/mcore/conf.json"));
	}
}
