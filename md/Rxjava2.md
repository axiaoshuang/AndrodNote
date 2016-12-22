#Rxjava2
##Rxjava1 和 Rxjava2区别
他们最大的不同在于Rxjava1所有的Observable都具有背压概念 但是有时我们并不需要这个功能 所以Rxjava2将背压策略单独的抽取出Flowable类 不需要背压策略的则使用Observable类。现在Rxjava2的Action可以抛出异常了 也就是说在onNext中出错的代码并不会走入onError 而是直接对外抛出。
另外就是rxjava2对api做了一些调整 见下文。
###背压(Backpressure)
在rxjava中会经常遇到一种情况就是被观察者发送消息太快以至于它的操作符或者订阅者不能及时处理相关的消息。那么随之而来的就是如何处理这些未处理的消息。

举个例子，使用zip操作符将两个无限大的Observable压缩在一起，其中一个被观察者发送消息的速度是另一个的两倍。一个比较不靠谱的做法就是把发送比较快的消息缓存起来，当比较慢的Observable发送消息的时候取出来并将他们结合在一起。这样做就使得rxjava变得笨重而且十分占用系统资源。

在rxjava中有多重控制流以及背压（backpressure）策略用来应对当一个快速发送消息的被观察者遇到一个处理消息缓慢的观察者。下面的解释将会向你展示你应当怎么设计属于你自己的被观察者和操作符去应对流量控制（flow control）。--来自官方 文档
####解决方式有以下几种:
#####1.Throttling节流  
sample 操作符定期收集observable发送的数据items，并发射出最后一个数据item。 
 
 throttleFirst跟sample有点类似，但是并不是把观测到的最后一个item发送出去，而是把该时间段第一个item发送出去。
   
debounce操作符会只发送两个在规定间隔内的时间发送的序列的最后一个。 

buffer操作符在突发期间你可以得到的想要的，并在缓冲区收集数据和最终在突发结束的时候释放缓存。使用debounce操作符释放缓存并关闭指示器buffer操作符。


#####2.阻塞
因为Android很多情况刷新ui在主线程会造成主线程阻塞所以这个方案不予考虑

#####3.背压
当subscribe订阅observable的时候可以通过调用subscribe.request（n），n是你想要的observable发送出来的量。

当在onNext()方法里处理完数据itme后，你能重新调用 request()方法，通知Observable发射数据items。

###Rxjava2使用背压

```
   
        Flowable.create(new FlowableOnSubscribe<Integer>() {

            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {

                for(int i=0;i<10000;i++){
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER) //指定背压处理策略，抛出异常
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("JG", integer.toString());
                        Thread.sleep(100);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("JG",throwable.toString());
                    }
                });
```
如上代码可以造成生产者大于消费者的情况,我们可以使用BackpressureStrategy模式去解决他. 

BackpressureStrategy一共分为五种情况 MISSING ERROR BUFFER DROP LATEST  

MISSING:当过度生产的observable时MISSING则会向subscrber抛出MissingBackpressureException异常并将后面数据全部丢弃 如上代码当为MISSING模式时运行结果如下:

```
12-21 18:01:59.999 3376-3560/com.ltc.helloandroid D/JG: 0

12-21 18:01:59.999 3376-3560/com.ltc.helloandroid D/JG: io.reactivex.exceptions.MissingBackpressureException: Queue is full?!
```
ERROR:和MISSING类似但是只要出现错误 onNext不会被调用 而MISSING会保留之前发射的数据 结果：

```
12-21 18:06:38.438 11098-11481/com.ltc.helloandroid D/JG: io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests

```
BUFFER:这个模式会一直在占用内存直到Observable发射完毕,不会丢弃任何数据。会把上游发射的数据全部缓存下来 当上游缓存的数据过多则容易出现OOM。 结果:

```
。。。
12-21 18:28:22.054 15895-16194/com.ltc.helloandroid D/JG: 126

12-21 18:28:22.156 15895-16194/com.ltc.helloandroid D/JG: 127 

12-21 18:28:22.260 15895-16194/com.ltc.helloandroid D/JG: 128 

12-21 18:28:22.361 15895-16194/com.ltc.helloandroid D/JG: 129

12-21 18:28:22.464 15895-16194/com.ltc.helloandroid D/JG: 130
。。。
```
DROP 和 LATEST:这两个模式比较类似,当内部容器(默认128个)满了都会丢弃数据不同的是LATEST会把超载数据的最后一次发射的给保留下来而DROP不会。

DROP结果:

```
。。。
12-21 18:28:22.054 15895-16194/com.ltc.helloandroid D/JG: 126

12-21 18:28:22.156 15895-16194/com.ltc.helloandroid D/JG: 127 
```
LATEST结果:

```
。。。
12-21 18:28:22.054 15895-16194/com.ltc.helloandroid D/JG: 126

12-21 18:28:22.156 15895-16194/com.ltc.helloandroid D/JG: 127 
12-21 18:28:22.189 15895-16194/com.ltc.helloandroid D/JG: 9999

```









