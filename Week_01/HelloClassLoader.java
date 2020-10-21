package com.csj.jvm;

import java.io.*;
import java.lang.reflect.Method;

/**
 * @author shunjie.cao
 */
public class HelloClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String path = HelloClassLoader.class.getResource("/Hello.xlass").getPath();
        byte[] bytes = new byte[0];
        try {
            bytes = toByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    public static void main(String[] args) {
        try {
            Class<?> hello = new HelloClassLoader().findClass("Hello");
            Method method = hello.getDeclaredMethod("hello");
            method.invoke(hello.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        File f = file;
        if (!f.exists()) {
            throw new FileNotFoundException("file not exists");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
}
