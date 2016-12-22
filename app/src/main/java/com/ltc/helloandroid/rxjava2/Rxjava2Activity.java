package com.ltc.helloandroid.rxjava2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        Disposable subscribe = Flowable.create((FlowableOnSubscribe<Integer>) e -> {

            for (int i = 0; i < 10000; i++) {
                e.onNext(i);
            }
            e.onComplete();
        }, BackpressureStrategy.DROP) //指定背压处理策略，抛出异常
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(integer -> {
                    Log.d("JG", integer.toString());
                    Thread.sleep(100);
                }, throwable -> Log.d("JG", throwable.toString()));
            mCompositeDisposable.add(subscribe);
//        Flowable.create(new FlowableOnSubscribe<Long>() {
//            @Override
//            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
//                Observable.interval(10, TimeUnit.MILLISECONDS)
//                        .take(Integer.MAX_VALUE)
//                        .subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//
//                                e.onNext(aLong);
//
//                            }
//                        });
//            }
//        }, BackpressureStrategy.DROP)
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(2);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.i(TAG, "onNext: " + aLong);
//                        aLong/=0;
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.i(TAG, "onError: ");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        Flowable<Long> flowable =
//                Flowable.create(e -> {
//                    Observable.interval(1, TimeUnit.MILLISECONDS)
//                            .take(Integer.MAX_VALUE)
//                            .subscribe(e::onNext);
//                }, BackpressureStrategy.DROP);
//
//
//        Observable<Long> observable =
//                Observable.create((ObservableOnSubscribe<Long>) e -> {
//                    Observable.interval(1, TimeUnit.MILLISECONDS)
//                            .take(Integer.MAX_VALUE)
//                            .subscribe(e::onNext);
//                });
//        flowable.subscribe(i -> {
//            Thread.sleep(100);
//            Log.v("TEST", "out : " + i);
//        });
//        observable.subscribe(i -> {
//            Thread.sleep(100);
//            Log.v("TEST", "out2 : " + i);
////        });
//    Flowable.create(new FlowableOnSubscribe<Integer>() {
//        @Override
//        public void subscribe(FlowableEmitter<Integer> e) throws Exception {
//            int i=0;
//            e.onNext(++i);
//            e.onNext(++i);
//            e.onComplete();
//        }
//    },BackpressureStrategy.BUFFER).subscribe(new Subscriber<Integer>() {
//        @Override
//        public void onSubscribe(Subscription s) {
//            s.request(1);
//        }
//
//        @Override
//        public void onNext(Integer integer) {
//            Log.i(TAG, "onNext: ");
//        }
//
//        @Override
//        public void onError(Throwable t) {
//            Log.i(TAG, "onError: ");
//
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
