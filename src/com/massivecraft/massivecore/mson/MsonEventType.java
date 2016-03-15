package com.massivecraft.massivecore.mson;

public abstract class MsonEventType
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static MsonEventType CLICK = new MsonEventType()
	{
		@Override
		public MsonEvent get(Mson mson)
		{
			return mson.clickEvent;
		}

		@Override
		public Mson set(Mson mson, MsonEvent event)
		{
			return Mson.valueOf(
				mson.getText(),
				mson.getColor(),
				mson.isBold(),
				mson.isItalic(),
				mson.isUnderlined(),
				mson.isStrikethrough(),
				mson.isObfuscated(),
				event,
				mson.getEvent(HOVER),
				mson.getInsertion(),
				mson.getExtra(),
				mson.getParent()
			);
		}
	};
	
	public static MsonEventType HOVER = new MsonEventType()
	{
		@Override
		public MsonEvent get(Mson mson)
		{
			return mson.hoverEvent;
		}
		
		@Override
		public Mson set(Mson mson, MsonEvent event)
		{
			return Mson.valueOf(
				mson.getText(),
				mson.getColor(),
				mson.isBold(),
				mson.isItalic(),
				mson.isUnderlined(),
				mson.isStrikethrough(),
				mson.isObfuscated(),
				mson.getEvent(CLICK),
				event,
				mson.getInsertion(),
				mson.getExtra(),
				mson.getParent()
			);
		}
	};
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract MsonEvent get(Mson mson);
	public abstract Mson set(Mson mson, MsonEvent event);

}
