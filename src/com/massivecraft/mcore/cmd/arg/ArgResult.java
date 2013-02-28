package com.massivecraft.mcore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgResult<T>
{
	// -------------------------------------------- //
	// FIELD: RESULT
	// -------------------------------------------- //
	
	protected T result = null;
	public T getResult() { return this.result; }
	public void setResult(T result) { this.result = result; }
	public boolean hasResult()
	{
		return this.getResult() != null;
	}
	
	// -------------------------------------------- //
	// FIELD: ERRORS
	// -------------------------------------------- //
	
	protected List<String> errors = new ArrayList<String>();
	public List<String> getErrors() { return this.errors; }
	
	public void setErrors(List<String> val)
	{
		if (val == null)
		{
			this.errors = new ArrayList<String>();
		}
		else
		{
			this.errors = val;
		}
	}
	
	public void setErrors(String... val)
	{
		this.setErrors(Arrays.asList(val));
	}
	
	public boolean hasErrors()
	{
		return this.errors.size() > 0;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ArgResult()
	{
		
	}
	
	public ArgResult(T result)
	{
		this.setResult(result);
	}
	
	public ArgResult(T result, List<String> errors)
	{
		this.setResult(result);
		this.setErrors(errors);
	}
	
	public ArgResult(T result, String... errors)
	{
		this.setResult(result);
		this.setErrors(errors);
	}
}