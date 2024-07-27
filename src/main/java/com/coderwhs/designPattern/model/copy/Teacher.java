package com.coderwhs.designPattern.model.copy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.*;

/**
 * @Author whs
 * @Date 2024/7/27 14:09
 * @description:
 */
@Data
@AllArgsConstructor
public class Teacher implements Serializable {
    private String teacherName;
    private Student student;

    // 实现深拷贝
    public Teacher deepCopy(){
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        Teacher teacher = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            // 序列化
            outputStream.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            inputStream = new ObjectInputStream(byteArrayInputStream);
            // 反序列化
            teacher = (Teacher) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return teacher;
    }
}
