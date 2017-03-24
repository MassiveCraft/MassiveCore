package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.ps.PS;

import java.io.Serializable;

public interface Destination extends Serializable
{
	PS getPs(Object watcherObject) throws MassiveException;
	boolean hasPs();
	String getMessagePsNull(Object watcherObject);
	
	String getDesc(Object watcherObject);
	void setDesc(String desc);
}
