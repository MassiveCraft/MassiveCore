package com.massivecraft.massivecore.command.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorField
{
	boolean nullable() default true;
	
	Class<?> type() default void.class;
	String singletonName() default "i";
}
