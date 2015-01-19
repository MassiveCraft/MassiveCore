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

package com.massivecraft.massivecore.xlib.mongodb;

import com.massivecraft.massivecore.xlib.bson.LazyBSONCallback;
import com.massivecraft.massivecore.xlib.bson.io.BSONByteBuffer;

@SuppressWarnings({"deprecation"})
public class LazyDBList extends com.massivecraft.massivecore.xlib.bson.LazyDBList {

    public LazyDBList(final byte[] data, final LazyBSONCallback callback) {
        super(data, callback);
    }

    public LazyDBList(final byte[] data, final int offset, final LazyBSONCallback callback) {
        super(data, offset, callback);
    }

    public LazyDBList(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        super(buffer, callback);
    }

    public LazyDBList(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }
}
