package com.massivecraft.core.persist;

import java.util.HashMap;
import java.util.Map;

public class Persist
{
	private static Map<Object, PersistRealm> realms = new HashMap<Object, PersistRealm>();
	public static Map<Object, PersistRealm> getRealms() { return realms; }
	public static PersistRealm getRealm(Object realmOwner) { return realms.get(realmOwner); }
	public static void createRealm(Object realmOwner)
	{
		if (realms.containsKey(realmOwner)) return;
		realms.put(realmOwner, new PersistRealm());
	}
	public static void removeRealm(Object realmOwner) { realms.remove(realmOwner); }
}
