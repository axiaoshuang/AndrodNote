package com.ltc.helloandroid.sort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ltc.helloandroid.R;
import com.ltc.helloandroid.bindview.MyBindView;
import com.ltc.helloandroid.bindview.Onclick;

import java.util.Arrays;

public class SortActivity extends AppCompatActivity {
    private int[] arrays = {4, 32, 6, 7, 3, 1, 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        MyBindView.bind(this);


    }

    @Onclick({R.id.binary_search, R.id.fast_sort, R.id.bubble_sort, R.id.selection_sort})
    public void onClick(View view) {
        if (view instanceof Button) {
            switch (view.getId()) {
                case R.id.fast_sort:
                    quick_sort(arrays, 0, arrays.length - 1);
                    break;
                case R.id.bubble_sort:
                    bubbleSort(arrays);
                    break;
                case R.id.selection_sort:
                    selectionSort(arrays);
                    break;
                case R.id.binary_search:
                    int i = binarySearch(arrays, 2);
                    Toast.makeText(this,String.valueOf(i) , Toast.LENGTH_SHORT).show();
                    return;
            }
            Toast.makeText(this, Arrays.toString(arrays), Toast.LENGTH_SHORT).show();


        }

    }

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


    //冒泡排序
    void bubbleSort(int s[]) {
        for (int i = 0; i < s.length - 1; i++) {
            for (int j = 0; j < s.length - i - 1; j++) {
                if (s[j + 1] < s[j]) {
                    swap(s, j, j + 1);
                }
            }
        }
    }

    void swap(int s[], int x, int y) {
        int temp = s[x];
        s[x] = s[y];
        s[y] = temp;
    }

    //快速排序
    void quick_sort(int s[], int l, int r) {
        if (l < r) {
            //Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
            int i = l, j = r, x = s[l];
            while (i < j) {
                while (i < j && s[j] >= x) // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    s[i++] = s[j];

                while (i < j && s[i] < x) // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    s[j--] = s[i];
            }
            s[i] = x;
            quick_sort(s, l, i - 1); // 递归调用
            quick_sort(s, i + 1, r);
        }
    }
    //二分查找  对有序数组才有效

    /**
     *
     * @param s 查找的数组
     * @param x 查找的数
     * @return  返回查找数的角标  没有则返回-1
     */
    int  binarySearch(int s[],int x){
        //先排序
        quick_sort(s,0,s.length-1);
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

}
