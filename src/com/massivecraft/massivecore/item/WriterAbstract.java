package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.Arrays;
import java.util.List;

// A = What we are writing from.
// B = What we are writing to.
// 
// O = The base object. Not yet morphed into specific class.
// C = The specific class object. Often the same as O but at times very different.
// F = The field class. Used for field writing.
//
// D = The data class. Used for sending arbitrary data along.
//
// TIP: Simply set to Object if you are not using a certain generic.
//
public abstract class WriterAbstract<OA, OB, CA, CB, FA, FB, D> extends Engine
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstract(Class<CA> classCA, Class<CB> classCB)
	{
		this.classCA = classCA;
		this.classCB = classCB;
	}
	
	// -------------------------------------------- //
	// WRITERS
	// -------------------------------------------- //
	// Writer have dependencies and child writers.
	
	// A dependency is another writer that must be successfully activated for this writer to function.
	// For that reason the dependencies are activated just after the setup logic.
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
	// This list should only contain writers that passed the setup routine.
	private List<WriterAbstract<CA, CB, ?, ?, ?, ?, D>> writers = new MassiveList<>();
	public List<WriterAbstract<CA, CB, ?, ?, ?, ?, D>> getWriters() { return this.writers; }

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
			WriterAbstract<?, ?, ?, ?, ?, ?, ?> writer = ReflectionUtil.getSingletonInstance(writerClass);
			
			if ( ! writer.isActive()) writer.setActive(this.getActivePlugin());
			
			if (add) this.getWriters().add((WriterAbstract<CA, CB, ?, ?, ?, ?, D>)writer);
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
		if ( ! MassiveCoreMConf.get().debugEnabled) return;
		
		// Create
		List<String> messages = new MassiveList<>();
		
		// Fill
		String message;
		
		// Main
		message = Txt.parse("<h>%s %s", name, TypeBooleanOn.get().getVisual(success));
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
	
	@Override
	public void setActive(boolean active)
	{
		if (active)
		{
			// Setup Self
			this.setupSelf();
			
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

	public OA createOA()
	{
		return null;
	}

	public OB createOB()
	{
		return null;
	}
	
	// -------------------------------------------- //
	// CLASSES
	// -------------------------------------------- //
	
	private final Class<CA> classCA;
	public Class<CA> getClassCA() { return this.classCA; }

	private final Class<CB> classCB;
	public Class<CB> getClassCB() { return this.classCB; }

	// -------------------------------------------- //
	// MORPH
	// -------------------------------------------- //
	// Per default the morph is just a secure cast.
	// If the morph fails it means the writer was not applicable.
	// In those cases we don't want a crash.
	// We rather silently continue.

	@SuppressWarnings("unchecked")
	public CA morphA(OA oa)
	{
		if (this.getClassCA() != null && ! this.getClassCA().isAssignableFrom(oa.getClass())) return null;
		return (CA)oa;
	}

	@SuppressWarnings("unchecked")
	public CB morphB(OB ob)
	{
		if (this.getClassCB() != null && ! this.getClassCB().isAssignableFrom(ob.getClass())) return null;
		return (CB)ob;
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setupSelf()
	{
		// Create O
		OA oa = this.createOA();
		OB ob = this.createOB();

		// Demand O
		if (oa == null) throw new NullPointerException("Couldn't Create OA");
		if (ob == null) throw new NullPointerException("Couldn't Create OB");
		
		// Morph
		CA ca = this.morphA(oa);
		CB cb = this.morphB(ob);
		
		// Demand C
		if (ca == null) throw new NullPointerException("Couldn't Create CA");
		if (cb == null) throw new NullPointerException("Couldn't Create CB");
		
		// Write (to provoke extra much)
		this.write(oa, ob, true);
		this.write(oa, ob, false);
	}

	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //

	public void write(OA oa, OB ob, boolean a2b)
	{
		this.write(oa, ob, null, a2b);
	}
	
	public void write(OA oa, OB ob, D d, boolean a2b)
	{
		// if ( ! this.isActive()) throw new IllegalStateException("not active " + this.getClass().getName());

		if (oa == null) throw new NullPointerException("oa");
		if (ob == null) throw new NullPointerException("ob");
		
		CA ca = this.morphA(oa);
		if (ca == null) return;

		CB cb = this.morphB(ob);
		if (cb == null) return;

		this.writeInner(oa, ob, ca, cb, d, a2b);
	}

	public void writeInner(OA oa, OB ob, CA ca, CB cb, D d, boolean a2b)
	{
		for (WriterAbstract<CA, CB, ?, ?, ?, ?, D> writer : this.getWriters())
		{
			writer.write(ca, cb, d, a2b);
		}

		if (a2b)
		{
			FA fa = getA(ca, d);
			FB fb = toB(fa);
			setB(cb, fb, d);
		}
		else
		{
			FB fb = getB(cb, d);
			FA fa = toA(fb);
			setA(ca, fa, d);
		}
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	public FA getA(CA ca, D d)
	{
		return null;
	}

	public void setA(CA ca, FA fa, D d)
	{

	}

	public FB getB(CB cb, D d)
	{
		return null;
	}

	public void setB(CB cb, FB fb, D d)
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
