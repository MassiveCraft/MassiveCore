package com.massivecraft.massivecore.command.type.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditCollection;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.util.Txt;

public abstract class TypeCollection<C extends Collection<E>, E> extends TypeAbstract<C>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeCollection(Type<E> innerType)
	{
		this.setInnerType(innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return this.getCollectionTypeName() + " of " + this.getInnerType().getTypeName();
	}
	
	public String getCollectionTypeName()
	{
		return "Collection";
	}
	
	@Override
	public String getVisualInner(C value, CommandSender sender)
	{
		// Empty
		if (value.isEmpty()) return EMPTY;
		
		List<String> parts = new MassiveList<String>();
		
		int index = -1;
		for (E element : value)
		{
			index++;
			String part = Txt.parse("<white>%d <yellow>%s", index, this.getInnerType().getVisualInner(element, sender));
			parts.add(part);
		}
		
		return Txt.implode(parts, "\n");
	}

	@Override
	public String getNameInner(C value)
	{
		// Empty
		if (value.isEmpty()) return "";
		
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (E element : value)
		{
			String part = this.getInnerType().getNameInner(element);
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}

	@Override
	public String getIdInner(C value)
	{
		// Empty
		if (value.isEmpty()) return "";
		
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (E element : value)
		{
			String part = this.getInnerType().getIdInner(element);
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public C read(String arg, CommandSender sender) throws MassiveException
	{
		// Create
		C ret = this.createNewInstance();
		
		// Check All
		if (this.getInnerType() instanceof AllAble)
		{
			AllAble<E> allAble = (AllAble<E>)this.getInnerType();
			if (arg.equalsIgnoreCase("all"))
			{
				ret.addAll(allAble.getAll(sender));
				return ret;
			}
		}
		
		// Fill
		String[] elementArgs = Txt.PATTERN_WHITESPACE.split(arg);
		for (String elementArg : elementArgs)
		{
			Type<E> innerType = this.getInnerType();
			E element = innerType.read(elementArg, sender);
			ret.add(element);
		}
		
		// Return
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments of the same type.
		// The passed arg might be more than one. We only want the latest.
		return this.getInnerType().getTabList(sender, getLastArg(arg));
	}
	
	@Override
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments of the same type.
		// The passed arg might be more than one. We only want the latest.
		return this.getInnerType().getTabListFiltered(sender, getLastArg(arg));
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerType().allowSpaceAfterTab();
	}
	
	@Override
	public <O> CommandEditAbstract<O, C> createEditCommand(EditSettings<O> settings, Property<O, C> property)
	{
		return new CommandEditCollection<O, C>(settings, property);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static List<String> getArgs(String string)
	{
		return Arrays.asList(Txt.PATTERN_WHITESPACE.split(string, -1));
	}
	
	public static String getLastArg(String string)
	{
		List<String> args = getArgs(string);
		if (args.isEmpty()) return null;
		return args.get(args.size() - 1);
	}
	
}
