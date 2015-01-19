package com.massivecraft.massivecore.pager;

import java.util.Collection;
import java.util.List;

public interface Pager<T>
{
	// -------------------------------------------- //
	// DATA SUPPLY
	// -------------------------------------------- //
	
	public Collection<? extends T> getItems();
	public int getItemsPerPage();
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public int size();
	public boolean isValid(int number);
	public boolean isEmpty();	
	public List<T> get(int number);
	
	// -------------------------------------------- //
	// TXT
	// -------------------------------------------- //
	
	public String getMessageEmpty();
	public String getMessageInvalid();
	public List<String> getPageTxt(int number, String title, Stringifier<T> stringifier);
	
}
