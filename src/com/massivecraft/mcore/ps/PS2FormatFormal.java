package com.massivecraft.mcore.ps;

public class PS2FormatFormal extends PS2FormatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PS2FormatFormal i = new PS2FormatFormal();
	public static PS2FormatFormal get()	{ return i; }
	private PS2FormatFormal()
	{
		super(
			"PS{NULL}",
			"PS{",
			PS2.NAME_SERIALIZED_WORLD + ": %s",
			PS2.NAME_SERIALIZED_BLOCKX + ": %d",
			PS2.NAME_SERIALIZED_BLOCKY + ": %d",
			PS2.NAME_SERIALIZED_BLOCKZ + ": %d",
			PS2.NAME_SERIALIZED_LOCATIONX + ": %.2f",
			PS2.NAME_SERIALIZED_LOCATIONY + ": %.2f",
			PS2.NAME_SERIALIZED_LOCATIONZ + ": %.2f",
			PS2.NAME_SERIALIZED_CHUNKX + ": %d",
			PS2.NAME_SERIALIZED_CHUNKZ + ": %d",
			PS2.NAME_SERIALIZED_PITCH + ": %.2f",
			PS2.NAME_SERIALIZED_YAW + ": %.2f",
			PS2.NAME_SERIALIZED_VELOCITYX + ": %.2f",
			PS2.NAME_SERIALIZED_VELOCITYY + ": %.2f",
			PS2.NAME_SERIALIZED_VELOCITYZ + ": %.2f",
			", ",
			"}"
		);
	}
}
