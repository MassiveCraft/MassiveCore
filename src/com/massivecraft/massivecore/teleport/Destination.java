package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.ps.PS;

import java.io.Serializable;

public interface Destination extends Serializable
{
	public PS getPs(Object watcherObject) throws MassiveException;
	public boolean hasPs();
	public String getMessagePsNull(Object watcherObject);
	
	public String getDesc(Object watcherObject);
	public void setDesc(String desc);
}
