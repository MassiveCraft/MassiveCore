package com.massivecraft.massivecore.mson;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;

import static com.massivecraft.massivecore.mson.Mson.mson;
import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

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
	
	public static Mson mson;
	public static void test()
	{
		mson = mson("hello");
		test("ofString");
		
		mson = mson(
			mson("hello")
		);
		test("ofMson");
		
		mson = mson(
			new MassiveList<>("hello ", "you!")
		);
		test("ofCollection");
		
		mson = mson
		(
			"test",
			mson
			(
				" test2",
				mson
				(
					" test3",
					mson("this ", "is only ", "one way to do it!")
				)
			).link("www.massivecraft,com")
		).color(BLUE)
		.tooltip("Holy Moly");
		
		test("child");
		
		
		mson = Mson.fromParsedMessage(Txt.parse("white <i>in<em>fo <b><em><bold>bad <lime>green"));
		test("parsed");
		
		mson = Mson.parse("white <i>in<em>fo <b><bold>bad <lime>green");
		test("parsed2");
		
		mson = Mson.parse("<i>insert here %s whatever <g> you <b>%s <g>could wish for!", "!1337!", "herpi derp");
		test("parseFormat");
		
		mson = Mson.format("Just a %s simple string! :)", "very");
		test("format");
		
		// -------------------------------------------- //
		// TEST SPLIT
		// -------------------------------------------- //
		
		mson = mson(mson("1 2 3 4").split(Txt.PATTERN_NEWLINE));
		test("split0");
		
		mson = mson(mson("\n1 2 \n3 4").split(Txt.PATTERN_NEWLINE));
		test("split1");
		
		mson = mson(mson(" 1 2 3 4 ").add("5 6 7").split(Txt.PATTERN_WHITESPACE));
		test("split2");
		
		Mson split = mson(" 1 2 3 4 ").add("5 6 7").add("8 9");
		List<Mson> splits = split.split(Txt.PATTERN_WHITESPACE);
		
		test("splitA", split);
		test("splitB", Mson.implode(splits, Mson.SPACE));
		
		// -------------------------------------------- //
		// TEST REPLACE
		// -------------------------------------------- //
		
		mson = mson("1 2 3 4 5 6 1 7 tests", " 01010101").getRoot().replace('1', '0');
		test("charr");
	
		mson = mson("1 2 3 4 5 6 1 7 tests", " 01010101").getRoot().replaceAll("1", "0");
		test("regex");
		
		// -------------------------------------------- //
		// TEST SPECIAL REPLACE ALL
		// -------------------------------------------- //
		
		// replace string mson
		
		mson = mson
		(
			"1 2 3 4 5 6 1 7 tests",
			" 1+1+1",
			"herpiderp"
		).style(BLUE).replaceAll("1", mson("0"));
		test("replaceAll1");
		
		mson = mson
		(
			"hellohello",
			"hello",
			"hello"
		).replaceAll("hello", mson("lol"));
		
		test("overload");
		
		
		Mson toReplace = mson("hallo");
		
		// replace mson mson
		mson = mson
		(
			"1 2 3 4 5 6 7 tests ",
			toReplace,
			" miep"
		).replaceAll(toReplace, mson("tests"));
		test("replaceAll2");
		
		mson = mson
		(
			"1 2 3 4 5 6 7 tests ",
			"hallo",
			"hallo",
			"hallo",
			" miep"
		).replaceAll(mson("hallo"), mson("tests"));
		
		test("overload2");
		
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
		test("Would you like to [allow] or [deny]?");

		// -------------------------------------------- //
		// SPONGE MIMIC
		// -------------------------------------------- //
		// https://docs.spongepowered.org/en/plugin/basics/text.html

		// Text multiColoredText = Texts.builder("Sponges are ").color(TextColors.YELLOW).append(Texts.builder("invincible!").color(TextColors.RED).build()).build();
		mson = mson(
			mson("Sponges are ").color(YELLOW),
			mson("invincible!").color(RED)
		);
		test("Sponges are invincible");
		
	}
	
	public static void test(String name)
	{
		test(name, mson);
	}
	
	public static void test(String name, Mson mson)
	{
		System.out.println(name + ": " + mson);
	}

}
