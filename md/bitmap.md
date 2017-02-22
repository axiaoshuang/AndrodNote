#Bitmap内存分析
参考文档<http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=498>

从Android提供的获取bitmap内存大小api如下：
```

    /**
     * Returns the minimum number of bytes that can be used to store this bitmap's pixels.
     *
     * <p>As of {@link android.os.Build.VERSION_CODES#KITKAT}, the result of this method can
     * no longer be used to determine memory usage of a bitmap. See {@link
     * #getAllocationByteCount()}.</p>
     */
    public final int getByteCount() {
        // int result permits bitmaps up to 46,340 x 46,340
        return getRowBytes() * getHeight();
    }
```

以上代码分析height就是原图片的高，而getRowBytes的值与bitmap格式有关，如果ARGB-8888 每个像素占8bit 总共32bit，4字节，所以需要*4，有关bitmap格式表对应的字节如下：

| 格式  | 描述 | 字节 |
|:------------- |:---------------:| -------------:|
| ALPHA_8  | 只有一个alpha通道 每个像素占4bit|0.5
 ARGB-4444|         每个像素占四bit，即A=4，R=4，G=4，B=4 |2
| RGB-565    | 比较特殊 R=5 G=6 B=5 无ALPHA通道      |          2  |
 ARGB-8888 |  每个像素占四bit，即A=8，R=8，G=8，B=8        |            4 |
 
 但是bitmap格式也只是以上决定因素之一，另外还需要和屏幕分辨率(Density)有关。一般来说如果放在xxhdpi为480 三星手机的密度为640
 所以公式为手机本身密度/图片存放位置的密度*宽才能拿到他实际的宽，高也同理。最后再加上精度问题之前的宽高需要+0.5f。所以最后得出来的公式：
 
 （原图片宽* 手机的密度 / 存放位置的密度（例如xhdpi）+0.5f）*
 （原图片高* 手机的密度 / 存放位置的密度（例如xhdpi）+0.5f）*
 图片格式（默认ARGB8888为4）=bitmap占用内存大小。








###Bitmap内存优化

####使用inSampleSize
inSampleSize就是降低图片的采样率，如果为2的话那加载的内存则会是原来的1/4。

####jpg vs png
jpg属于有损压缩，而png属于无损压缩，，有损压缩删除一些图像变化，让人脑自己去补充，所以体积会比有损小，但是造成的弊端显而易见。有损压缩一般人眼识别都都大致一样但是有损压缩在高分辨下打印出来就能看到明显的损坏，而无损则不会有。另外jpg走的是rgb565 从内存方面考虑会比png（ARGB-8888）小一半左右，但是这也要取决于图片额内容。

两者在选择时候主要体现于：

* 是否真的需要alpha通道
* 用户的cpu是否强劲，jpg的有损压缩算法会比png的无损更加耗时。
* 看你的色值是否丰富，如果丰富请选择jpg，如果只是作为背景可以选择png。

####使用矩阵
大图小用用采样，小图大用用矩阵。

####使用合理的图片格式

ALPHA8 没必要用，因为我们随便用个颜色就可以搞定的。

ARGB4444 虽然占用内存只有 ARGB8888 的一半，不过已经被官方嫌弃，失宠了。。『又要占省内存，又要看着爽，臣妾做不到啊T T』。

ARGB8888 是最常用的，大家应该最熟悉了。

RGB565 看到这个，我就看到了资源优化配置无处不在，这个绿色。。（不行了，突然好邪恶XD），其实如果不需要 alpha 通道，特别是资源本身为 jpg 格式的情况下，用这个格式比较理想。

####使用自定义view
复写ondraw方法去绘制图形。