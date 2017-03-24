package com.massivecraft.massivecore.item;

public class ConverterDefault<X, Y> extends Converter<X, Y>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterDefault<Object, Object> i = new ConverterDefault<>();
	@SuppressWarnings("unchecked")
	public static <X, Y> ConverterDefault<X, Y> get() { return (ConverterDefault<X, Y>) i; }
	@SuppressWarnings("unchecked")
	public static <X, Y> ConverterDefault<X, Y> get(Class<X> cx, Class<Y> cy) { return (ConverterDefault<X, Y>) i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public Y convert(X x)
	{
		return (Y)x;
	}

}
