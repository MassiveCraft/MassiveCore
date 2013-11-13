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

public final class ModdedEnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap<String, T>();
        private final Map<T, String> constantToName = new HashMap<T, String>();

        public ModdedEnumTypeAdapter(Class<T> classOfT) {
            for (T constant : classOfT.getEnumConstants()) {
                String name = constant.name();
                // Ignore when the field can't be found since modified enums won't have it
                // which is fine, since it wouldn't have an annotation anyway (how could it?)
                try {
                    SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                } catch (NoSuchFieldException ex) {}
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