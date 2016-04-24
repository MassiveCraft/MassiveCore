package com.massivecraft.massivecore.item;

import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;

public abstract class WriterAbstract<OA, OB, CA, CB, FA, FB> extends Engine
{
	// -------------------------------------------- //
	// WRITERS
	// -------------------------------------------- //
	// A writer may contain subwriters.

	private List<WriterAbstract<CA, CB, ?, ?, ?, ?>> writers = new MassiveList<>();

	public List<WriterAbstract<CA, CB, ?, ?, ?, ?>> getWriters()
	{
		return this.writers;
	}

	public void clearWriters()
	{
		this.writers.clear();
	}

	@SuppressWarnings("unchecked")
	public void addWriter(Class<?> clazz)
	{
		boolean success = false;
		try
		{
			Class<WriterAbstract<CA, CB, ?, ?, ?, ?>> clazzInner = (Class<WriterAbstract<CA, CB, ?, ?, ?, ?>>) clazz;
			WriterAbstract<CA, CB, ?, ?, ?, ?> writer = ReflectionUtil.getSingletonInstance(clazzInner);
			writer.setActive(this.getActivePlugin());
			this.getWriters().add(writer);
			success = true;
		}
		catch (Throwable t)
		{
			if (MassiveCoreMConf.get().debugWriters)
			{
				t.printStackTrace();
			}
		}

		if (MassiveCoreMConf.get().debugWriters)
		{
			String message = Txt.parse("<h>%s %s", clazz.getSimpleName(), TypeBoolean.getOn().getVisual(success));
			this.getActivePlugin().log(message);
		}
	}

	public void addWriters(Class<?>... clazzs)
	{
		for (Class<?> clazz : clazzs)
		{
			this.addWriter(clazz);
		}
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
	// ACTIVE
	// -------------------------------------------- //

	@Override
	public void setActive(boolean active)
	{
		this.provoke();
		super.setActive(active);
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
		if (!this.isActive()) throw new IllegalStateException("not active " + this.getClass().getName());

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
