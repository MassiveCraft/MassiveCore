package com.massivecraft.massivecore.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;

class SenderMapTest
{

	static SenderMap map = new SenderMap();
	public static void main(String[] args) 
	{
		map.addValue("Madus", SenderPresence.LOCAL); // Satisfying
		//printMap();
		
		map.removeValueCompletely("Madus"); // Satisfying
		//printMap();
		
		map.addValue("Madus", SenderPresence.LOCAL); // Satisfying
		map.addValue("Cayorion", SenderPresence.ONLINE); // Satisfying
		//printMap();
		
		map.removeValueCompletely("madus"); // Satisfying (case insensitive)
		//printMap();
		
		map.addValue("@console", SenderPresence.OFFLINE); // Satisfying
		//printMap();
		
		System.out.println(map.getPresence("@console")); // Satisfying (OFFLINE)
		System.out.println(map.getPresence("caYorioN")); // Satisfying (ONLINE)
		System.out.println(map.getPresence("MaDus")); // Satisfying (null)
	}
	
	public static void printMap()
	{
		printMap(map);
	}
	
	public static void printMap(SenderMap map)
	{
		for (Entry<SenderPresence, Map<SenderType, Set<String>>> entry1 : map.getInnerMap().entrySet())
		{
			System.out.println(entry1.getKey());
			for (Entry<SenderType, Set<String>> entry2 : entry1.getValue().entrySet())
			{
				System.out.println("	" + entry2.getKey() + ": " + entry2.getValue());
			}
		}
	}

}
