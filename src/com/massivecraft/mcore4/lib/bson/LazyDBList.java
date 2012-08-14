/**
 *
 */
package com.massivecraft.mcore4.lib.bson;


import com.massivecraft.mcore4.lib.bson.io.BSONByteBuffer;
import com.massivecraft.mcore4.lib.mongodb.DBObject;
import com.massivecraft.mcore4.lib.mongodb.util.JSON;

/**
 * @author scotthernandez
 *
 */
public class LazyDBList extends LazyBSONList implements DBObject {
    @SuppressWarnings("unused")
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
