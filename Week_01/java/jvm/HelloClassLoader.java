package jvm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            HelloClassLoader helloClassLoader = new HelloClassLoader();
            Class aClass= helloClassLoader.findClass("Hello");
            for (Method method : aClass.getDeclaredMethods()){
                method.invoke(aClass.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File helloFile = new File("jvm/Hello.xlass");
        if (helloFile.length() > Integer.MAX_VALUE){
            return null;
        }
        try (InputStream in = new FileInputStream(helloFile)){
            byte[] bytes = new byte[(int) helloFile.length()];
            int b;
            int i = 0;
            while ((b = in.read()) != -1){
                b -= 255;
                b = Math.abs(b);
                bytes[i++] = (byte) b;
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }
}
