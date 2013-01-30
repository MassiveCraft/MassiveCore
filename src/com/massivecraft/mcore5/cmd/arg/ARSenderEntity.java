package com.massivecraft.mcore5.cmd.arg;

import com.massivecraft.mcore5.store.SenderIdSource;
import com.massivecraft.mcore5.store.SenderColl;
import com.massivecraft.mcore5.store.SenderEntity;

public class ARSenderEntity<T extends SenderEntity<T>> extends ARSenderIdAbstractPredsource<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final SenderColl<T> coll;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getFullAny(SenderColl<T> coll) { return getFullAny(coll, coll.isCreative() ? coll.getMixinedIdSource() : coll); }
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getStartAny(SenderColl<T> coll) { return getStartAny(coll, coll.isCreative() ? coll.getMixinedIdSource() : coll); }
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getFullOnline(SenderColl<T> coll) { return getFullOnline(coll, coll.isCreative() ? coll.getMixinedIdSource() : coll); }
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getStartOnline(SenderColl<T> coll) { return getStartOnline(coll, coll.isCreative() ? coll.getMixinedIdSource() : coll); }
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getFullOnline(SenderColl<T> coll, SenderIdSource source)
	{
		return new ARSenderEntity<T>(coll, source, "player", new ArgPredictateAnd<String>(ArgPredictateStringEqualsLC.get(), ArgPredictateStringIsOnlineSenderId.get()));
	}
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getStartOnline(SenderColl<T> coll, SenderIdSource source)
	{
		return new ARSenderEntity<T>(coll, source, "player", new ArgPredictateAnd<String>(ArgPredictateStringStartsLC.get(), ArgPredictateStringIsOnlineSenderId.get()));
	}
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getFullAny(SenderColl<T> coll, SenderIdSource source)
	{
		return new ARSenderEntity<T>(coll, source, "player", ArgPredictateStringEqualsLC.get());
	}
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> getStartAny(SenderColl<T> coll, SenderIdSource source)
	{
		return new ARSenderEntity<T>(coll, source, "player", ArgPredictateStringStartsLC.get());
	}
	
	private ARSenderEntity(SenderColl<T> coll, SenderIdSource source, String typename, ArgPredictate<String> argPredictate)
	{
		super(source, typename, argPredictate);
		this.coll = coll;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T getResultForSenderId(String senderId)
	{
		return this.coll.get(senderId);
	}
	
}
