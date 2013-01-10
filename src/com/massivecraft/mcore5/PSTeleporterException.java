package com.massivecraft.mcore5;

public class PSTeleporterException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PSTeleporterException()
	{
		super();
	}

	public PSTeleporterException(String message)
	{
		super(message);
	}

	public PSTeleporterException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PSTeleporterException(Throwable cause)
	{
		super(cause);
	}

	protected PSTeleporterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
