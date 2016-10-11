package androiddev.milnaa.com.milnaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import androiddev.milnaa.com.milnaa.common.GsonImpl;
import androiddev.milnaa.com.milnaa.common.SharedPreferencesFactory;
import androiddev.milnaa.com.milnaa.common.Util;
import androiddev.milnaa.com.milnaa.model.LoginResultModel;
import androiddev.milnaa.com.milnaa.service.LoginService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    private String TAG = "SplashActivity";
    private LoginService mLoginService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Util.isNullOrEmpty(SharedPreferencesFactory.getMid(SplashActivity.this))
                        || Util.isNullOrEmpty(SharedPreferencesFactory.getPASSWORD(SplashActivity.this))) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    showLoadingBar("Auto login..");
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://www.milnaa.com/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(httpClient.build())
                            .build();
                    mLoginService = retrofit.create(LoginService.class);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mailId", SharedPreferencesFactory.getMid(SplashActivity.this));
                        jsonObject.put("Password", SharedPreferencesFactory.getPASSWORD(SplashActivity.this));
                        String requestBodyStr = jsonObject.toString();
                        Util.DLog(TAG, "requestBody:" + requestBodyStr);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);
                        mLoginService.login(requestBody)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ResponseBody>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(TAG, "onCompleted");
                                        dismissLoadingBar();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, e.toString());
                                        dismissLoadingBar();
                                    }

                                    @Override
                                    public void onNext(ResponseBody responseBody) {
                                        dismissLoadingBar();
                                        try {
                                            String res = responseBody.string();
                                            LoginResultModel loginResultModel = GsonImpl.get().toObject(res, LoginResultModel.class);
                                            if(loginResultModel.getResponse().getCode() == 0) {
                                                MyApplication.getInstance().setLoginResultModel(loginResultModel);
                                                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                                startActivity(i);
                                                finish();
                                            }else {
                                                MyApplication.getInstance().showNote("Auto login fail");
                                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, 500);
    }
}
