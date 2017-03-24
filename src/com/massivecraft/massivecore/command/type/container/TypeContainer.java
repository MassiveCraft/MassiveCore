package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditContainer;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.ContainerUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class TypeContainer<C, E> extends TypeAbstract<C>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeContainer(Class<?> clazz, Type<E> innerType)
	{
		super(clazz);
		this.setInnerType(innerType);
	}
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return this.getCollectionTypeName() + " of " + this.getInnerType().getName();
	}
	
	public String getCollectionTypeName()
	{
		return super.getName();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private int indexStart = 0;
	public int getIndexStart() { return this.indexStart; }
	@SuppressWarnings("unchecked")
	public <X> X setIndexStart(int indexStart) { this.indexStart = indexStart; return (X)this; }
	
	private boolean indexVisible = true;
	public boolean isIndexVisible() { return this.indexVisible; }
	@SuppressWarnings("unchecked")
	public <X> X setIndexVisible(boolean indexVisible) { this.indexVisible = indexVisible; return (X)this; }
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@Override
	public Mson getVisualMsonInner(C container, CommandSender sender)
	{
		// Empty
		if (ContainerUtil.isEmpty(container)) return MSON_EMPTY;
		
		// Create
		List<Mson> parts = new MassiveList<>();
		
		// Fill
		List<E> elements = this.getContainerElementsOrdered(container);
		Type<E> innerType = this.getInnerType();
		int index = this.getIndexStart();
		for (E element : elements)
		{
			Mson part = innerType.getVisualMson(element, sender);
			if (this.isIndexVisible())
			{
				part = Mson.mson(
					Mson.mson(String.valueOf(index)).color(ChatColor.WHITE),
					Mson.SPACE,
					part
				);
			}
			parts.add(part);
			index++;
		}
		
		// Return
		return Mson.implode(parts, Mson.mson("\n"));
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(C container, CommandSender sender)
	{
		// Empty
		if (ContainerUtil.isEmpty(container)) return EMPTY;
		
		// Create
		List<String> parts = new MassiveList<>();
		
		// Fill
		List<E> elements = this.getContainerElementsOrdered(container);
		Type<E> innerType = this.getInnerType();
		int index = this.getIndexStart();
		for (E element : elements)
		{
			String part = innerType.getVisual(element, sender);
			if (this.isIndexVisible()) part = Txt.parse("<white>%d <yellow>%s", index, part);
			parts.add(part);
			index++;
		}
		
		// Return
		return Txt.implode(parts, "\n");
	}

	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	
	@Override
	public String getNameInner(C container)
	{
		// Empty
		if (ContainerUtil.isEmpty(container)) return "";
		
		// Create
		List<String> parts = new MassiveList<>();
		
		// Fill
		List<E> elements = this.getContainerElementsOrdered(container);
		Type<E> innerType = this.getInnerType();
		for (E element : elements)
		{
			String part = innerType.getName(element);
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}

	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
	@Override
	public String getIdInner(C container)
	{
		// Empty
		if (ContainerUtil.isEmpty(container)) return "";
		
		// Create
		List<String> parts = new MassiveList<>();
		
		// Fill
		Type<E> innerType = this.getInnerType();
		List<E> elements = this.getContainerElementsOrdered(container);
		for (E element : elements)
		{
			String part = innerType.getId(element);
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
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
				ContainerUtil.addElements(ret, allAble.getAll(sender));
				return ret;
			}
		}
		
		// Fill
		Type<E> innerType = this.getInnerType();
		for (String elementArg : Txt.PATTERN_WHITESPACE.split(arg))
		{
			E element = innerType.read(elementArg, sender);
			ContainerUtil.addElement(ret, element);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //
	
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
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equalsInner(C container1, C container2)
	{
		List<E> ordered1 = this.getContainerElementsOrdered(container1);
		List<E> ordered2 = this.getContainerElementsOrdered(container2);
		
		if (ordered1.size() != ordered2.size()) return false;
		
		Type<E> innerType = this.getInnerType();
		for (int index = 0; index < ordered1.size(); index++)
		{
			E element1 = ordered1.get(index);
			E element2 = ordered2.get(index);
			
			if ( ! innerType.equals(element1, element2)) return false;
		}
		
		return true;
	}
	
	// -------------------------------------------- //
	// EDITOR
	// -------------------------------------------- //
	
	@Override
	public <O> CommandEditAbstract<O, C> createEditCommand(EditSettings<O> settings, Property<O, C> property)
	{
		return new CommandEditContainer<>(settings, property);
	}
	
	// -------------------------------------------- //
	// ARGS
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
