package com.xiecode.redisdemo.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
* @Description: 序列化工具类
* @param:
* @return:
* @Author: Xiewc
* @Date: 2022/5/20
*/
public class SerializeUtil {

    /**
    * @Description: 将java对象转换为byte数组序列化的过程
    * @param: [object]
    * @return: byte[]
    * @Author: Xiewc
    * @Date: 2022/5/20
    */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        if (bytes == null) return null;
        ObjectInputStream ois;
        ByteArrayInputStream bais;
        try {
            //反序列化
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
