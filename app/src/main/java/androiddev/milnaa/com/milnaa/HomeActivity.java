package androiddev.milnaa.com.milnaa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import androiddev.milnaa.com.milnaa.common.GsonImpl;
import androiddev.milnaa.com.milnaa.common.SharedPreferencesFactory;
import androiddev.milnaa.com.milnaa.common.Util;
import androiddev.milnaa.com.milnaa.model.LoginResultModel;
import androiddev.milnaa.com.milnaa.service.LoginService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LiuXiaocong on 9/19/2016.
 */
public class HomeActivity extends BaseActivity {
    @BindView(R.id.milnaauserid)
    TextView mMilnaauserid;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.logout)
    TextView mLogout;
    private LoginService mLoginService;
    String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        String userId = SharedPreferencesFactory.getMid(this);
        String pwd = SharedPreferencesFactory.getPASSWORD(this);
        LoginResultModel loginResultModel = MyApplication.getInstance().getLoginResultModel();
        if (loginResultModel != null) {
            mMilnaauserid.setText("welcome " + loginResultModel.getResult().getUserFullName());
            mEmail.setText(loginResultModel.getResult().getUserName() + "");
        } else {
            if (Util.isNullOrEmpty(userId) || Util.isNullOrEmpty(pwd)) {
                MyApplication.getInstance().showNote("Auto login fail");
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            }
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.milnaa.com/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            mLoginService = retrofit.create(LoginService.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mailId", SharedPreferencesFactory.getMid(HomeActivity.this));
                jsonObject.put("Password", SharedPreferencesFactory.getPASSWORD(HomeActivity.this));
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
                                    if (loginResultModel != null) {
                                        MyApplication.getInstance().setLoginResultModel(loginResultModel);
                                        mMilnaauserid.setText("welcome " + loginResultModel.getResult().getUserFullName());
                                        mEmail.setText(loginResultModel.getResult().getUserName() + "");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.logout)
    public void onClickLogout() {
        MyApplication.getInstance().setLoginResultModel(null);
        SharedPreferencesFactory.setMid(this, "");
        SharedPreferencesFactory.setPASSWORD(this, "");
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        finish();
    }
}
