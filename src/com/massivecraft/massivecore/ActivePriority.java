package com.massivecraft.massivecore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivePriority
{
	int value();

	int PRIORITY_COLL = 1000;
	int PRIORITY_NMS = PRIORITY_COLL + 1000;
	int PRIORITY_COMMAND = PRIORITY_NMS + 1000;
	int PRIORITY_ENGINE = PRIORITY_COMMAND + 1000;
	int PRIORITY_INTEGRATION = PRIORITY_ENGINE + 1000;
	int PRIORITY_TASK = PRIORITY_INTEGRATION + 1000;
	int PRIORITY_MIXIN = PRIORITY_TASK + 1000;
	int PRIORITY_TEST = PRIORITY_MIXIN + 1000;
	int PRIORITY_MIGRATOR = PRIORITY_TEST + 1000;

}
