package com.massivecraft.massivecore.command.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditSimple;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.comparator.ComparatorHashCode;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public abstract class TypeAbstract<T> implements Type<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String NULL = Txt.parse("<silver><em>NULL");
	public static final String EMPTY = Txt.parse("<silver><em>EMPTY");
	public static final String UNKNOWN = Txt.parse("<b>???");
	
	public static final ChatColor COLOR_DEFAULT = ChatColor.YELLOW;
	public static final ChatColor COLOR_NUMBER = ChatColor.LIGHT_PURPLE;
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		int prefixLength = "Type".length();
		String name = this.getClass().getSimpleName();
		
		// We don't want the "Type" part
		name = name.substring(prefixLength);
		
		// We split at uppercase letters, because most class names are camel-case.
		final String[] words = name.split("(?=[A-Z])");
		return Txt.implode(words, " ").toLowerCase();
	}
	
	// -------------------------------------------- //
	// INNER
	// -------------------------------------------- //
	
	protected List<Type<Object>> innerTypes = new MassiveList<Type<Object>>();
	
	@SuppressWarnings("unchecked")
	public <I extends Type<? extends Object>> List<I> getInnerTypes() { return (List<I>) this.innerTypes; }
	@SuppressWarnings("unchecked")
	public <I extends Type<? extends Object>> I getInnerType(int index) { return (I) this.getInnerTypes().get(index); }
	public <I extends Type<? extends Object>> I getInnerType() { return this.getInnerType(0); }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setInnerTypes(Collection<Type<?>> innerTypes) { this.innerTypes = new MassiveList(innerTypes); }
	public void setInnerTypes(Type<?>... innerTypes) { this.setInnerTypes(Arrays.asList(innerTypes)); };
	public void setInnerType(Type<?> innerType) { this.setInnerTypes(innerType); }
	
	// -------------------------------------------- //
	// WRITE VISUAL COLOR
	// -------------------------------------------- //
	
	protected ChatColor visualColor = COLOR_DEFAULT;
	public void setVisualColor(ChatColor color)
	{
		this.visualColor = color;
	}
	public ChatColor getVisualColor(T value, CommandSender sender)
	{
		return this.visualColor;
	}
	public ChatColor getVisualColor(T value)
	{
		return this.getVisualColor(value, null);
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	public String getVisualInner(T value, CommandSender sender)
	{
		if (value instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)value;
			String ret = senderEntity.getDisplayName(sender);
			return ret == null ? NULL : ret;
		}
		
		return this.getVisualColor(value, sender) + this.getNameInner(value);
	}
	public String getVisualInner(T value)
	{
		return this.getVisualInner(value, null);
	}
	
	public String getVisual(T value, CommandSender sender)
	{
		if (value == null) return NULL;
		return this.getVisualInner(value, sender);
	}
	public String getVisual(T value)
	{
		return this.getVisual(value, null);
	}
	
	public Set<String> getVisualsInner(T value, CommandSender sender)
	{
		return Collections.singleton(this.getVisualInner(value, sender));
	}
	public Set<String> getVisualsInner(T value)
	{
		return this.getVisualsInner(value, null);
	}
	
	public Set<String> getVisuals(T value, CommandSender sender)
	{
		if (value == null) return Collections.singleton(NULL);
		return this.getVisualsInner(value, sender);
	}
	public Set<String> getVisuals(T value)
	{
		return this.getVisuals(value, null);
	}
	
	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	
	public String getNameInner(T value)
	{
		if (value instanceof Named)
		{
			Named named = (Named) value;
			return named.getName();
		}
		
		return this.getIdInner(value);
	}
	
	public String getName(T value)
	{
		if (value == null) return null;
		return this.getNameInner(value);
	}
	
	public Set<String> getNamesInner(T value)
	{
		return Collections.singleton(this.getNameInner(value));
	}
	
	public Set<String> getNames(T value)
	{
		if (value == null) return Collections.emptySet();
		return this.getNamesInner(value);
	}
	
	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
	public String getIdInner(T value)
	{
		if (value instanceof Entity)
		{
			Entity<?> entity = (Entity<?>)value;
			return entity.getId();
		}
		else if (value instanceof String || value instanceof Number || value instanceof Boolean)
		{
			return value.toString();
		}
		
		return null;
	}
	
	public String getId(T value)
	{
		if (value == null) return null;
		return this.getIdInner(value);
	}
	
	public Set<String> getIdsInner(T value)
	{
		return Collections.singleton(this.getIdInner(value));
	}
	
	public Set<String> getIds(T value)
	{
		if (value == null) return Collections.emptySet();
		return this.getIdsInner(value);
	}
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
	public T read(CommandSender sender) throws MassiveException
	{
		return this.read(null, sender);
	}

	public T read(String arg) throws MassiveException
	{
		return this.read(arg, null);
	}

	public T read() throws MassiveException
	{
		return this.read(null, null);
	}
	
	// -------------------------------------------- //
	// VALID
	// -------------------------------------------- //
	
	public boolean isValid(String arg, CommandSender sender)
	{
		try
		{
			this.read(arg, sender);
			return true;
		}
		catch (MassiveException ex)
		{
			return false;
		}
	}
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //

	public boolean allowSpaceAfterTab()
	{
		return true;
	}
	
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		// Get the raw tab list.
		Collection<String> raw = this.getTabList(sender, arg);
		
		// Handle null case.
		if (raw == null || raw.isEmpty()) return Collections.emptyList();
		
		// Only keep the suggestions that starts with what the user already typed in.
		// This is the first basic step of tab completion.
		// "Ca" can complete into "Cayorion".
		// "Ma" can complete into "Madus"
		// "Ca" can not complete into "Madus" because it does not start with ignore case.
		List<String> ret = Txt.getStartsWithIgnoreCase(raw, arg);
		
		// Initial simple cleanup of suggestions.
		cleanSuggestions(ret);
		
		// Here we do a lot of things related to spaces.
		// Because spaces and tab completion doesn't go well together.
		// In the future we might be able to do something better, but Minecraft has its limitations.
		ret = prepareForSpaces(ret, arg);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// TAB LIST > PRIVATE TAB COMPLETE CALCULATIONS
	// -------------------------------------------- //
	
	// This method performs an initial cleanup of suggestions.
	// Currently we just throw away nulls and empty strings.
	private static void cleanSuggestions(List<String> suggestions)
	{
		ListIterator<String> iter = suggestions.listIterator();
		while (iter.hasNext())
		{
			String suggestion = iter.next();
			if (suggestion == null || suggestion.isEmpty())
			{
				iter.remove();
			}
		}
	}
	
	public static List<String> prepareForSpaces(List<String> suggestions, String arg)
	{
		// This will get the common prefix for all passed in suggestions.
		// This will allow us to tab complete some things with spaces
		// if we know they all start with the same value,
		// so we don't have to replace all of it.
		final String prefix = getPrefix(suggestions);
		
		// This is all the suggestions without the common prefix.
		List<String> ret = withoutPreAndSuffix(suggestions, prefix);
		// If it isn't empty and there is a prefix...
		if ( ! ret.isEmpty() && ! prefix.isEmpty())
		{
			// ...then we want the first one to have the prefix.
			// That prefix is not removed automatically,
			// due to how tab completion works.
			final String current = ret.get(0);
			String result = prefix;
			if ( ! current.isEmpty())
			{
				if (result.charAt(result.length()-1) != ' ') result += ' ';
				result += current;
			}
			
			int unwantedPrefixLength = arg.lastIndexOf(' ');
			if (unwantedPrefixLength != -1)
			{
				unwantedPrefixLength++;
				result = result.substring(unwantedPrefixLength);
			}
			ret.set(0, result);
		}
		
		return ret;
	}
	
	private static String getPrefix(List<String> suggestions)
	{
		String prefix = null;
		
		for (String suggestion : suggestions)
		{
			prefix = getOkay(prefix, suggestion);
		}
		
		if (prefix == null) return "";
		int lastSpace = prefix.lastIndexOf(" ");
		if (lastSpace == -1) return "";
		
		return prefix.substring(0, lastSpace + 1);
	}
	
	// This method return a new string only including the first characters that are equal.
	private static String getOkay(String original, String compared)
	{
		if (original == null) return compared;
		final int size = Math.min(original.length(), compared.length());
		StringBuilder ret = new StringBuilder();
		
		for (int i = 0; i < size; i++)
		{
			if (Character.toLowerCase(compared.charAt(i)) != Character.toLowerCase(original.charAt(i))) break;
			ret.append(compared.charAt(i));
		}
		
		if (ret.length() == 0) return "";
		
		int lastSpace = ret.lastIndexOf(" ");
		if (lastSpace == -1) return "";
		
		return ret.toString();
	}
	
	private static List<String> withoutPreAndSuffix(List<String> suggestions, String prefix)
	{
		LinkedHashSet<String> ret = new LinkedHashSet<String>(suggestions.size());
		boolean includesPrefix = false; // Sometimes a suggestion is equal to the prefix.
		for (String suggestion : suggestions)
		{
			if (suggestion.equals(prefix) && !includesPrefix)
			{
				ret.add("");
				includesPrefix = true;
				continue;
			}
			// We remove the prefix because we only want that once.
			// But we can't keep things after the first part either
			// because of spaces and stuff.
			if (suggestion.length() <= prefix.length()) continue;
			int lastSpace = suggestion.indexOf(' ', prefix.length());
			int lastIndex = lastSpace != -1 ? lastSpace : suggestion.length();
			ret.add(suggestion.substring(prefix.length(), lastIndex));
		}
		
		return new ArrayList<String>(ret);
	}
	
	// -------------------------------------------- //
	// CONTAINER
	// -------------------------------------------- //
	
	public boolean isContainer()
	{
		return this.isContainerCollection() || this.isContainerMap();
	}
	
	private Boolean containerMap = null;
	public boolean isContainerMap()
	{
		if (this.containerMap == null) this.containerMap = this.calcContainerMap();
		return this.containerMap;
	}
	protected boolean calcContainerMap()
	{
		T instance = this.createNewInstance();
		if (instance instanceof Map<?, ?>) return true;
		return false;
	}
	
	private Boolean containerCollection = null;
	public boolean isContainerCollection()
	{
		if (this.containerCollection == null) this.containerCollection = this.calcContainerCollection();
		return this.containerCollection;
	}
	protected boolean calcContainerCollection()
	{
		T instance = this.createNewInstance();
		if (instance instanceof Collection<?>) return true;
		return false;
	}
	
	public boolean isContainerIndexed()
	{
		return this.isContainerOrdered() || this.isContainerSorted();
	}
	
	private Boolean collectionOrdered = null;
	public boolean isContainerOrdered()
	{
		if (this.collectionOrdered == null) this.collectionOrdered = this.calcContainerOrdered();
		return this.collectionOrdered;
	}
	protected boolean calcContainerOrdered()
	{
		T instance = this.createNewInstance();
		if (instance instanceof List<?>) return true;
		if (instance instanceof LinkedHashMap<?, ?>) return true;
		return false;
	}
	
	private Boolean collectionSorted = null;
	public boolean isContainerSorted()
	{
		if (this.collectionSorted == null) this.collectionSorted = this.calcContainerSorted();
		return this.collectionSorted;
	}
	protected boolean calcContainerSorted()
	{
		T instance = this.createNewInstance();
		if (instance instanceof SortedSet<?>) return true;
		if (instance instanceof SortedMap<?, ?>) return true;
		return false;
	}
	
	private Comparator<Object> elementComparator = null;
	@SuppressWarnings("unchecked")
	public <E> Comparator<E> getContainerComparator()
	{
		if (this.elementComparator != null) return (Comparator<E>) this.elementComparator;
		if (this.isContainerIndexed()) return null;
		return (Comparator<E>) ComparatorHashCode.get().getLenient();
	}
	@SuppressWarnings("unchecked")
	public void setContainerComparator(Comparator<?> comparator) { this.elementComparator = (Comparator<Object>) comparator; }
	
	public <E> List<E> getContainerElementsOrdered(Iterable<E> elements)
	{
		if (elements == null) return null;
		
		List<E> ret;
		if (elements instanceof Collection<?>)
		{
			ret = new MassiveList<E>((Collection<E>)elements);
		}
		else
		{
			ret = new MassiveList<E>();
			for (E element : elements)
			{
				ret.add(element);
			}
		}
		
		Comparator<E> elementComparator = this.getContainerComparator();
		if (elementComparator != null) ret.sort(elementComparator);
		
		return ret;
	}
	
	@Override
	public <E> List<E> getContainerElementsOrdered(T container)
	{
		Collection<E> elements = this.getContainerElements(container);
		return this.getContainerElementsOrdered(elements);
	}
	
	public boolean isContainerEmpty(T container)
	{
		return this.getContainerElements(container).isEmpty();
	}
	
	@Override
	public void clearContainer(T container)
	{
		this.getContainerElements(container).clear();
	}
	
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getContainerElements(T container)
	{
		if (container instanceof Collection<?>)
		{
			Collection<E> collection = (Collection<E>)container;
			return collection;
		}
		
		if (container instanceof Map<?, ?>)
		{
			Map<?, ?> map = (Map<?, ?>)container;
			return (Collection<E>) map.entrySet();
		}
		
		throw new UnsupportedOperationException("not implemented");
	}
	
	public <E> void setContainerElements(T container, Iterable<E> elements)
	{
		this.clearContainer(container);
		this.addContainerElements(container, elements);
	}
	
	@SuppressWarnings("unchecked")
	public <E> boolean addContainerElement(T container, E element)
	{
		if (container instanceof Collection<?>)
		{
			Collection<E> collection = (Collection<E>)container;
			return collection.add(element);
		}
		
		if (container instanceof Map<?, ?>)
		{
			Map<Object, Object> map = (Map<Object, Object>)container;
			Entry<Object, Object> entry = (Entry<Object, Object>)element;
			Object key = entry.getKey();
			Object after = entry.getValue();
			Object before = map.put(key, after);
			return ! MUtil.equals(after, before);
		}
		
		throw new UnsupportedOperationException("not implemented");
	}
	
	public <E> void addContainerElements(T container, Iterable<E> elements)
	{
		for (E element : elements)
		{
			this.addContainerElement(container, element);
		}
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	public boolean equals(T type1, T type2, boolean strict)
	{
		if (type1 == null) return type2 == null;
		if (type2 == null) return type1 == null;
		return this.equalsInner(type1, type2, strict);
	}
	
	public boolean equalsInner(T type1, T type2, boolean strict)
	{
		return type1.equals(type2);
	}
	
	// -------------------------------------------- //
	// EDITOR
	// -------------------------------------------- //
	
	public <O> CommandEditAbstract<O, T> createEditCommand(EditSettings<O> settings, Property<O, T> property)
	{
		return new CommandEditSimple<O, T>(settings, property);
	}
	
	public T createNewInstance()
	{
		return null;
	}
	
}
