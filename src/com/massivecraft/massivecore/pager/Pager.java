package com.massivecraft.massivecore.pager;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pager<T>
{
	// -------------------------------------------- //
	// DATA SUPPLY
	// -------------------------------------------- //
	
	// The command to use for back and forward buttons.
	protected MassiveCommand command = null;
	public boolean hasCommand() { return this.command != null; }
	public Pager<T> setCommand(MassiveCommand command) { this.command = command; return this; }
	public MassiveCommand getCommand() { return this.command; }
	
	// The CommandSender for fallback height.
	protected CommandSender sender = null;
	public boolean hasSender() { return this.sender != null; }
	public Pager<T> setSender(CommandSender sender) { this.sender = sender; return this; }
	public CommandSender getSender() { return this.sender; }
	public CommandSender getSenderCalc()
	{
		CommandSender ret = this.getSender();
		if (ret != null) return ret;
		
		MassiveCommand command = this.getCommand();
		if (command != null) return command.sender;
		
		return null;		
	}
	
	// The args to use for back and forward buttons.
	protected List<String> args = null;
	public boolean hasArgs() { return this.args != null; }
	public Pager<T> setArgs(List<String> args) { this.args = args; return this; }
	public List<String> getArgs() { return this.args; }
	public List<String> getArgsCalc()
	{
		List<String> ret = this.getArgs();
		if (ret != null) return ret;
		
		MassiveCommand command = this.getCommand();
		if (command != null) return new ArrayList<String>(command.getArgs());
		
		return null;
	}
	
	// The page height. The asmount of items per page.
	protected Integer height = null;
	public boolean hasHeight() { return this.height != null; }
	public Pager<T> setHeight(Integer height) { this.height = height; return this; }
	public Integer getHeight() { return this.height; }
	public Integer getHeightCalc()
	{
		Integer ret = this.getHeight();
		if (ret != null) return ret;
		
		CommandSender sender = this.getSenderCalc();
		if (sender == null) return Txt.PAGEHEIGHT_PLAYER;
		if (sender instanceof Player) return Txt.PAGEHEIGHT_PLAYER;
		
		return Txt.PAGEHEIGHT_CONSOLE;
	}
	
	// The title to use at the top of the page.
	protected String title = null;
	public boolean hasTitle() { return this.title != null; }
	public Pager<T> setTitle(String title) { this.title = title; return this; }
	public String getTitle() { return this.title; }
	
	// The page number we want to show.
	protected Integer number = null;
	public boolean hasNumber() { return this.number != null; }
	public Pager<T> setNumber(Integer number) { this.number = number; return this; }
	public Integer getNumber() { return this.number; }
	
	// The items we are paging.
	protected Collection<? extends T> items = null;
	public boolean hasItems() { return this.items != null; }
	public Pager<T> setItems(Collection<? extends T> items) { this.items = items; return this; }
	public Collection<? extends T> getItems() { return this.items; }
	
	// The method of converting from item to Mson.
	protected Msonifier<T> msonifier = null;
	public boolean hasMsonifier() { return this.msonifier != null; }
	public Pager<T> setMsonifier(Msonifier<T> msonifier) { this.msonifier = msonifier; return this; }
	public Pager<T> setMsonifier(final Stringifier<T> stringifier) { this.msonifier = new Msonifier<T>(){
		@Override
		public Mson toMson(T item, int index)
		{
			return Mson.fromParsedMessage(stringifier.toString(item, index));
		}
	}; return this; }
	public Msonifier<T> getMsonifier() { return this.msonifier; }

	// -------------------------------------------- //
	// CALC
	// -------------------------------------------- //
	
	public void calc()
	{
		this.setSender(this.getSenderCalc());
		this.setArgs(this.getArgsCalc());
		this.setHeight(this.getHeightCalc());
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Pager()
	{
		
	}
	
	public Pager(MassiveCommand command, String title, Integer number, Stringifier<T> stringifier)
	{
		this(command, title, number, null, stringifier);
	}
	
	public Pager(MassiveCommand command, String title, Integer number, Collection<? extends T> items, Stringifier<T> stringifier)
	{
		this(command, title, number, items);
		this.setMsonifier(stringifier);
	}
	
	public Pager(MassiveCommand command, String title, Integer number)
	{
		this(command, title, number, (Collection<? extends T>)null);
	}
	
	public Pager(MassiveCommand command, String title, Integer number, Collection<? extends T> items)
	{
		this(command, title, number, items, (Msonifier<T>)null);
	}
	
	public Pager(MassiveCommand command, String title, Integer number, Msonifier<T> msonifier)
	{
		this(command, title, number, null, msonifier);
	}
	
	public Pager(MassiveCommand command, String title, Integer number, Collection<? extends T> items, Msonifier<T> msonifier)
	{
		this.command = command;
		this.title = title;
		this.number = number;
		this.items = items;
		this.msonifier = msonifier;
		this.calc();
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public int size()
	{
		return (int) Math.ceil((double) this.getItems().size() / this.getHeight());
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
	public List<T> getPage(int number)
	{
		// Return null if the page number is invalid
		if ( ! this.isValid(number)) return null;
		
		// Forge list from collection
		List<T> items = null;
		if (this.getItems() instanceof List)
		{
			items = (List<T>) this.getItems();
		}
		else
		{
			items = new ArrayList<T>(this.getItems());
		}
		
		int index = number - 1;
		
		// Calculate from and to
		int from = index * this.getHeight();
		int to = from + this.getHeight();
		if (to > items.size())
		{
			to = items.size();
		}
		
		// Pick them
		return items.subList(from, to);
	}
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public List<Mson> get()
	{
		// Create ret
		List<Mson> ret = new ArrayList<Mson>();
		
		// Add title
		ret.add(Txt.titleizeMson(this.getTitle(), this.size(), this.getNumber(), this.getCommand(), this.getArgs()));
		
		// Check empty
		if (this.isEmpty())
		{
			ret.add(Txt.getMessageEmpty());
			return ret;
		}
		
		// Get items
		List<T> pageItems = this.getPage(this.getNumber());
		
		// Check invalid
		if (pageItems == null)
		{
			ret.add(Txt.getMessageInvalid(this.size()));
			return ret;
		}
		
		// Add items
		int index = (this.getNumber() - 1) * this.getHeight();
		for (T pageItem : pageItems)
		{
			ret.add(this.getMsonifier().toMson(pageItem, index));
			index++;
		}
		
		// Return ret
		return ret;
	}
	
	// -------------------------------------------- //
	// MESSAGE
	// -------------------------------------------- //
	
	public void message()
	{
		// Get
		List<Mson> messages = this.get();
		
		// Message
		Mixin.messageOne(this.getSender(), messages);
	}
	
	public void messageAsync()
	{
		Bukkit.getScheduler().runTaskAsynchronously(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				message();				
			}
		});
	}
	
}
