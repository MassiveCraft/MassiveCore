package com.massivecraft.mcore5.cmd.arg;

import com.massivecraft.mcore5.cmd.MCommand;

public interface ArgReader<T>
{
	public ArgResult<T> read(String str, MCommand mcommand);
}