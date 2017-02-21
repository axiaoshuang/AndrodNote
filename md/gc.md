#GC算法

参考（抄袭）文献：<http://www.cnblogs.com/sunfie/p/5125283.html>


由于java是面向对象语言，所以就要考虑到他的对象回收，不然的话很容易造成oom（内存溢出），所以java会有一个单独的gc线程去处理java里面不使用的对象的回收。但是那些对象可以被回收？什么时候回收？采用什么方式回收？这就是gc算法需要做的事。比较老的有引用计数算法，原理是有一个引用，则+1；删除一个引用则-1。但是这种处理方式显然不适合java
当A引用B，B再引用A，两者同时置空的时候则对象还是不能被回收。由于这种算法的缺陷，jvm一般采用的算法是根搜索算法，他的处理方式是，**设立若干种根对象，当任何一个根对象到某一个对象均不可到达时，则认为这个对象是可以回收的**。不过垃圾收集显然还需要解决后两个问题，**什么时候回收以及如何回收**，在根搜索算法的基础上，现代虚拟机的实现当中，垃圾搜集的算法主要有三种，**分别是标记-清除算法、复制算法、标记-整理算法**，这三种算法都扩充了根搜索算法。
###标记/清除算法


标记：就是遍历gcRoot，找到存活的对象，并给他们打上标记。

清除：在标记之后执行，将之前未标记上的对象给清除，然后将之前标记上的对象的标记置空。

通俗的讲就是程序发现内存不够的时候，gc线程就会触发将当前应用程序暂停，然后进行遍历打上标记，然后清除未打上标记的对象再清除之前的标记，最后程序恢复运行。
标记/清除算法的优势是占用空间比较小，但是效率比较低，而且会导致之前的排列杂乱无章，而为了应付这一点，JVM就不得不维持一个内存的空闲列表，这又是一种开销。而且在分配数组对象的时候，寻找连续的内存空间会不太好找。
###复制算法
复制算法就是将内存一分为二，分为空闲区和活动区，当活动区的内存达到上限时，则将活动区的所有存活对象按照原来的顺序呢复制到空闲区，然后把活动区和内存去相互置换，同时也把之前的活动区的辣鸡对象清除。

复制算法的优势在于效率很高，但是效率高的代价就是占用两倍的内存。而且当对象存活率非常高的时候，这种开销是不可忽视的。

###标记/整理算法
标记：就是遍历gcRoot，找到存活的对象，并给他们打上标记。

整理：移动所有之前标记的存活对象，且按照内存地址值排列，然后将具有内存地址值之后的所有内存清空。

此算法标记过程同上（标记/清除算法）一样，而整理过程和复制算法一样，但是没有内存划分这么一说，少了不必要的内存开销。记/整理算法唯一的缺点就是效率也不高，不仅要标记所有存活对象，还要整理所有存活对象的引用地址。从效率上来说，标记/整理算法要低于复制算法


###分代搜集算法
分代搜集算法是针对对象的不同特性，而使用适合的算法，这里面并没有实际上的新算法产生。与其说分代搜集算法是第四个算法，不如说它是对前三个算法的实际应用。他可以划分成三个区域，新生代，老年代，永久代；

新生代：一般存放些生命周期比较短的对象；比如：局部变量，和一些临时变量。因为生命周期短如此所以他比较适合复制算法。考虑到比较占内存所以只给了两块10%的内存。新生代的每个对象都有年龄（在gc操作下存活下来的次数），当达到一定年龄的时候则会进入老年代。又或者当新生代内存满（10%）的时候，对象会进入老年代，可以说老年代是新生代的备用仓库。

老年代：存放生命周期比较长的对象；比如：缓存对象、数据库连接对象、单例对象（单例模式）等等。适合标记/整理或者标记/清除算法。

永久代（java8改为享元空间）：一旦出生几乎很难死去，一般存放在方法区。比如：String池中的对象（享元模式）、加载过的类信息等等。适合标记/整理或者标记/清除算法。


###回收时机
JVM进行gc回收的时候大部分都是进行新生代的回收，因此gc按照回收的类型，又可以分为普通回收，和全局回收。普通回收主要针对的是新生代，而全局回收主要针对的是老年代。
