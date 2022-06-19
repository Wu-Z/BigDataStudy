package com.kafka.jerry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.*;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVisitWebEvent implements Serializer, Serializable, Deserializer {

    /**
     * 日志的唯一 id
     */
    private String id;

    /**
     * 日期，如：20191025
     */
    private String date;

    /**
     * 页面 id
     */
    private Integer pageId;

    /**
     *  用户的唯一标识，用户 id
     */
    private String userId;

    /**
     * 页面的 url
     */
    private String url;

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(o);
            bytes = bo.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bo!=null){
                    bo.close();
                }
                if(oo!=null){
                    oo.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            bi =new ByteArrayInputStream(data);
            oi =new ObjectInputStream(bi);
            obj = oi.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(bi!=null){
                    bi.close();
                }
                if(oi!=null){
                    oi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    @Override
    public void close() {

    }
}