package com.massivecraft.mcore5.adapter;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore5.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore5.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore5.xlib.gson.JsonElement;
import com.massivecraft.mcore5.xlib.gson.JsonObject;
import com.massivecraft.mcore5.xlib.gson.JsonParseException;
import com.massivecraft.mcore5.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore5.xlib.gson.JsonSerializer;

public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{	
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String TYPE = "type";
	public static final String AMOUNT = "amount";
	public static final String DAMAGE = "damage";
	public static final String ENCHANTMENTS = "enchantments";
	public static final String TAG = "tag";
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(ItemStack itemStack, Type typeOfSrc, JsonSerializationContext context)
	{
		return toJson(itemStack);
	}
	
	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return fromJson(json);
	}
	
	// -------------------------------------------- //
	// JSON
	// -------------------------------------------- //
	
	public static JsonObject toJson(ItemStack stack)
	{
		// Check for "nothing"
		if (stack == null || stack.getTypeId() == 0 || stack.getAmount() == 0)
		{
			return null;
		}
		
		JsonObject jsonItemStack = new JsonObject();
		
		// Add type id
		jsonItemStack.addProperty(TYPE, stack.getTypeId());
		
		// Add amount
		if (stack.getAmount() != 1)
		{
			jsonItemStack.addProperty(AMOUNT, stack.getAmount());
		}
		
		// Add damage
		if (stack.getDurability() != 0) // Durability is a weird name since it is the amount of damage.
		{
			jsonItemStack.addProperty(DAMAGE, stack.getDurability());
		}
		
		// Add enchantments
		if (stack.getEnchantments().size() > 0)
		{
			JsonObject jsonEnchantments = new JsonObject();
			for (Entry<Enchantment, Integer> entry : stack.getEnchantments().entrySet())
			{
				jsonEnchantments.addProperty(String.valueOf(entry.getKey().getId()), entry.getValue());
			}
			jsonItemStack.add(ItemStackAdapter.ENCHANTMENTS, jsonEnchantments);
		}
		
		// Add the tag if there is one 
		JsonObject tag = getEnchFreeGsonTagFromItemStack(stack);
		if (tag != null)
		{
			jsonItemStack.add(TAG, tag);
		}
		
		return jsonItemStack;
	}
	
	// Used by method toJson
	public static JsonObject getEnchFreeGsonTagFromItemStack(ItemStack stack)
	{
		if (!(stack instanceof CraftItemStack)) return null;
		CraftItemStack craftItemStack = (CraftItemStack)stack;
		
		NBTTagCompound nbt = craftItemStack.getHandle().tag;
		if (nbt == null) return null;
		
		JsonObject gsonbt = (JsonObject) NbtGsonConverter.nbtToGsonVal(nbt);
		gsonbt.remove("ench");
		if (gsonbt.entrySet().size() == 0) return null;
		
		return gsonbt;		
	}
	
	public static ItemStack fromJson(JsonElement json)
	{
		// Check for "nothing"
		if (json == null || ! json.isJsonObject()) return null;
		
		JsonObject jsonItemStack = json.getAsJsonObject();
		
		// Populate values
		int type = 0; 
		int amount = 1;
		short damage = 0;
		
		if (jsonItemStack.has(TYPE))
		{
			type = jsonItemStack.get(TYPE).getAsInt();
		}
		
		if (jsonItemStack.has(AMOUNT))
		{
			amount = jsonItemStack.get(AMOUNT).getAsInt();
		}
		
		if (jsonItemStack.has(DAMAGE))
		{
			damage = jsonItemStack.get(DAMAGE).getAsShort();
		}
		
		// Create Non enchanted stack
		ItemStack stack = new ItemStack(type, amount, damage);
		
		// Add tag
		if (jsonItemStack.has(TAG))
		{
			JsonObject jsonbt = jsonItemStack.get(TAG).getAsJsonObject();
			
			CraftItemStack craftItemStack = new CraftItemStack(stack);
			stack = craftItemStack;
			
			NBTBase nbt = NbtGsonConverter.gsonValToNbt(jsonbt, null, NBType.COMPOUND, NBType.UNKNOWN);
			craftItemStack.getHandle().tag = (NBTTagCompound) nbt;
		}
		
		// Add enchantments if there are any
		if (jsonItemStack.has(ENCHANTMENTS))
		{
			JsonObject jsonEnchantments = jsonItemStack.get(ENCHANTMENTS).getAsJsonObject();
			for (Entry<String, JsonElement> enchantmentEntry: jsonEnchantments.entrySet())
			{
				int enchantmentId = Integer.valueOf(enchantmentEntry.getKey());
				Integer enchantmentLevel = Integer.valueOf(enchantmentEntry.getValue().getAsString());
				stack.addUnsafeEnchantment(Enchantment.getById(enchantmentId), enchantmentLevel);
			}
		}
		
		return stack;
	}
}
