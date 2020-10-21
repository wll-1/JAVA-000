package jvmbytecode;

import java.sql.SQLOutput;

public class HelloByteCode implements HelloInterface{

    public static void main(String[] args) {
        HelloByteCode helloByteCode = new HelloByteCode();
        helloByteCode.hello();

        int i = 19;
        helloByteCode.test(i);
        float j = 2.5f;
        double k = i - j;
        k *= i;

        long x = 111L;
        short y = 12;
        x += y;

        y = (short) (y + x);

        for (; i > 1; i /= 2){
            i++;
        }
    }

    public String test(int x){
        String result = "";
        if (x != 0){
            result = test(x / 2);
            result += x % 2;
        }
        return result;
    }


    @Override
    public HelloEnum hello() {
        return HelloEnum.HELLO;
    }
}
