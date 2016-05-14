package com.massivecraft.massivecore.picker;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.util.Txt;

public class PickerTest extends Picker
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static final PickerTest d = new PickerTest().setAlternatives(
		PickerTestImpossible.class,
		PickerTestPossible.class
	);
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final PickerTest i = d;
	public static PickerTest get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PickerTest()
	{
		
	}
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public void action()
	{
		String msg = this.getMsg();
		String message = Txt.parse(msg);
		MassiveCore.get().log(message);
	}
	
	public String getMsg()
	{
		return "<silver>Default...";
	}
	
}
