#排序
##选择排序
选择排序（Selection sort）是一种简单直观的排序算法。它的工作原理是每一次从待排序的数据元素中选出最小（或最大）的一个元素，存放在序列的起始位置，直到全部待排序的数据元素排完。 选择排序是不稳定的排序方法（比如序列[5， 5， 3]第一次就将第一个[5]与[3]交换，导致第一个5挪动到第二个5后面）。
###思路
对比数组中前一个元素跟后一个元素的大小，如果后面的元素比前面的元素小则用一个变量k来记住他的位置，接着第二次比较，前面“后一个元素”现变成了“前一个元素”，继续跟他的“后一个元素”进行比较如果后面的元素比他要小则用变量k记住它在数组中的位置(下标)，等到循环结束的时候，我们应该找到了最小的那个数的下标了，然后进行判断，如果这个元素的下标不是第一个元素的下标，就让第一个元素跟他交换一下值，这样就找到整个数组中最小的数了。然后找到数组中第二小的数，让他跟数组中第二个元素交换一下值，以此类推。
###Code
```
  //选择排序
    void selectionSort(int[] arrays) {

//        for (int i = 0; i < arrays.length; i++) {
//            for (int j = i+1; j < arrays.length; j++) {
//                if (arrays[i]>arrays[j])
//                    swap(arrays,i,j);
//            }
//        }
        //改良 加入个index记录位置避免重复换位置
        int minIndex=0;
        for (int i = 0; i < arrays.length-1; i++) {
            minIndex=i;
            for (int j = i + 1; j < arrays.length; j++) {
                if (arrays[minIndex] > arrays[j])
                    minIndex=j;
            }
            if (minIndex!=i)
            swap(arrays, i, minIndex);

        }

    }


```


##冒泡排序
冒泡排序（Bubble Sort），是一种计算机科学领域的较简单的排序算法。
它重复地走访过要排序的数列，一次比较两个元素，如果他们的顺序错误就把他们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。
这个算法的名字由来是因为越大的元素会经由交换慢慢“浮”到数列的顶端，故名。
###思路
遍历数组把两个相邻的数比较位置，如果前面的比后面的大则互相替换位置，这样最大的一位数就放在最后，再一直重复此操作，直到没有任何一对数字可以替换位置。
###实现
```
//冒泡排序
    void bubbleSort(int s[]){
        for (int i = 0; i < s.length-1 ; i++) {
            for (int j = 0; j <s.length-i-1; j++) {
                if (s[j+1]<s[j])
                {
                   swap(s,j,j+1);
                }
            }
        }
    }
```

##快速排序
快速排序是在冒泡排序的一种改进，它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
###思路
首先选择一个数作为关键数（一般选用数组第一个数，也可以选择一个随机数）,然后根据这个关建数先从右到左找到一个比他小的数替换位置，然后再从左到右寻找一个比他大的数继续替换位置，这样递归下去实现快速排序。
###代码实现
```
//快速排序
    void quick_sort(int s[], int l, int r)
    {
        if (l < r)
        {
            //Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
            int i = l, j = r, x = s[l];
            while (i < j)
            {
                while(i < j && s[j] >= x) // 从右向左找第一个小于x的数
                    j--;
                if(i < j)
                    s[i++] = s[j];

                while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数
                    i++;
                if(i < j)
                    s[j--] = s[i];
            }
            s[i] = x;
            quick_sort(s, l, i - 1); // 递归调用
            quick_sort(s, i + 1, r);
        }
    }
```
##排序比较
![sort pic](https://github.com/litian1a/AndrodNote/blob/master/pic/sortPic.jpg)

#查找
##二分查找
当数据量很大适宜采用该方法。采用二分法查找时，数据需是排好序的。主要思想是：（设查找的数组区间为array[low, high]）
##思路
（1）确定该区间的中间位置K（2）将查找的值T与array[k]比较。若相等，查找成功返回此位置；否则确定新的查找区域，继续二分查找。区域确定如下：a.array[k]>T 由数组的有序性可知array[k,k+1,……,high]>T;故新的区间为array[low,……，K-1]b.array[k]<T 类似上面查找区间为array[k+1,……，high]。每一次查找与中间值比较，可以确定是否查找成功，不成功当前查找区间缩小一半，递归找，即可。时间复杂度:O(log2n)。


##Code
```

  //二分查找  对有序数组才有效

    /**
     *
     * @param s 查找的数组
     * @param x 查找的数
     * @return  返回查找数的角标  没有则返回-1
     */
    int  binarySearch(int s[],int x){
        int low=0;
        int high=s.length-1;
        while ( low<=high){
            int middle =(low+high)/2;
            if (x==s[middle])
                return middle;
            if (x>s[middle])
                low=middle+1;
            if (x<s[middle])
                high=middle-1;

        }
        return -1;
    }


