package com.massivecraft.massivecore.mixin;

public abstract class HealthMixinAbstract implements HealthMixin
{
	@Override
	public boolean damage(Object senderObject, double damage)
	{
		Double current = this.getHealth(senderObject);
		if (current == null) return false;
		return this.setHealth(senderObject, current - damage);
	}

	@Override
	public boolean heal(Object senderObject, double heal)
	{
		Double current = this.getHealth(senderObject);
		if (current == null) return false;
		return this.setHealth(senderObject, current + heal);
	}
	
	@Override
	public boolean kill(Object senderObject)
	{
		return this.setHealth(senderObject, 0.0);
	}

}
