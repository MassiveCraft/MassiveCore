package com.massivecraft.mcore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ArgResult<T>
{
	// -------------------------------------------- //
	// FIELD: RESULT
	// -------------------------------------------- //
	
	@Getter @Setter protected T result = null;
	public boolean hasResult()
	{
		return this.getResult() != null;
	}
	
	// -------------------------------------------- //
	// FIELD: ERRORS
	// -------------------------------------------- //
	
	@Getter protected List<String> errors = new ArrayList<String>();
	
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