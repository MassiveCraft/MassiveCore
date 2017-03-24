package com.massivecraft.massivecore.pager;

public interface Stringifier<T>
{
	String toString(T item, int index);
}
