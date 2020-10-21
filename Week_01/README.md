##学习笔记

###字节码分析
观察发现
> long 类型转换 short 使用了两个助记符 l2i 和 i2s
> 
>JDK8 在做字符串拼接时默认使用了 StringBuilder 

```java
public String test(int x){
    String result = "";
    if (x != 0){
        result = test(x / 2);
        result += x % 2;
    }
    return result;
}
```
```
public java.lang.String test(int);
    Code:
       0: ldc           #9                  // String
       2: astore_2
       3: iload_1
       4: ifeq          36
       7: aload_0
       8: iload_1
       9: iconst_2
      10: idiv
      11: invokevirtual #5                  // Method test:(I)Ljava/lang/String;
      14: astore_2
      15: new           #10                 // class java/lang/StringBuilder
      18: dup
      19: invokespecial #11                 // Method java/lang/StringBuilder."<init>":()V
      22: aload_2
      23: invokevirtual #12                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      26: iload_1
      27: iconst_2
      28: irem
      29: invokevirtual #13                 // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
      32: invokevirtual #14                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      35: astore_2
      36: aload_2
      37: areturn
```

