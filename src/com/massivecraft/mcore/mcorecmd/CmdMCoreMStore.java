package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCoreMStore extends MCoreCommand
{
	public CmdMCoreMStoreStats cmdMCoreMStoreStats = new CmdMCoreMStoreStats(MUtil.list("stats"));
	public CmdMCoreMStoreListcolls cmdMCoreMStoreListcolls = new CmdMCoreMStoreListcolls(MUtil.list("listcolls"));
	public CmdMCoreMStoreCopydb cmdMCoreMStoreCopydb = new CmdMCoreMStoreCopydb(MUtil.list("copydb"));
	
	public CmdMCoreMStore(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdMCoreMStoreStats);
		this.addSubCommand(this.cmdMCoreMStoreListcolls);
		this.addSubCommand(this.cmdMCoreMStoreCopydb);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}