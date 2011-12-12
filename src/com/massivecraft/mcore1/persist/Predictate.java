package com.massivecraft.mcore1.persist;

public interface Predictate<T>
{
	public boolean apply(T type);
}