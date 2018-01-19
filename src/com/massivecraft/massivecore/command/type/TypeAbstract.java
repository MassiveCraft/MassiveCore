package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.Colorized;
import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditProperties;
import com.massivecraft.massivecore.command.editor.CommandEditSimple;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.editor.PropertyReflection;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.ContainerUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public abstract class TypeAbstract<T> implements Type<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String NULL = Txt.parse("<silver><em>NONE");
	public static final String EMPTY = Txt.parse("<silver><em>EMPTY");
	public static final String UNKNOWN = Txt.parse("<b>???");
	public static final String COLONSPACE = Txt.parse("<silver>: ");
	
	public static final Mson MSON_NULL = Mson.fromParsedMessage(NULL);
	public static final Mson MSON_EMPTY = Mson.fromParsedMessage(EMPTY);
	public static final Mson MSON_UNKNOWN = Mson.fromParsedMessage(UNKNOWN);
	public static final Mson MSON_COLONSPACE = Mson.fromParsedMessage(COLONSPACE);
	
	public static final ChatColor COLOR_DEFAULT = ChatColor.YELLOW;
	public static final ChatColor COLOR_NUMBER = ChatColor.LIGHT_PURPLE;
	
	public static final int TAB_LIST_UUID_THRESHOLD = 5;
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		int prefixLength = "Type".length();
		String name = this.getClass().getSimpleName();
		
		// We don't want the "Type" part
		name = name.substring(prefixLength);
		
		// We split at uppercase letters, because most class names are camel-case.
		final List<String> words = Txt.camelsplit(name);
		return Txt.implode(words, " ").toLowerCase();
	}
	
	protected final Class<T> clazz;
	public Class<T> getClazz() { return this.clazz; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public TypeAbstract(Class<?> clazz)
	{
		this.clazz = (Class<T>) clazz;
		
		try
		{
			constructor = ReflectionUtil.getConstructor(clazz);	
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	// -------------------------------------------- //
	// INNER
	// -------------------------------------------- //
	
	protected List<Type<?>> innerTypes = new MassiveList<>();
	
	@SuppressWarnings("unchecked")
	@Override public <I extends Type<?>> List<I> getInnerTypes() { return (List<I>) this.innerTypes; }
	@SuppressWarnings("unchecked")
	@Override public <I extends Type<?>> I getInnerType(int index) { return (I) this.getInnerTypes().get(index); }
	@Override public <I extends Type<?>> I getInnerType() { return this.getInnerType(0); }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override public void setInnerTypes(Collection<Type<?>> innerTypes) { this.innerTypes = new MassiveList(innerTypes); }
	@Override public void setInnerTypes(Type<?>... innerTypes) { this.setInnerTypes(Arrays.asList(innerTypes)); }
	
	@Override public void setInnerType(Type<?> innerType) { this.setInnerTypes(innerType); }
	
	private List<Integer> userOrder = null;
	@Override public void setUserOrder(List<Integer> userOrder) { this.userOrder = userOrder; }
	@Override public void setUserOrder(Integer... userOrder) { this.setUserOrder(Arrays.asList(userOrder)); }
	@Override public List<Integer> getUserOrder()
	{
		if (this.userOrder == null)
		{
			this.userOrder = MUtil.range(0, this.getInnerTypes().size());
		}
		return this.userOrder;
	}
	@Override
	public Integer getIndexUser(int indexTechy)
	{
		if (this.userOrder == null) return indexTechy;
		Integer ret = this.userOrder.indexOf(indexTechy);
		if (ret.equals(-1)) return null;
		return ret;
	}
	@Override
	public Integer getIndexTech(int indexUser)
	{
		if (this.userOrder == null) return indexUser;
		return this.userOrder.get(indexUser);
	}
	
	// -------------------------------------------- //
	// INNER PROPERTY
	// -------------------------------------------- //
	
	protected List<Property<T, ?>> innerProperties = new MassiveList<>();
	
	public boolean hasInnerProperties() { return ! this.getInnerProperties().isEmpty(); }
	
	@SuppressWarnings("unchecked")
	public <I extends Property<T, ?>> List<I> getInnerProperties() { return (List<I>) this.innerProperties; }
	@SuppressWarnings("unchecked")
	public <I extends Property<T, ?>> I getInnerProperty(int index) { return (I) this.getInnerProperties().get(index); }
	
	public <I extends Property<T, ?>> void setInnerProperties(Collection<I> innerProperties) { this.innerProperties = new MassiveList<Property<T, ?>>(innerProperties); }
	@SafeVarargs
	public final <I extends Property<T, ?>> void setInnerProperties(I... innerProperties) { this.setInnerProperties(Arrays.asList(innerProperties)); }
	public void setInnerProperties(Class<T> clazz) { this.setInnerProperties(PropertyReflection.getAll(clazz, this)); }
	
	// -------------------------------------------- //
	// WRITE SHOW
	// -------------------------------------------- //
	// A list of property values.
	
	public List<Mson> getShowInner(T value, CommandSender sender)
	{
		if (this.hasInnerProperties())
		{
			return Property.getShowLines(value, sender, this.getInnerProperties());
		}
		return this.getVisualMsonInner(value, sender).split(Txt.PATTERN_NEWLINE);
	}
	public List<Mson> getShow(T value, CommandSender sender)
	{
		if (value == null) return Collections.singletonList(MSON_NULL);
		return this.getShowInner(value, sender);
	}
	public List<Mson> getShow(T value)
	{
		return this.getShow(value, null);
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL COLOR
	// -------------------------------------------- //
	
	protected ChatColor visualColor = COLOR_DEFAULT;
	@Override
	public void setVisualColor(ChatColor color)
	{
		this.visualColor = color;
	}
	@Override
	public ChatColor getVisualColor(T value, CommandSender sender)
	{
		if (value instanceof Colorized)
		{
			Colorized colorized = (Colorized) value;
			return colorized.getColor();
		}
		return this.visualColor;
	}
	@Override
	public ChatColor getVisualColor(T value)
	{
		return this.getVisualColor(value, null);
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL MSON
	// -------------------------------------------- //
	// A visual mson.
	
	protected boolean visualMsonOverridden = calcVisualMsonOverridden();
	public boolean isVisualMsonOverridden() { return this.visualMsonOverridden; }
	public void setVisualMsonOverridden(boolean visualMsonOverridden) { this.visualMsonOverridden = visualMsonOverridden; }
	public boolean calcVisualMsonOverridden()
	{
		return ! TypeAbstract.class.equals(ReflectionUtil.getSuperclassDeclaringMethod(this.getClass(), true, "getVisualMsonInner"));
	}
	
	@Override
	public Mson getVisualMsonInner(T value, CommandSender sender)
	{
		String visualInner = this.getVisualInner(value, sender);
		
		Mson ret = Mson.fromParsedMessage(visualInner);
		if (this.hasInnerProperties()) ret.tooltip(Mson.toPlain(this.getShow(value, sender), true));
		return ret;
	}
	
	@Override
	public Mson getVisualMson(T value, CommandSender sender)
	{
		if (value == null) return MSON_NULL;
		return this.getVisualMsonInner(value, sender);
	}
	@Override
	public Mson getVisualMson(T value)
	{
		return this.getVisualMson(value, null);
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(T value, CommandSender sender)
	{
		if (value instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)value;
			String ret = senderEntity.getDisplayName(sender);
			return ret == null ? NULL : ret;
		}
		if (this.isVisualMsonOverridden())
		{
			return this.getVisualMsonInner(value, sender).toPlain(true);
		}
		else
		{
			return this.getVisualColor(value, sender) + this.getNameInner(value);	
		}
	}

	@Override
	public String getVisual(T value, CommandSender sender)
	{
		if (value == null) return NULL;
		return this.getVisualInner(value, sender);
	}
	@Override
	public String getVisual(T value)
	{
		return this.getVisual(value, null);
	}

	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	
	@Override
	public String getNameInner(T value)
	{
		if (value instanceof Named)
		{
			Named named = (Named) value;
			return named.getName();
		}
		
		return this.getIdInner(value);
	}
	
	@Override
	public String getName(T value)
	{
		if (value == null) return null;
		return this.getNameInner(value);
	}
	
	@Override
	public Set<String> getNamesInner(T value)
	{
		return Collections.singleton(this.getNameInner(value));
	}
	
	@Override
	public Set<String> getNames(T value)
	{
		if (value == null) return Collections.emptySet();
		return this.getNamesInner(value);
	}
	
	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
	@Override
	public String getIdInner(T value)
	{
		if (value instanceof Identified)
		{
			Identified identified = (Identified)value;
			return identified.getId();
		}
		else if (value instanceof String || value instanceof Number || value instanceof Boolean)
		{
			return value.toString();
		}
		
		return null;
	}
	
	@Override
	public String getId(T value)
	{
		if (value == null) return null;
		return this.getIdInner(value);
	}
	
	@Override
	public Set<String> getIdsInner(T value)
	{
		return Collections.singleton(this.getIdInner(value));
	}
	
	@Override
	public Set<String> getIds(T value)
	{
		if (value == null) return Collections.emptySet();
		return this.getIdsInner(value);
	}
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
	@Override
	public T read(CommandSender sender) throws MassiveException
	{
		return this.read(null, sender);
	}

	@Override
	public T read(String arg) throws MassiveException
	{
		return this.read(arg, null);
	}

	@Override
	public T read() throws MassiveException
	{
		return this.read(null, null);
	}
	
	// -------------------------------------------- //
	// VALID
	// -------------------------------------------- //
	
	@Override
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

	@Override
	public boolean allowSpaceAfterTab()
	{
		return true;
	}
	
	@Override
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
		MassiveSet<String> ret = new MassiveSet<>(suggestions.size());
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
		
		return new ArrayList<>(ret);
	}
	
	// -------------------------------------------- //
	// CONTAINER > IS
	// -------------------------------------------- //
	
	private Boolean container = null;
	@Override public boolean isContainer() { this.calcContainer(); return this.container; }
	
	private Boolean containerMap = null;
	@Override public boolean isContainerMap() { this.calcContainer(); return this.containerMap; }
	
	private Boolean containerCollection = null;
	@Override public boolean isContainerCollection() { this.calcContainer(); return this.containerCollection; }
	
	private Boolean containerIndexed = null;
	@Override public boolean isContainerIndexed() { this.calcContainer(); return this.containerIndexed; }
	
	private Boolean containerOrdered = null;
	@Override public boolean isContainerOrdered() { this.calcContainer(); return this.containerOrdered; }
	
	private Boolean containerSorted = null;
	@Override public boolean isContainerSorted() { this.calcContainer(); return this.containerSorted; }
	
	private void calcContainer()
	{
		if (this.container != null) return;
		T instance = this.createNewInstance();
		this.container = ContainerUtil.isContainer(instance);
		this.containerMap = ContainerUtil.isMap(instance);
		this.containerCollection = ContainerUtil.isCollection(instance);
		this.containerIndexed = ContainerUtil.isIndexed(instance);
		this.containerOrdered = ContainerUtil.isOrdered(instance);
		this.containerSorted = ContainerUtil.isSorted(instance);
	}
	
	// -------------------------------------------- //
	// CONTAINER > COMPARATOR
	// -------------------------------------------- //
	
	private Comparator<Object> elementComparator = null;
	@SuppressWarnings("unchecked")
	@Override
	public <E> Comparator<E> getContainerComparator()
	{
		if (this.elementComparator != null) return (Comparator<E>) this.elementComparator;
		if (this.isContainerIndexed()) return null;
		return (Comparator<E>) ComparatorSmart.get();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setContainerComparator(Comparator<?> comparator) { this.elementComparator = (Comparator<Object>) comparator; }
	
	@Override
	public <E> List<E> getContainerElementsOrdered(Iterable<E> elements)
	{
		if (elements == null) return null;
		
		List<E> ret;
		if (elements instanceof Collection<?>)
		{
			ret = new MassiveList<>((Collection<E>) elements);
		}
		else
		{
			ret = new MassiveList<>();
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
		Collection<E> elements = ContainerUtil.getElements(container);
		return this.getContainerElementsOrdered(elements);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(T type1, T type2)
	{
		if (type1 == null) return type2 == null;
		if (type2 == null) return type1 == null;
		return this.equalsInner(type1, type2);
	}
	
	@Override 
	public boolean equalsInner(T type1, T type2)
	{
		return type1.equals(type2);
	}
	
	// -------------------------------------------- //
	// EDITOR
	// -------------------------------------------- //
	
	@Override 
	public <O> CommandEditAbstract<O, T> createEditCommand(EditSettings<O> settings, Property<O, T> property)
	{
		if (this.hasInnerProperties())
		{
			return new CommandEditProperties<>(settings, property);
		}
		else
		{
			return new CommandEditSimple<>(settings, property);
		}
	}
	
	
	private Constructor<T> constructor;
	@Override
	public T createNewInstance()
	{
		try
		{
			return this.constructor.newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}

}
