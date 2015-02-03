package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.List;

public class AMain
{
	// This class is temporarily here for testing.
	public static void main(String[] args)
	{
		try
		{
			System.out.println("start");
			List<String> input = new ArrayList<String>();
		
			input.add("1");
		
			for(String str : ARAbstract.prepareForSpaces(input))
				System.out.println("\"" + str + "\"");
			System.out.println("end");
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

}
