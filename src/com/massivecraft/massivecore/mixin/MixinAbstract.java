package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class MixinAbstract extends Engine
{
	// -------------------------------------------- //
	// STATIC REFLECTION
	// -------------------------------------------- //
	
	private Class<?> clazz = ReflectionUtil.getSuperclassDeclaringField(this.getClass(), true, "d");
	public MixinAbstract getDefault() { return ReflectionUtil.getField(this.clazz, "d", null); }
	public MixinAbstract getInstance() { return ReflectionUtil.getField(this.clazz, "i", null); }
	public void setInstance(MixinAbstract i) { ReflectionUtil.setField(this.clazz, "i", null, i); }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public boolean isActive()
	{
		return this.getInstance() == this;
	}
	
	@Override
	public void setActive(boolean active)
	{
		this.setActiveMixin(active);
		super.setActive(active);
	}
	
	public void setActiveMixin(boolean active)
	{
		this.setInstance(active ? this : this.getDefault());
	}

}
