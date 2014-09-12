package com.massivecraft.massivecore.ps;

import com.massivecraft.massivecore.util.Txt;

public class PSFormatHumanComma extends PSFormatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PSFormatHumanComma i = new PSFormatHumanComma();
	public static PSFormatHumanComma get()	{ return i; }
	private PSFormatHumanComma()
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
			Txt.parse("<i>, "),
			Txt.parse("")
		);
	}
}
