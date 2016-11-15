package com.massivecraft.massivecore;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.event.EventMassiveCoreLog;
import com.massivecraft.massivecore.mixin.MixinLog;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.logging.Level;

public class Log
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String FIELD_TIMESTAMP = "timestamp";
	public static final String FIELD_PLUGIN = "plugin";
	public static final String FIELD_PLAYER_OBJECT = "player_object";
	public static final String FIELD_LEVEL = "level";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_MESSAGE = "message";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Map<String, Object> fields = new MassiveMap<>();
	public Map<String, Object> getFields() { return this.fields; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Log()
	{
		this
		.timestamp(System.currentTimeMillis())
		;
	}
	
	// -------------------------------------------- //
	// ACCESS > RAW
	// -------------------------------------------- //
	
	public Log set(String key, Object value)
	{
		if (value != null)
		{
			this.getFields().put(key, value);
		}
		else
		{
			this.getFields().remove(key);
		}
		
		return this;
	}
	
	public <T> T get(String key)
	{
		return (T) this.getFields().get(key);
	}
	
	// -------------------------------------------- //
	// ACCESS > SUGAR
	// -------------------------------------------- //
	
	public Log timestamp(Long timestamp) { return this.set(FIELD_TIMESTAMP, timestamp); }
	public Long timestamp() { return this.get(FIELD_TIMESTAMP); }
	
	public Log plugin(Plugin plugin) { return this.set(FIELD_PLUGIN, plugin); }
	public Plugin plugin() { return this.get(FIELD_PLUGIN); }
	
	public Log playerObject(Object playerObject) { return this.set(FIELD_PLAYER_OBJECT, playerObject); }
	public Object playerObject() { return this.get(FIELD_PLAYER_OBJECT); }
	
	public Log level(Level level) { return this.set(FIELD_LEVEL, level); }
	public Level level() { return this.get(FIELD_LEVEL); }
	
	public Log type(String category) { return this.set(FIELD_TYPE, category); }
	public String type() { return this.get(FIELD_TYPE); }
	
	public Log message(String message) { return this.set(FIELD_MESSAGE, message); }
	public String message() { return this.get(FIELD_MESSAGE); }
	
	// -------------------------------------------- //
	// SEND
	// -------------------------------------------- //
	
	public void send()
	{
		// Run Event
		EventMassiveCoreLog event = new EventMassiveCoreLog(this);
		event.run();
		if (event.isCancelled()) return;
		
		// Send Through Mixin
		MixinLog.get().send(this);
	}
	
}
