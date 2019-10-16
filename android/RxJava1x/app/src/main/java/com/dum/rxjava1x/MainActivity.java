package com.dum.rxjava1x;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(view -> {
            go();
        });
    }

    private void go() {
        Observable observable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Log.d(TAG, "call");
                //subscriber.onCompleted();
                subscriber.onError(new java.net.SocketTimeoutException());
            }
        });

        observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    private int retryCount;
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        Log.d(TAG, "retryWhen");
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                Log.d(TAG, "flatMap " + retryCount);
                                if (retryCount < 4) {
                                    if (throwable instanceof java.net.SocketTimeoutException ||
                                            throwable instanceof javax.net.ssl.SSLException ||
                                            //throwable instanceof com.google.api.client.googleapis.json.GoogleJsonResponseException ||
                                            throwable instanceof java.net.ConnectException ||
                                            throwable instanceof java.net.UnknownHostException) {
                                        retryCount++;
                                        return Observable.timer(5, TimeUnit.SECONDS);
                                    }
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribe(new Observer() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
                Log.d(TAG, "Thread " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
                Log.d(TAG, "Thread " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext");
                Log.d(TAG, "Thread " + Thread.currentThread().getName());
            }
        });
    }
}
