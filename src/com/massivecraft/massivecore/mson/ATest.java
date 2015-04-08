package com.massivecraft.massivecore.mson;

import static com.massivecraft.massivecore.mson.Mson.mson;

import java.util.List;

import org.bukkit.ChatColor;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;

public class ATest
{
	public static void main(String[] args)
	{
		try
		{
			// Test getMsonFrom
			Mson ofMson = mson(new Mson().text("hello"));
			System.out.println("ofMson:" + ofMson); // Success
			
			Mson ofString = mson("hello");
			System.out.println("ofString:" + ofString); // Success
			
			Mson ofCollection = mson(new MassiveList<String>("hello ", "you!"));
			System.out.println("ofCollection:" + ofCollection); // Success
			
			// Test children
			Mson child = mson("test").color(ChatColor.BLUE)
					.addChild(" test2").link("www.massivecraft.com")
					.addChild(" test3 ")
					.addChildren("this ", "is only ", "one way to do it!")
					.root()
					.tooltip("Holy moly!");
			System.out.println("child:" + child.root()); // Success
			
			// Test siblings
			Mson sibling = mson(
					"test",
					" test2",
					" test3",
					new Mson().text(" Test4, children: ").addChild("Child1 ")
					.addSiblings("Sibling 1 ", "Sibling 2 ", "Sibling 3 ")
					.addSibling("Sibling 4").root()
					).tooltip("Holy moly!");
			System.out.println("sibling:" + sibling.root()); // Success
			
			// Test fromParsedMessage
			Mson parsed = Mson.fromParsedMessage(Txt.parse("white <i>in<em>fo <b><em><bold>bad <lime>green"));
			System.out.println("parsed:" + parsed); // Success
			
			Mson parsed2 = Mson.parse("white <i>in<em>fo <b><bold>bad <lime>green");
			System.out.println("parsed2:" + parsed2.toRaw()); // Success
			
			Mson parseFormat = Mson.parse("<i>insert here %s whatever <g> you <b>%s <g>could wish for!", "!1337!", "herpi derp");
			System.out.println("parseFormat:" + parseFormat); // Success
			
			// Test format
			Mson format = Mson.format("Just a %s simple string! :)", "very");
			System.out.println("format:" + format); // Success
			
			// Test split
			List<Mson> split = format.split(Txt.REGEX_WHITESPACE.toString());
			System.out.println("split:" + mson(split)); // Success
			
			/*
			 * Test replace
			 */
			
			Mson charr = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replace('1', '0');
			System.out.println("char:" + charr); // Success
			
			Mson sequence = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replace("1", "0");
			System.out.println("sequence:" + sequence); // Success
		
			Mson regex = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replaceAll("1", "0");
			System.out.println("regex:" + regex); // Success
			
			//Mson replaceFirst = Mson.of("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").getRoot().replaceFirst("1", "0");
			//System.out.println("replaceFirst:" + replaceFirst.toRaw()); // Success
			
			/*
			 * Test special replaceAll
			 */
			
			// replace string mson
			Mson replaceAll1 = mson("1 2 3 4 5 6 1 7 tests").color(ChatColor.BLUE).addChild(" 1+1+1").addChild("herpiderp").root().replaceAll("1", mson("0"));
			System.out.println("replaceAll1:" + replaceAll1.root()); // Success

			Mson overload = mson("hello").addChild("hello").addChild("hello").root().replaceAll("hello", mson("lol"));
			System.out.println("overload:" + overload.root()); // Success
			
			Mson toReplace = new Mson().text("hallo");
			
			// replace mson mson
			Mson replaceAll2 = mson("1 2 3 4 5 6 7 tests").addChild(toReplace).addSibling(" miep").root().replaceAll(toReplace, mson("tests"));
			System.out.println("replaceAll2:" + replaceAll2.root()); // Success
			
			Mson overload2 = mson("1 2 3 4 5 6 7 tests").addChild("hallo").addChild("hallo").addChild("hallo").addSibling(" miep").root().replaceAll(mson("hallo"), mson("tests"));
			System.out.println("overload2:" + overload2.root()); // Success
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

}
