package com.massivecraft.massivecore;

public abstract class Heatable
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract HeatData getData();
	public abstract void setData(HeatData data);
	
	public abstract double getHeatPerMilli();
	public abstract double getHeatPerExecution();
	
	// -------------------------------------------- //
	// CUSTOM
	// -------------------------------------------- //
	
	private HeatData getRecalculatedData(double heatPerMilli)
	{
		long now = System.currentTimeMillis();
		HeatData data = this.getData().recalculated(now, heatPerMilli); 
		this.setData(data);
		return data;
	}
	
	public void addHeat(double heatPerMilli, double heat)
	{
		long now = System.currentTimeMillis();
		HeatData data = this.getData().recalculated(now, heatPerMilli);
		data = data.withHeat(data.getHeat() + heat);
		this.setData(data);
	}
	
	public double getHeat(double heatPerMilli)
	{
		HeatData data = getRecalculatedData(heatPerMilli);
		return data.getHeat();
	}
	
	public boolean isOverheated(double heatPerMilli)
	{
		HeatData data = getRecalculatedData(heatPerMilli);
		return data.isOverheated();
	}
	
	public double getOverheat(double heatPerMilli)
	{
		HeatData data = getRecalculatedData(heatPerMilli);
		return data.getOverheat();
	}

	public long getCooldownMillisLeft(double heatPerMilli)
	{
		double overheat = this.getOverheat(heatPerMilli);
		return (long) (-overheat / heatPerMilli);
	}
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	public void addHeat(double heat)
	{
		this.addHeat(this.getHeatPerMilli(), heat);
	}
	
	public void addHeat()
	{
		this.addHeat(this.getHeatPerExecution());
	}
	
	public double getHeat()
	{
		return this.getHeat(this.getHeatPerMilli());
	}
	
	public boolean isOverheated()
	{
		return this.isOverheated(this.getHeatPerMilli());
	}
	
	public double getOverheat()
	{
		return this.getOverheat(this.getHeatPerMilli());
	}

	public long getCooldownMillisLeft()
	{
		return this.getCooldownMillisLeft(this.getHeatPerMilli());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static double calcHeatPerExecution(long executionCount, long periodMillis)
	{
		return 1D / (double)executionCount;
	}
	
	public static double calcHeatPerMilli(long executionCount, long periodMillis)
	{
		return - 1D / (double)periodMillis;
	}
	
}
