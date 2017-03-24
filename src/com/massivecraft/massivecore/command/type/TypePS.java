package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.command.type.primitive.TypeFloat;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSBuilder;
import com.massivecraft.massivecore.ps.PSFormatDesc;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypePS extends TypeAbstract<PS>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePS i = new TypePS();
	public static TypePS get() { return i; }
	public TypePS() { super(PS.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(PS value, CommandSender sender)
	{
		// TODO: Slug did not work... will desc look better?
		return PSFormatDesc.get().format(value);
	}
	
	@Override
	public PS read(String arg, CommandSender sender) throws MassiveException
	{
		// Ellador 34 13 78.6 (standard one)
		// 34 13 79 (standard one)
		// pitch14.5
		// worldEllador,yaw14,
		// lx15,ly18,lz8003
		// lx15 ly32 lz99 wEllador
		// lx:15 ly:32 lz:99 w:Ellador
		
		// We get the sender ps (note only pitch, yaw, and world is implicit)
		PS defaultPs = PS.NULL;
		if (sender instanceof Entity)
		{
			Location loc = ((Entity) sender).getLocation();
			defaultPs = new PSBuilder()
			.world(PS.calcWorldName(loc.getWorld()))
			.pitch(loc.getPitch())
			.yaw(loc.getYaw())
			.build();
		}
		
		// We remove all commas optionally followed by spaces
		String argInner = arg.replaceAll("\\:\\s*", "");
		
		// We split on comma and space to get the list of raw entries.
		List<String> parts = Arrays.asList(argInner.split("[\\s,]+"));
		
		// Then we test the standard ones
		if (parts.size() == 4)
		{
			try
			{
				String world = TypeWorldId.get().read(parts.get(0), sender);
				double locationX = TypeDouble.get().read(parts.get(1), sender);
				double locationY = TypeDouble.get().read(parts.get(2), sender);
				double locationZ = TypeDouble.get().read(parts.get(3), sender);
				return new PSBuilder(defaultPs).world(world).locationX(locationX).locationY(locationY).locationZ(locationZ).build();
			}
			catch (Exception e)
			{
				
			}
			
			try
			{
				double locationX = TypeDouble.get().read(parts.get(0), sender);
				double locationY = TypeDouble.get().read(parts.get(1), sender);
				double locationZ = TypeDouble.get().read(parts.get(2), sender);
				String world = TypeWorldId.get().read(parts.get(3), sender);
				return new PSBuilder(defaultPs).world(world).locationX(locationX).locationY(locationY).locationZ(locationZ).build();
			}
			catch (Exception e)
			{
				
			}
		}
		else if (parts.size() == 3)
		{
			try
			{
				double locationX = TypeDouble.get().read(parts.get(0), sender);
				double locationY = TypeDouble.get().read(parts.get(1), sender);
				double locationZ = TypeDouble.get().read(parts.get(2), sender);
				return new PSBuilder(defaultPs).locationX(locationX).locationY(locationY).locationZ(locationZ).build();
			}
			catch (Exception e)
			{
				
			}
		}
		
		// Then we split each entry using known prefixes and append the ps builder.
		PSBuilder ret = new PSBuilder(defaultPs);
		boolean something = false;
		for (String part : parts)
		{
			String value;
			
			value = getValue(part, PS.NAME_SERIALIZED_WORLD, PS.NAME_FULL_WORLD);
			if (value != null)
			{
				ret.world(TypeWorldId.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_BLOCKX, PS.NAME_FULL_BLOCKX);
			if (value != null)
			{
				ret.blockX(TypeInteger.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_BLOCKY, PS.NAME_FULL_BLOCKY);
			if (value != null)
			{
				ret.blockY(TypeInteger.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_BLOCKZ, PS.NAME_FULL_BLOCKZ);
			if (value != null)
			{
				ret.blockZ(TypeInteger.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_LOCATIONX, PS.NAME_FULL_LOCATIONX);
			if (value != null)
			{
				ret.locationX(TypeDouble.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_LOCATIONY, PS.NAME_FULL_LOCATIONY);
			if (value != null)
			{
				ret.locationY(TypeDouble.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_LOCATIONZ, PS.NAME_FULL_LOCATIONZ);
			if (value != null)
			{
				ret.locationZ(TypeDouble.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_CHUNKX, PS.NAME_FULL_CHUNKX);
			if (value != null)
			{
				ret.chunkX(TypeInteger.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_CHUNKZ, PS.NAME_FULL_CHUNKZ);
			if (value != null)
			{
				ret.chunkZ(TypeInteger.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_PITCH, PS.NAME_FULL_PITCH);
			if (value != null)
			{
				ret.pitch(TypeFloat.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_YAW, PS.NAME_FULL_YAW);
			if (value != null)
			{
				ret.yaw(TypeFloat.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_VELOCITYX, PS.NAME_FULL_VELOCITYX);
			if (value != null)
			{
				ret.velocityX(TypeDouble.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_VELOCITYY, PS.NAME_FULL_VELOCITYY);
			if (value != null)
			{
				ret.velocityY(TypeDouble.get().read(value, sender));
				something = true;
			}
			
			value = getValue(part, PS.NAME_SERIALIZED_VELOCITYZ, PS.NAME_FULL_VELOCITYZ);
			if (value != null)
			{
				ret.velocityZ(TypeDouble.get().read(value, sender));
				something = true;
			}
		}
	
		if ( ! something)
		{
			throw new MassiveException().addMsg("<b>Invalid physical state \"<h>%s<b>\".", arg);
		}
		
		return ret.build();
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return null;
	}
	
	public static String getValue(String entry, String... prefixes)
	{
		for (String prefix : prefixes)
		{
			if ( ! StringUtils.startsWithIgnoreCase(entry, prefix)) continue;
			return entry.substring(prefix.length());
		}
		return null;
	}
	
}
