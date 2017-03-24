package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.mson.Mson;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

public class TypeWrapper<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeWrapper(Type<T> innerType)
	{
		super(innerType.getClazz());
		this.setInnerType(innerType);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return this.getInnerType().getName();
	}
	
	@Override
	public List<Mson> getShowInner(T value, CommandSender sender)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getShowInner(value, sender);
	}
	
	@Override
	public Mson getVisualMsonInner(T value, CommandSender sender)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getVisualMsonInner(value, sender);
	}
	
	@Override
	public String getVisualInner(T value, CommandSender sender)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getVisualInner(value, sender);
	}

	@Override
	public String getNameInner(T value)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getNameInner(value);
	}

	@Override
	public String getIdInner(T value)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getIdInner(value);
	}

	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		Type<T> inner = this.getInnerType();
		return inner.read(arg, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return new MassiveList<>(this.getInnerType().getTabList(sender, arg));
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerType().allowSpaceAfterTab();
	}
	
	@Override
	public <I extends Property<T, ?>> List<I> getInnerProperties()
	{
		Type<T> innerType = this.getInnerType();
		return innerType.getInnerProperties();
	}
	
	@Override
	public <I extends Property<T, ?>> void setInnerProperties(Collection<I> innerProperties)
	{
		Type<T> innerType = this.getInnerType();
		innerType.setInnerProperties(innerProperties);
	}
	
	@Override 
	public <O> CommandEditAbstract<O, T> createEditCommand(EditSettings<O> settings, Property<O, T> property)
	{
		Type<T> innerType = this.getInnerType();
		return innerType.createEditCommand(settings, property);
	}
	
	@Override
	public T createNewInstance()
	{
		Type<T> innerType = this.getInnerType();
		return innerType.createNewInstance();
	}
	
}
