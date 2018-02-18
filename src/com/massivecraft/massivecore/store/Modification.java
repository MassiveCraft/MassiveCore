package com.massivecraft.massivecore.store;

public enum Modification
{

	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	LOCAL_ALTER (true, 40),
	LOCAL_ATTACH (true, 80),
	LOCAL_DETACH (true, 90),
	REMOTE_ALTER (true, 50),
	REMOTE_ATTACH (true, 60),
	REMOTE_DETACH (true, 70),
	NONE (false, 10),
	UNKNOWN (false, 30),
	UNKNOWN_LOG(false, 20),
	UNKNOWN_CHANGED(false, 35),
	;
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	// The modifications take priority over all others.
	// It is local attach and local detach, because otherwise
	// the system would think it was a remote attach or remote detach.
	public static final int TOP_PRIORITY = 80;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final boolean modified;
	public boolean isModified() { return this.modified; }
	
	private final int priority;
	public int getPriority() { return this.priority; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Modification(boolean modified, int priority)
	{
		this.modified = modified;
		this.priority = priority;
	}
	
	// -------------------------------------------- //
	// LOCAL VS REMOTE
	// -------------------------------------------- //
	
	public boolean isLocal()
	{
		return this == LOCAL_ALTER || this == LOCAL_ATTACH || this == LOCAL_DETACH;
	}
	public boolean isRemote()
	{
		return this == REMOTE_ALTER || this == REMOTE_ATTACH || this == REMOTE_DETACH;
	}
	
	// -------------------------------------------- //
	// SAFE
	// -------------------------------------------- //
	
	// If a modification state is safe,
	// that means that you can always be sure that when it is saved
	// in identifiedModifications, it ensured to actually be that.
	public boolean isSafe()
	{
		return this == LOCAL_ATTACH || this == LOCAL_DETACH;
	}
	
	// Local Attach and Detach has the top priority.
	// Otherwise newly attached entities would be removed thinking it was a remote detach.
	// Otherwise newly detached entities would be loaded thinking it was a remote attach.
	public boolean hasTopPriority()
	{
		return this.getPriority() >= TOP_PRIORITY;
	}
	
	// -------------------------------------------- //
	// UNKNOWN
	// -------------------------------------------- //
	
	public boolean isUnknown()
	{
		return this == Modification.UNKNOWN || this == Modification.UNKNOWN_LOG || this == Modification.UNKNOWN_CHANGED;
	}
}
