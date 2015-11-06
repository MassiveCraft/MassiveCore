package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;

// The "inner" controls all ways the type behaves and "seems".
// The "outer" type is how the type interfaces in source code. For example what is read.
public abstract class TypeTransformer<I, O> extends TypeAbstract<O>
{
	// -------------------------------------------- //
	// PREFIX
	// -------------------------------------------- //
	
	public static final String PREFIX = UNKNOWN + " ";
	
	public static String prefix(String string)
	{
		return PREFIX + string;
	}
	
	public static Set<String> prefix(Set<String> strings)
	{
		// Create
		Set<String> ret = new MassiveSet<String>(strings.size());
		
		// Fill
		for (String string : strings)
		{
			ret.add(prefix(string));
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final Type<I> INNER;
	public final Type<O> OUTER;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeTransformer(Type<I> typeInner, Type<O> typeOuter)
	{
		this.setInnerTypes(typeInner, typeOuter);
		INNER = typeInner;
		OUTER = typeOuter;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	// NOTE: These should return null on null input. 
	
	public abstract O innerToOuter(I inner);
	public abstract I outerToInner(O outer);
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return INNER.getTypeName();
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL COLOR
	// -------------------------------------------- //
	
	@Override
	public ChatColor getVisualColor(O outer, CommandSender sender)
	{
		I inner = outerToInner(outer);
		// NOTE: Inner type must NPE evade.
		return INNER.getVisualColor(inner, sender);
	}
	
	@Override
	public ChatColor getVisualColor(O outer)
	{
		I inner = outerToInner(outer);
		// NOTE: Inner type must NPE evade.
		return INNER.getVisualColor(inner);
	}
	
	@Override
	public void setVisualColor(ChatColor color)
	{
		INNER.setVisualColor(color);
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(O outer, CommandSender sender)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisualInner(outer, sender));
		return INNER.getVisualInner(inner, sender);
	}
	
	@Override
	public String getVisualInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisualInner(outer));
		return INNER.getVisualInner(inner);
	}
	
	@Override
	public String getVisual(O outer, CommandSender sender)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisual(outer, sender));
		return INNER.getVisual(inner, sender);
	}
	
	@Override
	public String getVisual(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisual(outer));
		return INNER.getVisual(inner);
	}
	
	@Override
	public Set<String> getVisualsInner(O outer, CommandSender sender)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisualsInner(outer, sender));
		return INNER.getVisualsInner(inner, sender);
	}
	
	@Override
	public Set<String> getVisualsInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisualsInner(outer));
		return INNER.getVisualsInner(inner);
	}
	
	@Override
	public Set<String> getVisuals(O outer, CommandSender sender)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisuals(outer, sender));
		return INNER.getVisuals(inner, sender);
	}
	
	@Override
	public Set<String> getVisuals(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) return prefix(OUTER.getVisuals(outer));
		return INNER.getVisuals(inner);
	}
	
	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	
	@Override
	public String getNameInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getNameInner(inner);
	}
	
	@Override
	public String getName(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getName(inner);
	}
	
	@Override
	public Set<String> getNamesInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getNamesInner(inner);
	}
	
	@Override
	public Set<String> getNames(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getNames(inner);
	}
	
	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
	@Override
	public String getIdInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getIdInner(inner);
	}
	
	@Override
	public String getId(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getId(inner);
	}
	
	@Override
	public Set<String> getIdsInner(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getIdsInner(inner);
	}
	
	@Override
	public Set<String> getIds(O outer)
	{
		I inner = outerToInner(outer);
		if (outer != null && inner == null) throw new NullPointerException("transformer");
		return INNER.getIds(inner);
	}
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
	@Override
	public O read(String arg, CommandSender sender) throws MassiveException
	{
		I inner = INNER.read(arg, sender);
		O outer = innerToOuter(inner);
		return outer;
	}
	
	@Override
	public O read(CommandSender sender) throws MassiveException
	{
		I inner = INNER.read(sender);
		O outer = innerToOuter(inner);
		return outer;
	}
	
	@Override
	public O read(String arg) throws MassiveException
	{
		I inner = INNER.read(arg);
		O outer = innerToOuter(inner);
		return outer;
	}
	
	@Override
	public O read() throws MassiveException
	{
		I inner = INNER.read();
		O outer = innerToOuter(inner);
		return outer;
	}
	
	// -------------------------------------------- //
	// VALID
	// -------------------------------------------- //
	
	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		return INNER.isValid(arg, sender);
	}
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return INNER.getTabList(sender, arg);
	}
	
	@Override
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		return INNER.getTabListFiltered(sender, arg);
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return INNER.allowSpaceAfterTab();
	}
	
	// -------------------------------------------- //
	// EDITOR
	// -------------------------------------------- //
	
	@Override
	public <S> CommandEditAbstract<S, O> createEditCommand(EditSettings<S> settings, Property<S, O> property)
	{
		return OUTER.createEditCommand(settings, property);
	}
	
	@Override
	public O createNewInstance()
	{
		return OUTER.createNewInstance();
	}

}
