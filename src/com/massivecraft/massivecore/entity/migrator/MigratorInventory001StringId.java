package com.massivecraft.massivecore.entity.migrator;

import com.massivecraft.massivecore.adapter.AdapterInventory;
import com.massivecraft.massivecore.item.DataItemStack;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;
import com.massivecraft.massivecore.store.migrator.MigratorUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.Inventory;

import java.util.Map.Entry;
import java.util.Set;

public class MigratorInventory001StringId extends MigratorRoot
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final Set<String> NAMES_EXTRA_FIELDS = MUtil.set(
		AdapterInventory.HELMET,
		AdapterInventory.CHESTPLATE,
		AdapterInventory.LEGGINGS,
		AdapterInventory.BOOTS,
		AdapterInventory.SHIELD
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MigratorInventory001StringId i = new MigratorInventory001StringId();
	public static MigratorInventory001StringId get() { return i; }
	private MigratorInventory001StringId()
	{
		super(Inventory.class);
	}
	
	// -------------------------------------------- //
	// CONVERSION
	// -------------------------------------------- //
	
	@Override
	public void migrateInner(JsonObject json)
	{
		for (Entry<String, JsonElement> entry : json.entrySet())
		{
			String name = entry.getKey();
			boolean updateable = NAMES_EXTRA_FIELDS.contains(name) || StringUtils.isNumeric(name);
			
			if (!updateable) continue;
			
			updateField(json, name);
		}
	}
	
	public void updateField(JsonObject json, String name)
	{
		JsonElement element = json.get(name);
		if (element == null) return;
		if (!element.isJsonObject()) return;
		JsonObject object = element.getAsJsonObject();
		MigratorUtil.migrate(DataItemStack.class, object);
	}
	
}
