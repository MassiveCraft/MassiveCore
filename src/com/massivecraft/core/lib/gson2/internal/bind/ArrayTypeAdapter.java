/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.massivecraft.core.lib.gson2.internal.bind;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.massivecraft.core.lib.gson2.internal.$Gson$Types;
import com.massivecraft.core.lib.gson2.reflect.TypeToken;
import com.massivecraft.core.lib.gson2.stream.JsonReader;
import com.massivecraft.core.lib.gson2.stream.JsonToken;
import com.massivecraft.core.lib.gson2.stream.JsonWriter;

/**
 * Adapt an array of objects.
 */
public final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
  public static final Factory FACTORY = new Factory() {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
      Type type = typeToken.getType();
      if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
        return null;
      }

      Type componentType = $Gson$Types.getArrayComponentType(type);
      TypeAdapter<?> componentTypeAdapter = context.getAdapter(TypeToken.get(componentType));
      // create() doesn't define a type parameter
      TypeAdapter<T> result = new ArrayTypeAdapter(
          context, componentTypeAdapter, $Gson$Types.getRawType(componentType));
      return result;
    }
  };

  private final Class<E> componentType;
  private final TypeAdapter<E> componentTypeAdapter;

  public ArrayTypeAdapter(MiniGson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
    this.componentTypeAdapter =
      new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
    this.componentType = componentType;
  }

  public Object read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    List<E> list = new ArrayList<E>();
    reader.beginArray();
    while (reader.hasNext()) {
      E instance = componentTypeAdapter.read(reader);
      list.add(instance);
    }
    reader.endArray();
    Object array = Array.newInstance(componentType, list.size());
    for (int i = 0; i < list.size(); i++) {
      Array.set(array, i, list.get(i));
    }
    return array;
  }

  @SuppressWarnings("unchecked")
  @Override public void write(JsonWriter writer, Object array) throws IOException {
    if (array == null) {
      writer.nullValue(); // TODO: better policy here?
      return;
    }

    writer.beginArray();
    for (int i = 0, length = Array.getLength(array); i < length; i++) {
      final E value = (E) Array.get(array, i);
      componentTypeAdapter.write(writer, value);
    }
    writer.endArray();
  }
}
