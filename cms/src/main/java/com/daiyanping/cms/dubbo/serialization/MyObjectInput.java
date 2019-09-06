package com.daiyanping.cms.dubbo.serialization;

import org.apache.dubbo.common.serialize.ObjectInput;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @ClassName MyObjectInput
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-05
 * @Version 0.1
 */
public class MyObjectInput implements ObjectInput {
    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean readBool() throws IOException {
        return false;
    }

    @Override
    public byte readByte() throws IOException {
        return 0;
    }

    @Override
    public short readShort() throws IOException {
        return 0;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public String readUTF() throws IOException {
        return null;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }
}
