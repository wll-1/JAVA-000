#GC内存分析
##串行GC
> java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails GCLogAnalysis
```text
1[GC (Allocation Failure) [DefNew: 34878K->4352K(39296K), 0.0050412 secs] 34878K->13574K(126720K), 0.0054647 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2[GC (Allocation Failure) [DefNew: 39124K->4351K(39296K), 0.0059390 secs] 48347K->26338K(126720K), 0.0062209 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
... 4
7[GC (Allocation Failure) [DefNew: 39193K->4349K(39296K), 0.0052817 secs] 97558K->75320K(126720K), 0.0055498 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
8[GC (Allocation Failure) [DefNew: 38756K->4343K(39296K), 0.0055645 secs] 109727K->85901K(126720K), 0.0057471 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
9[GC (Allocation Failure) [DefNew: 39065K->39065K(39296K), 0.0001807 secs][Tenured: 81558K->86951K(87424K), 0.0126753 secs] 120623K->88506K(126720K), [Metaspace: 2781K->2781K(1056768K)], 0.0139090 secs] [Times: user=0.00 sys=0.01, real=0.01 secs]
10[Full GC (Allocation Failure) [Tenured: 87361K->86889K(87424K), 0.0094797 secs] 126601K->97025K(126720K), [Metaspace: 2781K->2781K(1056768K)], 0.0104750 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
11[Full GC (Allocation Failure) [Tenured: 87373K->87353K(87424K), 0.0104492 secs] 126655K->106638K(126720K), [Metaspace: 2781K->2781K(1056768K)], 0.0110615 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
... 13
25[Full GC (Allocation Failure) [Tenured: 87291K->87291K(87424K), 0.0014146 secs] 126572K->126023K(126720K), [Metaspace: 2781K->2781K(1056768K)], 0.0018564 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
26[Full GC (Allocation Failure) [Tenured: 87291K->87084K(87424K), 0.0101330 secs] 126023K->125816K(126720K), [Metaspace: 2781K->2781K(1056768K)], 0.0109147 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
        at GCLogAnalysis.main(GCLogAnalysis.java:25)
Heap
 def new generation   total 39296K, used 38963K [0x00000000f8000000, 0x00000000faaa0000, 0x00000000faaa0000)
  eden space 34944K, 100% used [0x00000000f8000000, 0x00000000fa220000, 0x00000000fa220000)
  from space 4352K,  92% used [0x00000000fa660000, 0x00000000faa4cdf0, 0x00000000faaa0000)
  to   space 4352K,   0% used [0x00000000fa220000, 0x00000000fa220000, 0x00000000fa660000)
 tenured generation   total 87424K, used 87084K [0x00000000faaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 87424K,  99% used [0x00000000faaa0000, 0x00000000fffab3b0, 0x00000000fffab400, 0x0000000100000000)
 Metaspace       used 2811K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 312K, capacity 386K, committed 512K, reserved 1048576K
```
> Young区内存 总39296K(约38M)约占堆内存31% , old区内存 总87424K(约85M)约占堆内存69%
>
> 第一次执行GC Young区34878K(占Young区总大小约89%) 清理掉30526K, 9222K晋升至Old区
>
> 第9次CG Young区空间剩余不到1%, 同时Old区空间占用93%, 清理Young区和Old区 32117K 内存后 Old区还有86951K 占总的99%
>
> 之后一直执行FullGC直至OOM
------
> java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [DefNew: 69952K->8704K(78656K), 0.0091920 secs] 69952K->24985K(253440K), 0.0095464 secs] [Times: user=0.00 sys=0.02, real=0.01 secs]
...8
[GC (Allocation Failure) [DefNew: 69952K->69952K(78656K), 0.0004475 secs][Tenured: 172733K->174327K(174784K), 0.0180042 secs] 242685K->186634K(253440K), [Metaspace: 2781K->2781K(1056768K)], 0.0207583 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
[Full GC (Allocation Failure) [Tenured: 174712K->174773K(174784K), 0.0222044 secs] 253363K->186617K(253440K), [Metaspace: 2781K->2781K(1056768K)], 0.0225387 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
...40
[Full GC (Allocation Failure) [Tenured: 174748K->174665K(174784K), 0.0021931 secs] 252693K->252609K(253440K), [Metaspace: 2781K->2781K(1056768K)], 0.0028342 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
        at GCLogAnalysis.main(GCLogAnalysis.java:25)
```
> Young区内存 总78656K(约77M)约占堆内存31% , old区内存 总87424K(约85M)约占堆内存69%
> 共执行52次GC, GC情况和之前基本类似
----
> java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [DefNew: 139776K->17471K(157248K), 0.0162371 secs] 139776K->47079K(506816K), 0.0166365 secs] [Times: user=0.00 sys=0.02, real=0.02 secs]
...12
[GC (Allocation Failure) [DefNew: 139708K->139708K(157248K), 0.0001480 secs][Tenured: 315382K->346369K(349568K), 0.0263186 secs] 455091K->346369K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0268053 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0004329 secs][Tenured: 346369K->349269K(349568K), 0.0322831 secs] 486145K->351161K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0335401 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349541K->349563K(349568K), 0.0374378 secs] 506711K->357425K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0377249 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349563K->342800K(349568K), 0.0390935 secs] 506782K->342800K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0394274 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0001575 secs][Tenured: 342800K->349549K(349568K), 0.0268229 secs] 482576K->366043K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0275520 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349549K->349501K(349568K), 0.0329738 secs] 506300K->368546K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0333035 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349501K->349100K(349568K), 0.0358960 secs] 506517K->371989K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0361447 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349100K->347497K(349568K), 0.0402022 secs] 505942K->347497K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0403999 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0001457 secs][Tenured: 347497K->349328K(349568K), 0.0279157 secs] 487273K->364352K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0283367 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349436K->349500K(349568K), 0.0335371 secs] 506680K->370185K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0344613 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
执行结束!共生成对象次数:12266
Heap
 def new generation   total 157248K, used 26399K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,  18% used [0x00000000e0000000, 0x00000000e19c7ca0, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
  to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
 tenured generation   total 349568K, used 349500K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 349568K,  99% used [0x00000000eaaa0000, 0x00000000fffef258, 0x00000000fffef400, 0x0000000100000000)
 Metaspace       used 2787K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 310K, capacity 386K, committed 512K, reserved 1048576K
```
> 内存占比基本类似 
> 512M总内存 创建12266个对象  执行23次GC 执行6次FullGC 最高暂停时间40毫秒 
---------------
> java -XX:+UseSerialGC -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [DefNew: 69952K->8703K(78656K), 0.0099737 secs] 69952K->24747K(253440K), 0.0103870 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
...6
[GC (Allocation Failure) [DefNew: 78590K->8703K(78656K), 0.0093669 secs][Tenured: 178980K->160546K(179120K), 0.0165658 secs] 235116K->160546K(257776K), [Metaspace: 2781K->2781K(1056768K)], 0.0269021 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 106710K->13375K(120448K), 0.0070986 secs] 267256K->192869K(388028K), 0.0075795 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 120447K->13375K(120448K), 0.0150537 secs] 299941K->224859K(388028K), 0.0154234 secs] [Times: user=0.00 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 120447K->13375K(120448K), 0.0140564 secs] 331931K->263514K(388028K), 0.0145190 secs] [Times: user=0.00 sys=0.01, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 120447K->13375K(120448K), 0.0144384 secs][Tenured: 290647K->246602K(290764K), 0.0259316 secs] 370586K->246602K(411212K), [Metaspace: 2781K->2781K(1056768K)], 0.0413845 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->17466K(157248K), 0.0095724 secs] 386378K->296566K(506816K), 0.0100744 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
...8
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0002694 secs][Tenured: 327849K->348981K(349568K), 0.0238054 secs] 467625K->356353K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0247058 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
[Full GC (Allocation Failure) [Tenured: 349359K->349109K(349568K), 0.0430651 secs] 506442K->359120K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0437276 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349397K->349477K(349568K), 0.0367794 secs] 506508K->363577K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0373424 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349477K->348016K(349568K), 0.0395889 secs] 506049K->348016K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0399453 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0004467 secs][Tenured: 348016K->349170K(349568K), 0.0273808 secs] 487792K->372229K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0288254 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349559K->349566K(349568K), 0.0335714 secs] 506771K->376684K(506816K), [Metaspace: 2781K->2781K(1056768K)], 0.0341818 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
执行结束!共生成对象次数:11745
```
> 不加-Xms参数Young区Old区大小逐渐变大
-----
> java -XX:+UseSerialGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [DefNew: 279616K->34944K(314560K), 0.0324664 secs] 279616K->94104K(1013632K), 0.0327653 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 314560K->34943K(314560K), 0.0350928 secs] 373720K->162787K(1013632K), 0.0353530 secs] [Times: user=0.02 sys=0.02, real=0.04 secs]
... 13
[GC (Allocation Failure) [DefNew: 279616K->34943K(314560K), 0.0108200 secs] 667675K->474129K(1013632K), 0.0110931 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 314559K->34943K(314560K), 0.0141977 secs] 753745K->550852K(1013632K), 0.0145318 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
执行结束!共生成对象次数:18059
```
> 1G总内存 创建18059个对象  执行17次GC 最高暂停时间40毫秒 没有执行FullGC
----
> java -XX:+UseSerialGC -Xms4g  -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [DefNew: 1118528K->139775K(1258304K), 0.0796817 secs] 1118528K->246590K(4054528K), 0.0798898 secs] [Times: user=0.03 sys=0.05, real=0.08 secs]
[GC (Allocation Failure) [DefNew: 1258303K->139775K(1258304K), 0.0970258 secs] 1365118K->400962K(4054528K), 0.0972628 secs] [Times: user=0.05 sys=0.05, real=0.10 secs]
[GC (Allocation Failure) [DefNew: 1258303K->139775K(1258304K), 0.0643714 secs] 1519490K->543281K(4054528K), 0.0647323 secs] [Times: user=0.03 sys=0.03, real=0.07 secs]
[GC (Allocation Failure) [DefNew: 1258303K->139776K(1258304K), 0.0686017 secs] 1661809K->695377K(4054528K), 0.0687547 secs] [Times: user=0.03 sys=0.03, real=0.07 secs]
执行结束!共生成对象次数:16941
```
> 4G总内存 创建16941个对象 执行4次GC 最高暂停时间100毫秒
------
##并行GC
> java -XX:+UseParallelGC -Xms128m -Xmx128m -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [PSYoungGen: 33049K->5096K(38400K)] 33049K->10982K(125952K), 0.0032439 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...4
[GC (Allocation Failure) [PSYoungGen: 38392K->5107K(19968K)] 84699K->63898K(107520K), 0.0040183 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...3
[GC (Allocation Failure) [PSYoungGen: 28540K->9306K(29184K)] 94405K->85360K(116736K), 0.0034139 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[Full GC (Ergonomics) [PSYoungGen: 9306K->0K(29184K)] [ParOldGen: 76053K->77616K(87552K)] 85360K->77616K(116736K), [Metaspace: 2781K->2781K(1056768K)], 0.0117883 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
...16
[Full GC (Allocation Failure) [PSYoungGen: 14703K->14703K(29184K)] [ParOldGen: 87424K->87404K(87552K)] 102128K->102108K(116736K), [Metaspace: 2781K->2781K(1056768K)], 0.0153175 secs] [Times: user=0.13 sys=0.00, real=0.02 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.util.Arrays.copyOf(Unknown Source)
        at java.lang.AbstractStringBuilder.ensureCapacityInternal(Unknown Source)
        at java.lang.AbstractStringBuilder.append(Unknown Source)
```
> 运行期间 堆内存大小 和Young区大小发生过变化 
---
> java -XX:+UseParallelGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [PSYoungGen: 131584K->21503K(153088K)] 131584K->43268K(502784K), 0.0060657 secs] [Times: user=0.03 sys=0.09, real=0.01 secs]
...9
[Full GC (Ergonomics) [PSYoungGen: 37366K->0K(116736K)] [ParOldGen: 291255K->231161K(349696K)] 328622K->231161K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0291744 secs] [Times: user=0.16 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->20447K(116736K)] 290041K->251608K(466432K), 0.0043415 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...4
[Full GC (Ergonomics) [PSYoungGen: 17684K->0K(116736K)] [ParOldGen: 307085K->260460K(349696K)] 324769K->260460K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0298949 secs] [Times: user=0.09 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58573K->17674K(116736K)] 319033K->278134K(466432K), 0.0039946 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...3
[Full GC (Ergonomics) [PSYoungGen: 18115K->0K(116736K)] [ParOldGen: 318404K->281025K(349696K)] 336520K->281025K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0311741 secs] [Times: user=0.13 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->19364K(116736K)] 339905K->300390K(466432K), 0.0040767 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...2
[Full GC (Ergonomics) [PSYoungGen: 20018K->0K(116736K)] [ParOldGen: 317375K->301077K(349696K)] 337393K->301077K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0334936 secs] [Times: user=0.23 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->24448K(116736K)] 359957K->325526K(466432K), 0.0028062 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 83328K->16354K(116736K)] 384406K->341444K(466432K), 0.0046863 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 16354K->0K(116736K)] [ParOldGen: 325089K->311825K(349696K)] 341444K->311825K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0391677 secs] [Times: user=0.19 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 58809K->22983K(116736K)] 370634K->334808K(466432K), 0.0046081 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 81863K->18329K(116736K)] 393688K->351011K(466432K), 0.0059518 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 18329K->0K(116736K)] [ParOldGen: 332681K->313716K(349696K)] 351011K->313716K(466432K), [Metaspace: 2781K->2781K(1056768K)], 0.0347682 secs] [Times: user=0.19 sys=0.02, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 58417K->20302K(116736K)] 372133K->334018K(466432K), 0.0042707 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 79182K->20234K(117760K)] 392898K->353654K(467456K), 0.0061790 secs] [Times: user=0.08 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 20234K->0K(117760K)] [ParOldGen: 333420K->320781K(349696K)] 353654K->320781K(467456K), [Metaspace: 2781K->2781K(1056768K)], 0.0345982 secs] [Times: user=0.22 sys=0.00, real=0.04 secs]
...7
[Full GC (Ergonomics) [PSYoungGen: 59904K->0K(117760K)] [ParOldGen: 341101K->341836K(349696K)] 401005K->341836K(467456K), [Metaspace: 2781K->2781K(1056768K)], 0.0353774 secs] [Times: user=0.23 sys=0.00, real=0.04 secs]
执行结束!共生成对象次数:9695
```
> 512m 创建9695个对象 共执行43次GC 执行15次FullGC 最大暂停时间 40ms
---
> java -XX:+UseParallelGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [PSYoungGen: 262144K->43503K(305664K)] 262144K->80132K(1005056K), 0.0099212 secs] [Times: user=0.05 sys=0.08, real=0.01 secs]
...12
[Full GC (Ergonomics) [PSYoungGen: 38869K->0K(232960K)] [ParOldGen: 616295K->314837K(699392K)] 655164K->314837K(932352K), [Metaspace: 2781K->2781K(1056768K)], 0.0410296 secs] [Times: user=0.23 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 116736K->38883K(232960K)] 431573K->353721K(932352K), 0.0042146 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...10
[Full GC (Ergonomics) [PSYoungGen: 43047K->0K(232960K)] [ParOldGen: 658182K->361751K(699392K)] 701229K->361751K(932352K), [Metaspace: 2781K->2781K(1056768K)], 0.0410554 secs] [Times: user=0.23 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 116736K->41488K(232960K)] 478487K->403240K(932352K), 0.0044326 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...8
[Full GC (Ergonomics) [PSYoungGen: 66104K->0K(259584K)] [ParOldGen: 681646K->370831K(699392K)] 747751K->370831K(958976K), [Metaspace: 2781K->2781K(1056768K)], 0.0450257 secs] [Times: user=0.36 sys=0.00, real=0.04 secs]
执行结束!共生成对象次数:18827
```
> 1g 创建18827个对象 共执行 36 次GC FullGC 3次 最大暂定时间 40ms
---
> java -XX:+UseParallelGC -Xms2g -Xmx2g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [PSYoungGen: 524800K->87029K(611840K)] 524800K->138573K(2010112K), 0.0159244 secs] [Times: user=0.05 sys=0.08, real=0.02 secs]
...14
[GC (Allocation Failure) [PSYoungGen: 307825K->79394K(465920K)] 1441276K->1271842K(1864192K), 0.0149043 secs] [Times: user=0.06 sys=0.06, real=0.02 secs]
执行结束!共生成对象次数:21257
``` 
> 2g 创建21257个对象 执行16次GC 没有FullGC 最大暂停时间 20ms
---
> java -XX:+UseParallelGC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
```text
正在执行...
[GC (Allocation Failure) [PSYoungGen: 1048576K->174591K(1223168K)] 1048576K->224778K(4019712K), 0.0252169 secs] [Times: user=0.02 sys=0.11, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 1223167K->174590K(1223168K)] 1273354K->340025K(4019712K), 0.0323055 secs] [Times: user=0.06 sys=0.19, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 1223166K->174585K(1223168K)] 1388601K->458408K(4019712K), 0.0301872 secs] [Times: user=0.17 sys=0.08, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 1223161K->174577K(1223168K)] 1506984K->598981K(4019712K), 0.0329052 secs] [Times: user=0.17 sys=0.08, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 1223153K->174577K(1223168K)] 1647557K->729957K(4019712K), 0.0315742 secs] [Times: user=0.13 sys=0.11, real=0.03 secs]
执行结束!共生成对象次数:23526
```
> 4G 创建23526个对象 执行5次GC 最大耗时 30ms
---
> java -XX:+UseParallelGC -Xms8g -Xmx8g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [PSYoungGen: 2097664K->320646K(2446848K)] 2097664K->320654K(8039424K), 0.0491485 secs] [Times: user=0.13 sys=0.20, real=0.05 secs]
[GC (Allocation Failure) [PSYoungGen: 2418310K->349172K(2446848K)] 2418318K->359050K(8039424K), 0.0550660 secs] [Times: user=0.08 sys=0.24, real=0.06 secs]
执行结束!共生成对象次数:16178
```
> 8G 创建16178个对象 执行2次GC 没有FullGC 最大暂停时间60ms
##CMSGC
>  java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
```text
...4
[GC (Allocation Failure) [ParNew: 157247K->17467K(157248K), 0.0189947 secs] 327006K->234837K(506816K), 0.0193638 secs] [Times: user=0.09 sys=0.02, real=0.02 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 217369K(349568K)] 238689K(506816K), 0.0003224 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
...97
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
[CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (CMS Final Remark) [YG occupancy: 35585 K (157248 K)][Rescan (parallel) , 0.0002795 secs][weak refs processing, 0.0000239 secs][class unloading, 0.0002071 secs][scrub symbol table, 0.0003064 secs][scrub string table, 0.0001423 secs][1 CMS-remark: 346686K(349568K)] 382271K(506816K), 0.0016367 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [ParNew: 139776K->139776K(157248K), 0.0004719 secs][CMS: 346194K->349450K(349568K), 0.0419734 secs] 485970K->355141K(506816K), [Metaspace: 2780K->2780K(1056768K)], 0.0431307 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [CMS: 349558K->349225K(349568K), 0.0399244 secs] 506774K->356536K(506816K), [Metaspace: 2780K->2780K(1056768K)], 0.0403436 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 349225K(349568K)] 359465K(506816K), 0.0006158 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
执行结束!共生成对象次数:12219
```
> 512m 创建12219个对象 
>Initial Mark(初始标记) 
>concurrent-mark(并发标记) 
>concurrent-preclean(并发预处理) 
>Final Remark(最终标记) 
>concurrent-sweep(并发清除) 
>concurrent-reset(并发重置)
---
> java -XX:+UseConcMarkSweepGC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
```text
[GC (Allocation Failure) [ParNew: 545344K->68095K(613440K), 0.0214081 secs] 545344K->153030K(4126208K), 0.0218766 secs] [Times: user=0.16 sys=0.09, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 613439K->68094K(613440K), 0.0255901 secs] 698374K->272889K(4126208K), 0.0258370 secs] [Times: user=0.13 sys=0.13, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 613438K->68096K(613440K), 0.0516701 secs] 818233K->396685K(4126208K), 0.0520082 secs] [Times: user=0.34 sys=0.02, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68094K(613440K), 0.0518510 secs] 942029K->528382K(4126208K), 0.0522047 secs] [Times: user=0.34 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613438K->68096K(613440K), 0.0500218 secs] 1073726K->654587K(4126208K), 0.0503121 secs] [Times: user=0.34 sys=0.02, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0497898 secs] 1199931K->773306K(4126208K), 0.0501950 secs] [Times: user=0.34 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0524862 secs] 1318650K->902377K(4126208K), 0.0526766 secs] [Times: user=0.45 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0514341 secs] 1447721K->1027924K(4126208K), 0.0516450 secs] [Times: user=0.34 sys=0.02, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0576521 secs] 1573268K->1157854K(4126208K), 0.0578399 secs] [Times: user=0.47 sys=0.01, real=0.06 secs]
执行结束!共生成对象次数:18670
```
> 4G 创建18670个对象 执行9次Young GC 耗时最高 60ms 
> 
> 没有执行CMSGC 也没有执行 FullGC 和 并行GC Xmx4g比起来 效率下降很多 
---
##G1GC
> java -XX:+UseG1GC -Xms512m -Xmx512m -XX:+PrintGC GCLogAnalysis
```text
[GC pause (G1 Evacuation Pause) (young) 34M->9130K(512M), 0.0027362 secs]
[GC pause (G1 Evacuation Pause) (young) 44M->24M(512M), 0.0034431 secs]
[GC pause (G1 Evacuation Pause) (young) 134M->59M(512M), 0.0053520 secs]
[GC pause (G1 Evacuation Pause) (young) 127M->77M(512M), 0.0052480 secs]
[GC pause (G1 Evacuation Pause) (young) 197M->117M(512M), 0.0063950 secs]
[GC pause (G1 Evacuation Pause) (young) 255M->161M(512M), 0.0067946 secs]
[GC pause (G1 Evacuation Pause) (young) 331M->214M(512M), 0.0079076 secs]
[GC pause (G1 Humongous Allocation) (young) (initial-mark) 232M->220M(512M), 0.0049159 secs]
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0002041 secs]
[GC concurrent-mark-start]
[GC concurrent-mark-end, 0.0013820 secs]
[GC remark, 0.0011094 secs]
[GC cleanup 236M->236M(512M), 0.0006646 secs]
... 264
[Full GC (Allocation Failure)  417M->339M(512M), 0.0305937 secs]
... 25
[GC pause (G1 Evacuation Pause) (young) 398M->373M(512M), 0.0022739 secs]
[GC pause (G1 Evacuation Pause) (mixed)-- 399M->395M(512M), 0.0031704 secs]
[GC pause (G1 Humongous Allocation) (young) (initial-mark) 399M->395M(512M), 0.0017479 secs]
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0001125 secs]
[GC concurrent-mark-start]
[GC concurrent-mark-end, 0.0017713 secs]
[GC pause (G1 Humongous Allocation) (young)-- 417M->410M(512M), 0.0025796 secs]
[GC remark, 0.0010720 secs]
[GC cleanup 411M->411M(512M), 0.0003674 secs]
执行结束!共生成对象次数:12070
```
> 512m 创建12070个对象 
---
> java -XX:+UseG1GC -Xms4g -Xmx4g -XX:+PrintGC GCLogAnalysis
```text
[GC pause (G1 Evacuation Pause) (young) 204M->73M(4096M), 0.0093373 secs]
[GC pause (G1 Evacuation Pause) (young) 251M->133M(4096M), 0.0098042 secs]
[GC pause (G1 Evacuation Pause) (young) 311M->191M(4096M), 0.0097820 secs]
[GC pause (G1 Evacuation Pause) (young) 369M->249M(4096M), 0.0090772 secs]
[GC pause (G1 Evacuation Pause) (young) 427M->301M(4096M), 0.0089592 secs]
[GC pause (G1 Evacuation Pause) (young) 479M->360M(4096M), 0.0091250 secs]
[GC pause (G1 Evacuation Pause) (young) 538M->419M(4096M), 0.0093148 secs]
[GC pause (G1 Evacuation Pause) (young) 623M->488M(4096M), 0.0121592 secs]
[GC pause (G1 Evacuation Pause) (young) 762M->578M(4096M), 0.0169996 secs]
[GC pause (G1 Evacuation Pause) (young) 872M->661M(4096M), 0.0176929 secs]
[GC pause (G1 Evacuation Pause) (young) 1035M->760M(4096M), 0.0199027 secs]
[GC pause (G1 Evacuation Pause) (young) 1206M->891M(4096M), 0.0257154 secs]
[GC pause (G1 Evacuation Pause) (young) 1633M->1057M(4096M), 0.0319675 secs]
[GC pause (G1 Evacuation Pause) (young) 1721M->1193M(4096M), 0.0279043 secs]
[GC pause (G1 Evacuation Pause) (young) 2045M->1355M(4096M), 0.0366420 secs]
执行结束!共生成对象次数:20456
```
> 4g 创建20456个对象 共执行14次GC全是Young GC 耗时随大小递增 最大耗时36ms
> 耗时上低于CMSGC总的耗时高于并行GC 