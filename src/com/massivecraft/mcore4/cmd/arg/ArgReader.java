package com.massivecraft.mcore4.cmd.arg;

import com.massivecraft.mcore4.cmd.MCommand;

public interface ArgReader<T>
{
	public ArgResult<T> read(String str, MCommand mcommand);
}