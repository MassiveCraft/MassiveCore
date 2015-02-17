package com.massivecraft.massivecore.pager;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.util.Txt;

public abstract class PagerAbstract<T> implements Pager<T>
{
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public int size()
	{
		return (int) Math.ceil((double) this.getItems().size() / this.getItemsPerPage());
	}
	
	public boolean isValid(int number)
	{
		if (this.isEmpty()) return false;
		if (number < 1) return false;
		if (number > this.size()) return false;
		return true;
	}
	
	public boolean isEmpty()
	{
		return this.getItems().isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> get(int number)
	{
		// Return null if the page number is invalid
		if (!this.isValid(number)) return null;
		
		// Create return value
		List<T> ret = new ArrayList<T>();
		
		// Forge list from collection
		List<T> items = null;
		if (this.getItems() instanceof List)
		{
			items = (List<T>)this.getItems();
		}
		else
		{
			items = new ArrayList<T>(this.getItems());
		}
		
		int index = number - 1;
		
		// Calculate from and to
		int from = index * this.getItemsPerPage();
		int to = from + this.getItemsPerPage();
		if (to > items.size())
		{
			to = items.size();
		}
		
		// Pick them
		ret.addAll(items.subList(from, to));
		
		// Return return value
		return ret;
	}
	
	// -------------------------------------------- //
	// TXT
	// -------------------------------------------- //
	
	public String getMessageEmpty()
	{
		return Txt.parse("<i>Sorry, no pages available.");
	}
	
	public String getMessageInvalid()
	{
		if (this.size() == 0)
		{
			return this.getMessageEmpty();
		}
		else if (this.size() == 1)
		{
			return Txt.parse("<b>Invalid, there is only one page.", this.size());
		}
		else
		{
			return Txt.parse("<b>Invalid, page must be between 1 and %d.", this.size());
		}
	}
	
	@Override
	public List<String> getPageTxt(int number, String title, Stringifier<T> stringifier)
	{
		List<String> ret = new ArrayList<String>();
		
		ret.add(Txt.titleize(title + Txt.parse("<a>") + " " + number + "/" + this.size()));
		
		if (this.isEmpty())
		{
			ret.add(this.getMessageEmpty());
			return ret;
		}
		
		List<T> pageItems = this.get(number);
		
		if (pageItems == null)
		{
			ret.add(this.getMessageInvalid());
			return ret;
		}
		
		int index = (number - 1) * this.getItemsPerPage();
		for (T pageItem : pageItems)
		{
			if (stringifier != null)
			{
				ret.add(stringifier.toString(pageItem, index));
			}
			else
			{
				ret.add(pageItem.toString());
			}
			index++;
		}
		
		return ret;
	}
	
	
}
