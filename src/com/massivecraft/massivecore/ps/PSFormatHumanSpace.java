package com.massivecraft.massivecore.ps;

import com.massivecraft.massivecore.util.Txt;

public class PSFormatHumanSpace extends PSFormatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PSFormatHumanSpace i = new PSFormatHumanSpace();
	public static PSFormatHumanSpace get()	{ return i; }
	private PSFormatHumanSpace()
	{
		super(
			Txt.parse("<silver><em>NULL"),
			Txt.parse(""),
			true,
			false,
			Txt.parse("<v>%s"),
			Txt.parse("<v>%d"),
			Txt.parse("<v>%d"),
			Txt.parse("<v>%d"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%d"),
			Txt.parse("<v>%d"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse("<v>%.2f"),
			Txt.parse(" "),
			Txt.parse("")
		);
	}
}
