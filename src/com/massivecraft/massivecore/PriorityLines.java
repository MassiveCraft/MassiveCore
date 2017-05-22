package com.massivecraft.massivecore;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.comparator.ComparatorIdentity;
import com.massivecraft.massivecore.comparator.ComparatorPrioritized;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.FlattenUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PriorityLines implements Prioritized, Comparable<PriorityLines>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private int priority;
	@Override public int getPriority() { return this.priority; }
	public void setPriority(int priority) { this.priority = priority; }
	
	private List<Mson> lines;
	public List<String> getLines() { return Mson.toPlain(this.lines, true); }
	public void setLines(Object... lines)
	{
		Collection<Object> linesList = FlattenUtil.flatten(lines);
		List<Mson> msonLines = new MassiveList<>();
		
		// Extract the items from the array
		for (Object line: linesList)
		{
			msonLines.add(ensureMson(line));
		}
		this.lines = msonLines;
	}
	
	public void setLine(Object line) { this.setLines(line); };
	public List<Mson> getLinesMson() { return new MassiveList<>(this.lines); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PriorityLines(int priority, Object... lines)
	{
		this.priority = priority;
		this.setLines(lines);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return MassiveCore.gson.toJson(this);
	}
	
	// -------------------------------------------- //
	// COMPARABLE & EQUALS & HASHCODE
	// -------------------------------------------- //
	
	@Override
	public int compareTo(PriorityLines that)
	{
		// Create
		int ret = 0;
		
		// Fill
		ret = ComparatorPrioritized.get().compare(this, that);
		if (ret != 0) return ret;
		
		ret = ComparatorIdentity.get().compare(this, that);
		
		// Return
		return ret;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof PriorityLines)) return false;
		PriorityLines that = (PriorityLines)object;
		return MUtil.equals(
			this.getPriority(), that.getPriority(),
			this.getLines(), that.getLines()
		);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.getPriority(),
			this.getLines()
		);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private static Mson ensureMson(Object messageItem)
	{
		if (messageItem instanceof Mson) return (Mson) messageItem;
		if (messageItem instanceof String) return Mson.fromParsedMessage((String) messageItem);
		throw new IllegalArgumentException(String.format("messageItem is neither an Mson or String, %s", messageItem));
	}
	
}
