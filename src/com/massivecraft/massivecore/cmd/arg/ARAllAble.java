package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

public interface ARAllAble<T> extends AR<T>
{
	public Collection<T> getAll();
}
