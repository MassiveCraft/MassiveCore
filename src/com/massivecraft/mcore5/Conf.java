package com.massivecraft.mcore5;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.massivecraft.mcore5.cmd.CmdMcore;
import com.massivecraft.mcore5.usys.cmd.CmdUsys;
import com.massivecraft.mcore5.util.MUtil;

public class Conf extends SimpleConfig
{
	// -------------------------------------------- //
	// CONTENT
	// -------------------------------------------- //
	
	public static String dburi = "gson://./mstore";
	public static String serverid = UUID.randomUUID().toString();
	public static Map<String, List<String>> cmdaliases = MUtil.map(
		CmdUsys.USYS, MUtil.list(CmdUsys.USYS),
		CmdMcore.MCORE, MUtil.list(CmdMcore.MCORE)
	);
	
	public static List<String> getCmdAliases(String name)
	{
		List<String> ret = cmdaliases.get(name);
		if (ret == null)
		{
			ret = MUtil.list(name);
			cmdaliases.put(name, ret);
			i.save();
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static transient Conf i = new Conf();
	private Conf()
	{
		super(MCore.p, new File("plugins/mcore/conf.json"));
	}
}
