package com.massivecraft.massivecore.ps;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

public class PSFormatAbstract implements PSFormat
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String strNull;
	private final String strStart;
	
	private final boolean useWorldDisplayname;
	private final boolean useWorldAlias;
	
	private final String formatWorld;
	private final String formatBlockX;
	private final String formatBlockY;
	private final String formatBlockZ;
	private final String formatLocationX;
	private final String formatLocationY;
	private final String formatLocationZ;
	private final String formatChunkX;
	private final String formatChunkZ;
	private final String formatPitch;
	private final String formatYaw;
	private final String formatVelocityX;
	private final String formatVelocityY;
	private final String formatVelocityZ;
	
	private final String strGlue;
	private final String strStop;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PSFormatAbstract(String strNull, String strStart, boolean useWorldDisplayname, boolean useWorldAlias, String formatWorld, String formatBlockX, String formatBlockY, String formatBlockZ, String formatLocationX, String formatLocationY, String formatLocationZ, String formatChunkX, String formatChunkZ, String formatPitch, String formatYaw, String formatVelocityX, String formatVelocityY, String formatVelocityZ, String strGlue, String strStop)
	{
		this.strNull = strNull;
		this.strStart = strStart;
		this.useWorldDisplayname = useWorldDisplayname;
		this.useWorldAlias = useWorldAlias;
		this.formatWorld = formatWorld;
		this.formatBlockX = formatBlockX;
		this.formatBlockY = formatBlockY;
		this.formatBlockZ = formatBlockZ;
		this.formatLocationX = formatLocationX;
		this.formatLocationY = formatLocationY;
		this.formatLocationZ = formatLocationZ;
		this.formatChunkX = formatChunkX;
		this.formatChunkZ = formatChunkZ;
		this.formatPitch = formatPitch;
		this.formatYaw = formatYaw;
		this.formatVelocityX = formatVelocityX;
		this.formatVelocityY = formatVelocityY;
		this.formatVelocityZ = formatVelocityZ;
		this.strGlue = strGlue;
		this.strStop = strStop;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String format(PS ps)
	{
		if (ps == null) return this.strNull;
		
		List<String> entries = this.formatEntries(ps);
		
		return this.strStart + Txt.implode(entries, this.strGlue) + this.strStop;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public List<String> formatEntries(PS ps)
	{
		List<String> ret = new ArrayList<String>();
		
		Object val = null;
		
		val = ps.getWorld();
		if (val != null)
		{
			if (this.useWorldDisplayname)
			{
				val = Mixin.getWorldDisplayName(val.toString());
			}
			else if (this.useWorldAlias)
			{
				val = Mixin.getWorldAliasOrId(val.toString());
			}
			ret.add(String.format(this.formatWorld, val));
		}
		
		val = ps.getBlockX();
		if (val != null) ret.add(String.format(this.formatBlockX, val));
		
		val = ps.getBlockY();
		if (val != null) ret.add(String.format(this.formatBlockY, val));
		
		val = ps.getBlockZ();
		if (val != null) ret.add(String.format(this.formatBlockZ, val));
		
		val = ps.getLocationX();
		if (val != null) ret.add(String.format(this.formatLocationX, val));
		
		val = ps.getLocationY();
		if (val != null) ret.add(String.format(this.formatLocationY, val));
		
		val = ps.getLocationZ();
		if (val != null) ret.add(String.format(this.formatLocationZ, val));
		
		val = ps.getChunkX();
		if (val != null) ret.add(String.format(this.formatChunkX, val));
		
		val = ps.getChunkZ();
		if (val != null) ret.add(String.format(this.formatChunkZ, val));
		
		val = ps.getPitch();
		if (val != null) ret.add(String.format(this.formatPitch, val));
		
		val = ps.getYaw();
		if (val != null) ret.add(String.format(this.formatYaw, val));
		
		val = ps.getVelocityX();
		if (val != null) ret.add(String.format(this.formatVelocityX, val));
		
		val = ps.getVelocityY();
		if (val != null) ret.add(String.format(this.formatVelocityY, val));
		
		val = ps.getVelocityZ();
		if (val != null) ret.add(String.format(this.formatVelocityZ, val));

		return ret;
	}
	

}
