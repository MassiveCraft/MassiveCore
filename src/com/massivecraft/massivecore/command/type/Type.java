package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;

public interface Type<T>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	// Human friendly name
	public String getTypeName();
	
	// -------------------------------------------- //
	// INNER
	// -------------------------------------------- //
	
	public <I extends Type<?>> List<I> getInnerTypes();
	public <I extends Type<?>> I getInnerType(int index);
	public <I extends Type<?>> I getInnerType();
	
	public void setInnerTypes(Collection<Type<?>> innerTypes);
	public void setInnerTypes(Type<?>... innerTypes);
	public void setInnerType(Type<?> innerType);
	
	// -------------------------------------------- //
	// WRITE VISUAL COLOR
	// -------------------------------------------- //
	
	public ChatColor getVisualColor(T value, CommandSender sender);
	public ChatColor getVisualColor(T value);
	public void setVisualColor(ChatColor color);
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	// A visual and colorful representation. Possibly with added detail such as simple ASCII art.
	
	public String getVisualInner(T value, CommandSender sender);
	public String getVisualInner(T value);
	public String getVisual(T value, CommandSender sender);
	public String getVisual(T value);
	
	public Set<String> getVisualsInner(T value, CommandSender sender);
	public Set<String> getVisualsInner(T value);
	public Set<String> getVisuals(T value, CommandSender sender);
	public Set<String> getVisuals(T value);
	
	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	// A human friendly but simple representation without color and clutter.
	
	public String getNameInner(T value);
	public String getName(T value);
	public Set<String> getNamesInner(T value);
	public Set<String> getNames(T value);
	
	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	// System identification string. Most times unsuitable for humans. 
	
	public String getIdInner(T value);
	public String getId(T value);
	public Set<String> getIdsInner(T value);
	public Set<String> getIds(T value);
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
	public T read(String arg, CommandSender sender) throws MassiveException;
	public T read(CommandSender sender) throws MassiveException;
	public T read(String arg) throws MassiveException;
	public T read() throws MassiveException;

	// -------------------------------------------- //
	// VALID
	// -------------------------------------------- //
	// Used for arbitrary argument order
	
	public boolean isValid(String arg, CommandSender sender);
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //
	
	// The sender is the one that tried to tab complete.
	// The arg is beginning the word they are trying to tab complete.
	public Collection<String> getTabList(CommandSender sender, String arg);
	public List<String> getTabListFiltered(CommandSender sender, String arg);
	
	// Sometimes we put a space after a tab completion.
	// That would however not make sense with all Types.
	// Default is true;
	public boolean allowSpaceAfterTab();
	
	// -------------------------------------------- //
	// CONTAINER > IS
	// -------------------------------------------- //
	
	public boolean isContainer();
	public boolean isContainerMap();
	public boolean isContainerCollection();
	
	public boolean isContainerIndexed();
	public boolean isContainerOrdered();
	public boolean isContainerSorted();
	
	// -------------------------------------------- //
	// CONTAINER > COMPARATOR
	// -------------------------------------------- //
	
	public <E> Comparator<E> getContainerComparator();
	public void setContainerComparator(Comparator<?> container);
	
	public <E> List<E> getContainerElementsOrdered(Iterable<E> elements);
	public <E> List<E> getContainerElementsOrdered(T container);
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	public boolean equals(T type1, T type2);
	public boolean equalsInner(T type1, T type2);
	
	// -------------------------------------------- //
	// EDITOR
	// -------------------------------------------- //
	
	public <O> CommandEditAbstract<O, T> createEditCommand(EditSettings<O> settings, Property<O, T> property);
	public T createNewInstance();
	
}
