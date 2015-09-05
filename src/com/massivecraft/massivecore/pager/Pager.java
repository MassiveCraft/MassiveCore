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
	// MSON
	// -------------------------------------------- //
	
	public List<Mson> getPage(int number, String title, MassiveCommand command, List<String> args, Msonifier<T> msonifier);
	public List<Mson> getPage(int number, String title, MassiveCommand command, Msonifier<T> msonifier);
	
	// -------------------------------------------- //
	// STRING
	// -------------------------------------------- //
	
	public List<Mson> getPage(int number, String title, MassiveCommand command, List<String> args, Stringifier<T> stringifier);
	public List<Mson> getPage(int number, String title, MassiveCommand command, Stringifier<T> stringifier);

}
