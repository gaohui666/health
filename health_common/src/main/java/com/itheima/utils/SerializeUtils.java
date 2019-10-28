package com.itheima.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
    /**
     * 序列化 对象
     * @author tianya
     * @param o
     * @return
     */
    public static byte[] serialize(Object o){
        byte[] byteArray = null ;
        try(ByteArrayOutputStream bty = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bty);){
            oos.writeObject(o);
            byteArray = bty.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray ;
    }



    /**
     * 反序列化 对象
     * @author tianya
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes){
        Object o = null ;
        try(ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            ObjectInputStream ois =	new ObjectInputStream(bai);){
            o = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
}
