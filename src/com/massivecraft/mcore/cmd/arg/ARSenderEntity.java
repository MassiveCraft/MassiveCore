package com.massivecraft.mcore.cmd.arg;

import com.massivecraft.mcore.store.SenderColl;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.store.SenderIdSource;
import com.massivecraft.mcore.util.IdUtil;

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
		if (senderId == null) return null;
		
		// Convert names to ids so we can handle both
		String betterId = IdUtil.getId(senderId);
		if (betterId != null) return this.coll.get(betterId);
		
		return this.coll.get(senderId);
	}
	
}
