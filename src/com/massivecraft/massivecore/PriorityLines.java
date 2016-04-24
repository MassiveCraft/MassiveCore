package com.massivecraft.massivecore;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.massivecraft.massivecore.comparator.ComparatorIdentity;
import com.massivecraft.massivecore.comparator.ComparatorPrioritized;
import com.massivecraft.massivecore.util.MUtil;

public class PriorityLines implements Prioritized, Comparable<PriorityLines>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private int priority;
	@Override public int getPriority() { return this.priority; }
	public void setPriority(int priority) { this.priority = priority; }
	
	private List<String> lines;
	public List<String> getLines() { return this.lines; }
	public void setLines(List<String> lines) { this.lines = lines; }
	public void setLines(String... lines) { this.setLines(Arrays.asList(lines)); }
	public void setLines(String line) { this.setLines(Collections.singletonList(line)); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PriorityLines(int priority, List<String> lines)
	{
		this.priority = priority;
		this.lines = lines;
	}
	
	public PriorityLines(int priority, String... lines)
	{
		this(priority, Arrays.asList(lines));
	}
	
	public PriorityLines(int priority)
	{
		this(priority, new LinkedList<String>());
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
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof PriorityLines)) return false;
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
	
	

}
