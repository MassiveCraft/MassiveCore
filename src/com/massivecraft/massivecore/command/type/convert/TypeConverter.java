package com.massivecraft.massivecore.command.type.convert;

import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeTransformer;
import com.massivecraft.massivecore.item.Converter;
import org.bukkit.command.CommandSender;

public class TypeConverter<A, B> extends TypeTransformer<A, B> 
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Converter<A, B> a2b;
	public Converter<A, B> getA2b() { return this.a2b; }
	
	private final Converter<B, A> b2a;
	public Converter<B, A> getB2a() { return this.b2a; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public TypeConverter(Type<A> typeA, Type<B> typeB, Converter<A, B> a2b, Converter<B, A> b2a)
	{
		super(typeA, typeB);
		this.a2b = a2b;
		this.b2a = b2a;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public B innerToOuter(A inner, CommandSender sender)
	{
		return this.getA2b().convert(inner);
	}

	@Override
	public A outerToInner(B outer)
	{
		return this.getB2a().convert(outer);
	}
	
}
