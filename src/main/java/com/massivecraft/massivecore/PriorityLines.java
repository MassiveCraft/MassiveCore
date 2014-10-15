package com.massivecraft.massivecore;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.massivecraft.massivecore.Prioritized;
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
	public void setLines(List<String> lines) { this.lines = lines;  }
	public void setLines(String... lines) { this.setLines(Arrays.asList(lines)); }

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
	// COMPARABLE
	// -------------------------------------------- //
	
	@Override
	public int compareTo(PriorityLines that)
	{
		int ret;
		
		ret = Integer.valueOf(this.priority).compareTo(that.priority);
		if (ret != 0) return ret;
		
		if (MUtil.equals(this.lines, that.lines)) return 0;
		ret = HashCodeComparator.get().compare(this.lines, that.lines);
		if (ret != 0) return ret;
		
		return ret;
	}
	
	// -------------------------------------------- //
	// HASH CODE & EQUALS
	// -------------------------------------------- //
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lines == null) ? 0 : lines.hashCode());
		result = prime * result + priority;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof PriorityLines)) return false;
		PriorityLines other = (PriorityLines) obj;
		if (lines == null)
		{
			if (other.lines != null) return false;
		}
		else if (!lines.equals(other.lines)) return false;
		if (priority != other.priority) return false;
		return true;
	}

}
