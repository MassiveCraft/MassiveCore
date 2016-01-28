package com.massivecraft.massivecore.ps;

public class PSFormatArgument extends PSFormatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PSFormatArgument i = new PSFormatArgument();
	public static PSFormatArgument get()	{ return i; }
	private PSFormatArgument()
	{
		super(
			"null",
			"",
			false,
			false,
			PS.NAME_SERIALIZED_WORLD + ":%s",
			PS.NAME_SERIALIZED_BLOCKX + ":%+d",
			PS.NAME_SERIALIZED_BLOCKY + ":%+d",
			PS.NAME_SERIALIZED_BLOCKZ + ":%+d",
			PS.NAME_SERIALIZED_LOCATIONX + ":%+.2f",
			PS.NAME_SERIALIZED_LOCATIONY + ":%+.2f",
			PS.NAME_SERIALIZED_LOCATIONZ + ":%+.2f",
			PS.NAME_SERIALIZED_CHUNKX + ":%+d",
			PS.NAME_SERIALIZED_CHUNKZ + ":%+d",
			PS.NAME_SERIALIZED_PITCH + ":%.2f",
			PS.NAME_SERIALIZED_YAW + ": %.2f",
			PS.NAME_SERIALIZED_VELOCITYX + ":%+.2f",
			PS.NAME_SERIALIZED_VELOCITYY + ":%+.2f",
			PS.NAME_SERIALIZED_VELOCITYZ + ":%+.2f",
			" ",
			""
		);
	}
}
