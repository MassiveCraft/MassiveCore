package com.massivecraft.mcore5.mixin;

public class Mixin
{
	private static PsTeleporterMixin psTeleporterMixin = DefaultPsTeleporterMixin.get();
	public static PsTeleporterMixin getPsTeleporterMixin() { return psTeleporterMixin; }
	public static void setPsTeleporterMixin(PsTeleporterMixin val) { psTeleporterMixin = val; }
	
	private static DisplayNameMixin displayNameMixin = DefaultDisplayNameMixin.get();
	public static DisplayNameMixin getDisplayNameMixin() { return displayNameMixin; }
	public static void setDisplayNameMixin(DisplayNameMixin val) { displayNameMixin = val; }
	
	private static ListNameMixin listNameMixin = DefaultListNameMixin.get();
	public static ListNameMixin getListNameMixin() { return listNameMixin; }
	public static void setListNameMixin(ListNameMixin val) { listNameMixin = val; }
	
	private static SenderIdFixerMixin senderIdFixerMixin = DefaultSenderIdFixerMixin.get();
	public static SenderIdFixerMixin getSenderIdFixerMixin() { return senderIdFixerMixin; }
	public static void setSenderIdFixerMixin(SenderIdFixerMixin val) { senderIdFixerMixin = val; }
	
	// Add the command sender id resolver system as a mixin?
	// Nah that could possibly stay a util.
	
	// Player last online should be added
	
}
