package com.massivecraft.core.persist;

public interface Predictate<T>
{
	public boolean apply(T type);
}