/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package com.massivecraft.massivecore.xlib.bson;

import com.massivecraft.massivecore.xlib.bson.io.BSONByteBuffer;
import com.massivecraft.massivecore.xlib.mongodb.DBObject;
import com.massivecraft.massivecore.xlib.mongodb.util.JSON;

/**
 * @author scotthernandez
 * @deprecated Please use {@link com.massivecraft.massivecore.xlib.mongodb.LazyDBList} instead.
 */
@Deprecated
@SuppressWarnings({"unused"})
public class LazyDBList extends LazyBSONList implements DBObject {
    private static final long serialVersionUID = -4415279469780082174L;

	public LazyDBList(byte[] data, LazyBSONCallback callback) { super(data, callback); }
	public LazyDBList(byte[] data, int offset, LazyBSONCallback callback) { super(data, offset, callback); }
	public LazyDBList(BSONByteBuffer buffer, LazyBSONCallback callback) { super(buffer, callback); }
	public LazyDBList(BSONByteBuffer buffer, int offset, LazyBSONCallback callback) { super(buffer, offset, callback); }

    /**
     * Returns a JSON serialization of this object
     * @return JSON serialization
     */
    @Override
    public String toString(){
        return JSON.serialize( this );
    }

    public boolean isPartialObject(){
        return _isPartialObject;
    }

    public void markAsPartialObject(){
        _isPartialObject = true;
    }

    private boolean _isPartialObject;
}
