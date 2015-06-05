package com.massivecraft.massivecore.mson;

public enum MsonEventAction
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //

	SUGGEST_COMMAND(),
	RUN_COMMAND(),
	OPEN_URL(),
	SHOW_TEXT(true),
	SHOW_ITEM(true),
	
	// End of list
	;
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //

	private boolean isHoverAction;
	public boolean isHoverAction() { return this.isHoverAction; }

	// NOTE: This behaviour might change.
	// So to check if something is a click action this method should be called.
	// Doing !action.isHoverAction();
	// Shouldn't be done outside of this class.
	public boolean isClickAction() { return ! this.isHoverAction(); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	private MsonEventAction(boolean isHoveraction)
	{
		this.isHoverAction = isHoveraction;
	}

	private MsonEventAction()
	{
		this(false);
	}

}
