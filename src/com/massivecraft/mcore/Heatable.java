package com.massivecraft.mcore;

public abstract class Heatable
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	protected abstract HeatData getData();
	protected abstract void setData(HeatData data);
	
	// -------------------------------------------- //
	// UTIL
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
	
}
