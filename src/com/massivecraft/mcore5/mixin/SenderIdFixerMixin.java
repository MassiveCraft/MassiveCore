package com.massivecraft.mcore5.mixin;

public interface SenderIdFixerMixin
{
	public boolean canFix(String name);
	public String tryFix(String name);
	public String reqFix(String name);
}
