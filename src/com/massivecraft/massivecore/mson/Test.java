package com.massivecraft.massivecore.mson;

import static com.massivecraft.massivecore.mson.Mson.mson;
import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;

public class Test
{
	public static void main(String[] args)
	{
		try
		{
			test();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public static void test()
	{
		Mson mson;
		
		mson = mson("hello");
		test("ofString", mson);
		
		mson = mson(
			mson("hello")
		);
		test("ofMson", mson);
		
		mson = mson(
			new MassiveList<String>("hello ", "you!")
		);
		test("ofCollection", mson);
		
		mson = mson("test")
		.color(BLUE)
		.addChild(" test2").link("www.massivecraft.com")
		.addChild(" test3 ")
		.addChildren("this ", "is only ", "one way to do it!")
		.root()
		.tooltip("Holy moly!");
		test("child", mson);
		
		mson = mson(
			"test",
			" test2",
			" test3",
			mson().text(" Test4, children: ").addChild("Child1 ").addSiblings("Sibling 1 ", "Sibling 2 ", "Sibling 3 ").addSibling("Sibling 4").root()
		).tooltip("Holy moly!");
		test("sibling", mson);
		
		mson = Mson.fromParsedMessage(Txt.parse("white <i>in<em>fo <b><em><bold>bad <lime>green"));
		test("parsed", mson);
		
		mson = Mson.parse("white <i>in<em>fo <b><bold>bad <lime>green");
		test("parsed2", mson);
		
		mson = Mson.parse("<i>insert here %s whatever <g> you <b>%s <g>could wish for!", "!1337!", "herpi derp");
		test("parseFormat", mson);
		
		Mson format = Mson.format("Just a %s simple string! :)", "very");
		test("format", format);
		
		// Test split
		mson = mson(format.split(Txt.REGEX_WHITESPACE.toString()));
		test("split", mson);
		
		// -------------------------------------------- //
		// TEST REPLACE
		// -------------------------------------------- //
		
		mson = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replace('1', '0');
		test("charr", mson);
		
		mson = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replace("1", "0");
		test("sequence", mson);
	
		mson = mson("1 2 3 4 5 6 1 7 tests").addChild(" 01010101").root().replaceAll("1", "0");
		test("regex", mson);
		
		// -------------------------------------------- //
		// TEST SPECIAL REPLACE ALL
		// -------------------------------------------- //
		
		// replace string mson
		mson = mson("1 2 3 4 5 6 1 7 tests").color(BLUE).addChild(" 1+1+1").addChild("herpiderp").root().replaceAll("1", mson("0"));
		test("replaceAll1", mson);
		
		mson = mson("hellohello").addChild("hello").addChild("hello").root().replaceAll("hello", mson("lol"));
		test("overload", mson);
		
		
		Mson toReplace = mson("hallo");
		
		// replace mson mson
		mson = mson("1 2 3 4 5 6 7 tests").addChild(toReplace).addSibling(" miep").root().replaceAll(toReplace, mson("tests"));
		test("replaceAll2", mson);
		
		mson = mson("1 2 3 4 5 6 7 tests").addChild("hallo").addChild("hallo").addChild("hallo").addSibling(" miep").root().replaceAll(mson("hallo"), mson("tests"));
		test("overload2", mson);
		
		// -------------------------------------------- //
		// EXAMPLES
		// -------------------------------------------- //
		
		// Example: Would you like to [allow] or [deny]?
		mson = mson(
			"Would you like to ",
			mson("[allow]").style(RED).command("/asfd blah allow"),
			" or ",
			mson("[deny]").style(GREEN).command("/asfd blah deny"),
			"?"
		).style(YELLOW);
		test("Would you like to [allow] or [deny]?", mson);

		// -------------------------------------------- //
		// SPONGE MIMIC
		// -------------------------------------------- //
		// https://docs.spongepowered.org/en/plugin/basics/text.html

		// Text multiColoredText = Texts.builder("Sponges are ").color(TextColors.YELLOW).append(Texts.builder("invincible!").color(TextColors.RED).build()).build();
		mson = mson(
			mson("Sponges are ").color(YELLOW),
			mson("invincible!").color(RED)
		);
		test("Sponges are invincible", mson);
		
	}
	
	public static void test(String name, Mson mson)
	{
		System.out.println(name + ": " + mson);
	}

}
