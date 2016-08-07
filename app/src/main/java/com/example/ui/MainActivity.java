package com.example.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.rxjavademo.R;
import com.example.api.RestClient;
import com.example.bean.AddressListData;
import com.example.bean.AddressListDataResult;
import com.example.bean.UserInfo;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private TextView textView;
    private Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.id_text);

        btnView = (Button) findViewById(R.id.id_btn);
        
        //test
        //test

//        File cacheFile = new File(getCacheDir(), "HttpResponseCache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //10Mb
//        new OkHttpClient.Builder().cache(cache).build();

//        getLoginInfo();

//        getListInfo();

//        loginInfo();

//        createObservable();

//        flatMap();

//        merge();

//        timer();

    }

    public void getAddressList(String user_id) {
        RestClient.getInstance().getApiClient().getAddressList(user_id).enqueue(new Callback<AddressListDataResult>() {
            @Override
            public void onResponse(Call<AddressListDataResult> call, Response<AddressListDataResult> response) {
                try {
                    AddressListDataResult result = response.body();
                    if (result != null && result.status == 1) {
                        List<AddressListData> dataList = result.data;
                        textView.setText(dataList.get(0).consignee);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AddressListDataResult> call, Throwable t) {
                textView.setText("fail");
            }
        });
    }

    public void getLoginInfo() {
        Call<ResponseBody> call = RestClient.getInstance().getApiClient().login("18357482583", "654321");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String resp = response.body().string();

                    JSONTokener tokener = new JSONTokener(resp);
                    JSONObject object = (JSONObject) tokener.nextValue();
                    if (object.getInt("status") == 1) {
                        JSONObject data = object.getJSONObject("data");
                        textView.setText(data.getString("nick"));
                        //getAddressList(data.getString("uid"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                textView.setText("fail");
            }
        });
    }

    public void loginInfo() {
        RestClient.getInstance().getApiClient().loginInfo("18357482583", "654321")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserInfo result) {
                        textView.setText(result.data.uid);
                        //getAddressList(data.getString("uid"));
                    }
                });
    }

    public void getListInfo() {
        RestClient.getInstance().getApiClient().getList("15979")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressListDataResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(AddressListDataResult result) {
                        if (result != null && result.status == 1) {
                            List<AddressListData> dataList = result.data;
                            textView.setText(dataList.get(0).consignee);
                        }
                    }
                });
    }

    String str = "";

    /**
     * 观察者模式
     */
    public void createObservable() {

        //被观察者
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        ).subscribeOn(Schedulers.io());

        //观察者1
        Subscriber<String> mySubscriber1 = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                str = str + s;
                textView.setText(str);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        //观察者2
        Subscriber<String> mySubscriber2 = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                str = str + s;
                textView.setText(str);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        //订阅被观察者
        myObservable.subscribe(mySubscriber1);
        myObservable.subscribe(mySubscriber2);

//        //简单写法
//        Observable<String> myObservable = Observable.just("Hello, world!!");
//
//        //Actions可以定义Subscriber的每一个部分，Observable.subscribe()函数能够处理一个，两个或者三个Action参数，
//        // 分别表示onNext()，onError()和onComplete()函数
//        Action1<String> onNextAction = new Action1<String>() {
//            @Override
//            public void call(String s) {
//                textView.setText(s);
//            }
//        };
//
//        myObservable.subscribe(onNextAction);

//        Observable.just("Hello, world!")
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        textView.setText(s);
//                    }
//                });

    }

    /**
     * map变换
     * 所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列。
     */
    public void map() {
        Observable.just("1") // 输入类型 String
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String number) { // 参数类型 String
                        return Integer.parseInt(number); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) { // 参数类型 Bitmap
                        textView.setText(number + "");
                    }
                });
    }

    public void flatMap() {
        RestClient.getInstance().getApiClient().loginInfo("18357482583", "654321")
                .flatMap(new Func1<UserInfo, Observable<AddressListDataResult>>() {
                    @Override
                    public Observable<AddressListDataResult> call(UserInfo userInfo) {
                        return RestClient.getInstance().getApiClient().getList(userInfo.data.uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressListDataResult>() {
                    @Override
                    public void onNext(AddressListDataResult dataResult) {
                        if (dataResult != null && dataResult.status == 1) {
                            List<AddressListData> dataList = dataResult.data;
                            textView.setText(dataList.get(0).consignee);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    /**
     * 合并请求数据
     * onNext数据按方法调用顺序执行：黄劲-15979
     */
    public void merge() {
        Observable.merge(RestClient.getInstance().getApiClient().getList("15979"), RestClient.getInstance().getApiClient().loginInfo("18357482583", "654321"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object obj) {
                        String msg = "";
                        if (obj instanceof UserInfo) {
                            //msg = ((UserInfo) obj).data.uid;
                            Toast.makeText(MainActivity.this, ((UserInfo) obj).data.uid, Toast.LENGTH_SHORT).show();
                        } else if (obj instanceof AddressListDataResult) {
                            //msg = msg + ((AddressListDataResult)obj).data.get(0).consignee;
                            Toast.makeText(MainActivity.this, ((AddressListDataResult) obj).data.get(0).consignee, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 实现定时器功能
     */
    public void timer() {
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Log", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // log.e("error");
                    }

                    @Override
                    public void onNext(Long number) {
                        //log.d ("hello world");
                        Log.d("Log", "hello");
                    }
                });
    }



}
