package com.massivecraft.mcore.adapter;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_4_R1.NBTBase;

import lombok.Getter;

public enum NBType
{
	// -------------------------------------------- //
	// VALUES
	// -------------------------------------------- //
	
	END(0, "end"),
	BYTE(1, "byte"),
	SHORT(2, "short"),
	INT(3, "int"),
	LONG(4, "long"),
	FLOAT(5, "float"),
	DOUBLE(6, "double"),
	BYTEARRAY(7, "bytearray"),
	STRING(8, "string"),
	LIST(9, "list"),
	COMPOUND(10, "compound"),
	INTARRAY(11, "intarray"),
	UNKNOWN(-1, "unknown"),
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@Getter final byte id;
	@Getter final String name;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private NBType(int id, String name)
	{
		this.id = (byte)id;
		this.name = name;
	}
	
	// -------------------------------------------- //
	// STATIC UTILS
	// -------------------------------------------- //
	
	protected final static transient Map<String, NBType> tagnameToEnum = new HashMap<String, NBType>();
	protected final static transient Map<Byte, NBType> byteToEnum = new HashMap<Byte, NBType>();
	
	static
	{
		for (NBType value : values())
		{
			tagnameToEnum.put(value.getName(), value);
			byteToEnum.put(value.getId(), value);
		}
	}
	
	public static NBType getByName(String name)
	{
		NBType ret = tagnameToEnum.get(name);
		if (ret == null)
		{
			ret = UNKNOWN;
		}
		return ret; 
	}
	
	public static NBType getById(byte id)
	{
		NBType ret = byteToEnum.get(id);
		if (ret == null)
		{
			ret = UNKNOWN;
		}
		return ret; 
	}
	
	public static NBType getByTag(NBTBase tag)
	{
		NBType ret = byteToEnum.get(tag.getTypeId());
		if (ret == null)
		{
			ret = UNKNOWN;
		}
		return ret; 
	}
}
