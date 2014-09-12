package com.massivecraft.massivecore.ps;

import com.massivecraft.massivecore.util.Txt;

public class PSFormatSlug extends PSFormatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PSFormatSlug i = new PSFormatSlug();
	public static PSFormatSlug get()	{ return i; }
	private PSFormatSlug()
	{
		super(
			Txt.parse("<silver><em>NULL"),
			"",
			false,
			true,
			Txt.parse("<k>") + PS.NAME_SERIALIZED_WORLD + Txt.parse("<v>") + "%s",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_BLOCKX + Txt.parse("<v>") + "%d",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_BLOCKY + Txt.parse("<v>") + "%d",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_BLOCKZ + Txt.parse("<v>") + "%d",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_LOCATIONX + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_LOCATIONY + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_LOCATIONZ + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_CHUNKX + Txt.parse("<v>") + "%d",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_CHUNKZ + Txt.parse("<v>") + "%d",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_PITCH + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_YAW + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_VELOCITYX + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_VELOCITYY + Txt.parse("<v>") + "%.2f",
			Txt.parse("<k>") + PS.NAME_SERIALIZED_VELOCITYZ + Txt.parse("<v>") + "%.2f",
			"",
			""
		);
	}
}
