package com.massivecraft.massivecore.mixin;

public interface HealthMixin
{
	// Health
	public Double getHealth(Object senderObject);
	public boolean setHealth(Object senderObject, double health);
	
	// Max Health
	public Double getMaxHealth(Object senderObject);
	public boolean setMaxHealth(Object senderObject, double maxHealth);
	
	// Convenience
	public boolean damage(Object senderObject, double damage);
	public boolean heal(Object senderObject, double heal);
	public boolean kill(Object senderObject);
}
