package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.mixin.MixinVisibility;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.command.CommandSender;

import java.lang.ref.WeakReference;

public class PredicateVisibleTo implements Predicate<Object>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final WeakReference<CommandSender> watcher;
	public CommandSender getWatcher() { return this.watcher.get(); }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static PredicateVisibleTo get(Object watcherObject) { return new PredicateVisibleTo(watcherObject); }
	public PredicateVisibleTo(Object watcherObject)
	{
		this.watcher = new WeakReference<>(IdUtil.getSender(watcherObject));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(Object watcheeObject)
	{
		return MixinVisibility.get().isVisible(watcheeObject, this.getWatcher());
	}

}
