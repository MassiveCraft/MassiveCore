package com.massivecraft.mcore5.mixin;

public class PsTeleporterException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PsTeleporterException()
	{
		super();
	}

	public PsTeleporterException(String message)
	{
		super(message);
	}

	public PsTeleporterException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PsTeleporterException(Throwable cause)
	{
		super(cause);
	}

	protected PsTeleporterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
