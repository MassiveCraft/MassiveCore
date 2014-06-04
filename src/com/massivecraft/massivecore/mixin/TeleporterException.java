package com.massivecraft.massivecore.mixin;

public class TeleporterException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public TeleporterException()
	{
		
	}

	public TeleporterException(String message)
	{
		super(message);
	}

	public TeleporterException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TeleporterException(Throwable cause)
	{
		super(cause);
	}

	protected TeleporterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
