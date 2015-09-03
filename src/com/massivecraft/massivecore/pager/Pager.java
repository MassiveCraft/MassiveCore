package com.massivecraft.massivecore.pager;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.mson.Mson;

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
	
	// -------------------------------------------- //
	// MSON
	// -------------------------------------------- //
	
	public List<Mson> getPageMson(int number, String title, MassiveCommand command, List<String> args, Msonifier<T> msonifier);
	public List<Mson> getPageMson(int number, String title, MassiveCommand command, Msonifier<T> msonifier);

}
