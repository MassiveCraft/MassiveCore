package com.massivecraft.massivecore.test;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;


public abstract class Test extends Engine
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Object> issues = new MassiveList<>();
	protected void addIssue(Object issue)
	{
		this.issues.add(issue);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if (!MassiveCoreMConf.get().debugEnabled) return;
		
		this.test();
		
		if (this.issues.isEmpty()) return;
		
		Object message;
		
		message = Txt.parse("<b>Issues Detected by %s in %s", this.getClass().getSimpleName(), this.getActivePlugin().getName());
		message = Txt.titleize(message);
		MixinMessage.get().messageOne(IdUtil.CONSOLE_ID, message);
		
		for (Object issue : issues)
		{
			MixinMessage.get().messageOne(IdUtil.CONSOLE_ID, issue);
		}
		
		try
		{
			Thread.sleep(20000L);
		}
		catch (InterruptedException ignored)
		{
			
		}
	}
	
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	protected abstract void test();
}
