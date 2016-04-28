package com.massivecraft.massivecore.command.editor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorTypeSet
{
	// This is the type class.
	Class<?> value();
	
	// The name of the singleton instance field to use.
	String fieldName() default "i";
}
