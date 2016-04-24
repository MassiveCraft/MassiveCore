package com.massivecraft.massivecore.item;

public abstract class Converter<X, Y>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Y convert(X x);

}
