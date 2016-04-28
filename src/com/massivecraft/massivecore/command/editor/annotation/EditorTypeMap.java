package com.massivecraft.massivecore.command.editor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorTypeMap
{
	// This is the type class.
	Class<?> typeKey();
	Class<?> typeValue();
	
	// The name of the singleton instance field to use.
	String fieldNameKey() default "i";
	String fieldNameValue() default "i";
}
