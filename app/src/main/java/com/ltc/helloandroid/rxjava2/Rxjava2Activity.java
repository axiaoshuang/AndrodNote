package com.ltc.helloandroid.rxjava2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ltc.helloandroid.MainActivity;
import com.ltc.helloandroid.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.schedulers.Schedulers;

public class Rxjava2Activity extends AppCompatActivity {

    private static final String TAG = "Rxjava2Activity";
    @Bind(R.id.activity_rxjava2)
    LinearLayout mActivityRxjava2;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);
//        //支持背压
        Flowable.create((FlowableOnSubscribe<Integer>) e -> {

            for (int i = 0; i < 127; i++) {
                e.onNext(i);
            }
            e.onComplete();
        }, BackpressureStrategy.ERROR) //指定背压处理策略，抛出异常
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i(TAG, "onError: " + t.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(integer -> {
//                    Log.d("JG", integer.toString());
//                    Thread.sleep(100);
//                }, throwable -> Log.d("JG", throwable.toString()));

        //   mCompositeDisposable.add(subscribe);

        //不支持背压
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                for (int i = 0; i < 1000; i++) {
//                    e.onNext(String.valueOf(i));
//                }
//                e.onComplete();
//            }
//        })
//                .observeOn(Schedulers.computation())
//                .subscribeOn(Schedulers.newThread())
//        .subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                mCompositeDisposable.add(d);
//            }
//
//            @Override
//            public void onNext(String s) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.i(TAG, "onNext: "+s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        })
//        ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
