package com.massivecraft.massivecore.pager;

public interface Stringifier<T>
{
	public String toString(T item, int index);
}
