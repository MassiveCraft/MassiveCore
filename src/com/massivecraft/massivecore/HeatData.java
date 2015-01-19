package com.massivecraft.massivecore;

public final class HeatData
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final transient double HEAT_MIN = 0D;
	public static final transient double HEAT_HIGH = 1D;
	public static final transient long MILLIS_CALC_EPSILON = 50;
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	private final double heat;
	public double getHeat() { return this.heat; }
	
	private final long lastCalcMillis;
	public long getLastCalcMillis() { return this.lastCalcMillis; }
	
	// -------------------------------------------- //
	// FIELDS: INSPECT
	// -------------------------------------------- //
	
	public double getOverheat()
	{
		return this.getHeat() - HEAT_HIGH;
	}
	
	public boolean isOverheated()
	{
		return this.getOverheat() > 0;
	}
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public HeatData withHeat(double heat) { return new HeatData(heat, lastCalcMillis); }
	public HeatData withLastCalcMillis(long lastCalcMillis) { return new HeatData(heat, lastCalcMillis); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private HeatData(double heat, long lastCalcMillis)
	{
		this.heat = Math.max(0, heat);
		this.lastCalcMillis = lastCalcMillis;
	}
	
	private HeatData()
	{
		this.heat = 0;
		this.lastCalcMillis = 0;
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static HeatData valueOf(double heat, long lastCalcMillis)
	{
		return new HeatData(heat, lastCalcMillis);
	}
	
	// -------------------------------------------- //
	// FACTORY: RECALCULATED
	// -------------------------------------------- //
	
	public HeatData recalculated(long now, double heatPerMilli)
	{
		if (this.lastCalcMillis + MILLIS_CALC_EPSILON >= now) return this;
		
		long deltaMillis = now - this.lastCalcMillis;
		double heatDelta = heatPerMilli * deltaMillis;
		double heatTarget = this.heat + heatDelta;
		heatTarget = Math.max(0, heatTarget);
		
		return valueOf(heatTarget, now);
	}

}
