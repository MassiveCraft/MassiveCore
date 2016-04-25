package com.massivecraft.massivecore.item;

import java.util.Arrays;
import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;

public abstract class WriterAbstract<OA, OB, CA, CB, FA, FB> extends Engine
{
	// -------------------------------------------- //
	// WRITERS
	// -------------------------------------------- //
	// Writer have dependencies and child writers.
	
	// A dependency is another writer that must be successfully activated for this writer to function.
	// For that reason the dependencies are activated just after the provoke logic.
	// Examples would be WriterPotionEffect and WriterFireworkEffect.
	// They are implicitly required for some ItemStack field writers. 
	
	private List<Class<?>> dependencyClasses = new MassiveList<>();
	public List<Class<?>> getDependencyClasses() { return this.dependencyClasses; }
	public void addDependencyClasses(Class<?>... dependencyClasses) { this.getDependencyClasses().addAll(Arrays.asList(dependencyClasses)); }

	// This is the writer classes scheduled to be used at setup.
	// We do not yet know if they are compatible with this Minecraft version.
	// All, some or none of them may fail.
	private List<Class<?>> writerClasses = new MassiveList<>();
	public List<Class<?>> getWriterClasses() { return this.writerClasses; }
	public void addWriterClasses(Class<?>... writerClasses) { this.getWriterClasses().addAll(Arrays.asList(writerClasses)); }
	
	// These are the actually functional child writers.
	// This list should only contain writers that passed the provoke routine.
	private List<WriterAbstract<CA, CB, ?, ?, ?, ?>> writers = new MassiveList<>();
	public List<WriterAbstract<CA, CB, ?, ?, ?, ?>> getWriters() { return this.writers; }

	// Here is the logic to perform the dependency and child writer setup.
	public void setupDependencies()
	{
		for (Class<?> dependencyClass : this.getDependencyClasses())
		{
			this.setupWriter(dependencyClass, false);
		}
	}
	
	public void setupWriters()
	{
		for (Class<?> writerClass : this.getWriterClasses())
		{
			this.setupWriter(writerClass, true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setupWriter(Class<?> writerClass, boolean add)
	{
		try
		{
			Class<WriterAbstract<?, ?, ?, ?, ?, ?>> writerClassInner = (Class<WriterAbstract<?, ?, ?, ?, ?, ?>>) writerClass;
			WriterAbstract<?, ?, ?, ?, ?, ?> writer = ReflectionUtil.getSingletonInstance(writerClassInner);
			
			if ( ! writer.isActive()) writer.setActive(this.getActivePlugin());
			
			if (add) this.getWriters().add((WriterAbstract<CA, CB, ?, ?, ?, ?>)writer);
		}
		catch (Throwable t)
		{
			this.reportSuccess(false, writerClass.getSimpleName(), t);
		}
	}
	
	public void reportSuccess(boolean success)
	{
		this.reportSuccess(success, this.getClass().getSimpleName());
	}
	
	public void reportSuccess(boolean success, String name)
	{
		this.reportSuccess(success, name, null);
	}
	
	public void reportSuccess(boolean success, String name, Throwable t)
	{
		if ( ! MassiveCoreMConf.get().debugWriters) return;
		
		// Create
		List<String> messages = new MassiveList<>();
		
		// Fill
		String message;
		
		// Main
		message = Txt.parse("<h>%s %s", name, TypeBoolean.getOn().getVisual(success));
		messages.add(message);
		
		// Throwable
		if (t != null)
		{
			message = Txt.parse("<b>### %s <i>%s", t.getClass().getSimpleName(), t.getMessage());
			messages.add(message);
			for (String s : MUtil.getStackTraceStrings(Arrays.asList(t.getStackTrace()), true))
			{
				message = Txt.parse("<b>--> %s", s);
				messages.add(message);
			}
		}
		
		// Send
		for (String s : messages)
		{
			this.getActivePlugin().log(s);
		}
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	// The setActive method starts out with the provoke.
	// This means it can fail immediately with a runtime exception.
	// If this happens it will not have been activated in any way.
	
	@Override
	public void setActive(boolean active)
	{
		if (active)
		{
			// Provoke
			this.provoke();
			
			// Setup Dependencies
			this.setupDependencies();
			
			// Report This Success
			this.reportSuccess(true);
			
			// Setup Writers
			this.setupWriters();
		}
		super.setActive(active);
	}

	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //

	public abstract CA createA();

	public abstract CB createB();

	// -------------------------------------------- //
	// CLASSES
	// -------------------------------------------- //

	private Class<CA> classA = null;

	public Class<CA> getClassA()
	{
		return this.classA;
	}

	public void setClassA(Class<CA> classA)
	{
		this.classA = classA;
	}

	private Class<CB> classB = null;

	public Class<CB> getClassB()
	{
		return this.classB;
	}

	public void setClassB(Class<CB> classB)
	{
		this.classB = classB;
	}

	// -------------------------------------------- //
	// MORPH
	// -------------------------------------------- //

	@SuppressWarnings("unchecked")
	public CA morphA(OA oa)
	{
		Class<CA> classA = this.getClassA();
		if (classA != null && !classA.isAssignableFrom(oa.getClass())) return null;
		CA ca = (CA) oa;
		return ca;
	}

	@SuppressWarnings("unchecked")
	public CB morphB(OB ob)
	{
		Class<CB> classB = this.getClassB();
		if (classB != null && !classB.isAssignableFrom(ob.getClass())) return null;
		CB cb = (CB) ob;
		return cb;
	}

	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //

	public Object provoke()
	{
		// Create Instances
		CA ia = this.createA();
		CB ib = this.createB();

		// Demand Set
		if (ia == null) throw new NullPointerException("Couldn't Create A");
		if (ib == null) throw new NullPointerException("Couldn't Create B");

		// Use Access
		FA fa = this.getA(ia);
		this.setA(ia, fa);

		FB fb = this.getB(ib);
		this.setB(ib, fb);

		// Use To
		this.toA(fb);
		this.toB(fa);

		// Return
		return null;
	}

	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //

	public void write(OA oa, OB ob, boolean a2b)
	{
		if ( ! this.isActive()) throw new IllegalStateException("not active " + this.getClass().getName());

		if (oa == null) throw new NullPointerException("oa");
		if (ob == null) throw new NullPointerException("ob");

		CA ca = this.morphA(oa);
		if (ca == null) return;

		CB cb = this.morphB(ob);
		if (cb == null) return;

		this.writeInner(oa, ob, ca, cb, a2b);
	}

	public void writeInner(OA oa, OB ob, CA ca, CB cb, boolean a2b)
	{
		for (WriterAbstract<CA, CB, ?, ?, ?, ?> writer : this.getWriters())
		{
			writer.write(ca, cb, a2b);
		}

		if (a2b)
		{
			FA fa = getA(ca);
			FB fb = toB(fa);
			setB(cb, fb);
		}
		else
		{
			FB fb = getB(cb);
			FA fa = toA(fb);
			setA(ca, fa);
		}
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	public FA getA(CA ca)
	{
		return null;
	}

	public void setA(CA ca, FA fa)
	{

	}

	public FB getB(CB cb)
	{
		return null;
	}

	public void setB(CB cb, FB fb)
	{

	}

	// -------------------------------------------- //
	// CONVERT
	// -------------------------------------------- //
	
	private Converter<? super FA, FB> converterTo = ConverterDefault.get();
	public Converter<? super FA, FB> getConverterTo() { return this.converterTo; }
	public void setConverterTo(Converter<? super FA, FB> converterTo) { this.converterTo = converterTo; }
	
	private Converter<? super FB, FA> converterFrom = ConverterDefault.get();
	public Converter<? super FB, FA> getConverterFrom() { return this.converterFrom; }
	public void setConverterFrom(Converter<? super FB, FA> converterFrom) { this.converterFrom = converterFrom; }

	public FA toA(FB fb)
	{
		return this.getConverterFrom().convert(fb);
	}

	public FB toB(FA fa)
	{
		return this.getConverterTo().convert(fa);
	}

}
