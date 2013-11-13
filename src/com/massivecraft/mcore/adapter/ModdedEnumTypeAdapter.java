package com.massivecraft.mcore.adapter;

import com.massivecraft.mcore.xlib.gson.Gson;
import com.massivecraft.mcore.xlib.gson.TypeAdapter;
import com.massivecraft.mcore.xlib.gson.TypeAdapterFactory;
import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;
import com.massivecraft.mcore.xlib.gson.reflect.TypeToken;
import com.massivecraft.mcore.xlib.gson.stream.JsonReader;
import com.massivecraft.mcore.xlib.gson.stream.JsonToken;
import com.massivecraft.mcore.xlib.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This type adapter and factory handles modified Java Enums.
 * It's based upon: https://github.com/MassiveCraft/mcore/blob/91f9ec7c0c7d9a11a35db905be520f5cf6b6743e/src/com/massivecraft/mcore/xlib/gson/internal/bind/TypeAdapters.java#L670
 * The only change is the try-catch around the annotation code.
 * 
 * https://github.com/MassiveCraft/mcore/pull/62
 * 
 * # Problem
 * The problem that was occurring is Forge modifies Vanilla Minecraft Enums
 * (see https://github.com/MinecraftForge/MinecraftForge/blob/master/common/net/minecraftforge/common/EnumHelper.java) 
 * the way that it modifies the Enum is pure Java hackery modifying $VALUES on the underlying Enum.
 * This will update the calls to Class.getEnumContants but won't update any fields on the Enum.
 * So when the built-in Gson EnumTypeAdaper tries to see if any SerializedName annotations are on the fields of the Enum,
 * it can't find a field with the new names and throws an exception.
 * 
 * # Reasoning
 * There is really not any way that we could put any fix in on the MCPC+ side since we can't add fields to Java enums at runtime.
 * 
 * # Solution
 * This ModdedEnumTypeAdapter is basically just a straight copy of the built-in one,
 * but ignores when it can't find the field/annotation (which is the desired behavior in this case anyways).
 * I tested this with Factions on the latest MCPC+ release and it resolves the issue that was logged.
 * Hopefully this will reduce the number of people opening issues on both sides.
 * If you have any questions, feel free to hit me up on IRC.
 * 
 * @author OniBait
 */
public final class ModdedEnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
	private final Map<String, T> nameToConstant = new HashMap<String, T>();
	private final Map<T, String> constantToName = new HashMap<T, String>();

	public ModdedEnumTypeAdapter(Class<T> classOfT) {
		for (T constant : classOfT.getEnumConstants()) {
			String name = constant.name();
			try { // MCore - Ignore when the field can't be found since modified enums won't have it.
				SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
				if (annotation != null) {
					name = annotation.value();
				}
			} catch (NoSuchFieldException ex) {} // MCore
			nameToConstant.put(name, constant);
			constantToName.put(constant, name);
		}
	}
	public T read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return nameToConstant.get(in.nextString());
	}

	public void write(JsonWriter out, T value) throws IOException {
		out.value(value == null ? null : constantToName.get(value));
	}

	public static final TypeAdapterFactory ENUM_FACTORY = newEnumTypeHierarchyFactory();

	public static <TT> TypeAdapterFactory newEnumTypeHierarchyFactory() {
		return new TypeAdapterFactory() {
			@SuppressWarnings({"rawtypes", "unchecked"})
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
				Class<? super T> rawType = typeToken.getRawType();
				if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
					return null;
				}
				if (!rawType.isEnum()) {
					rawType = rawType.getSuperclass(); // handle anonymous subclasses
				}
				return (TypeAdapter<T>) new ModdedEnumTypeAdapter(rawType);
			}
		};
	}
}
