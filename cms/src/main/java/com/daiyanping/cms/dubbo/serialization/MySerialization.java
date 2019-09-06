package com.daiyanping.cms.dubbo.serialization;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName MySerialization
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-05
 * @Version 0.1
 */
public class MySerialization implements Serialization {
    @Override
    public byte getContentTypeId() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return null;
    }

    @Override
    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return null;
    }
}
