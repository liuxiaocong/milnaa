package androiddev.milnaa.com.milnaa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
public class LoginActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    String TAG = "LoginActivity";
    @BindView(R.id.signUp)
    TextView mSignUp;

    @BindView(R.id.name)
    EditText mName;

    @BindView(R.id.pwd)
    EditText mPassword;

    LoginService mLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.milnaa.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        mLoginService = retrofit.create(LoginService.class);

    }

    @OnClick(R.id.signUp)
    public void onClickSignUp(View view) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.login)
    public void onClickLogin(View view) {
        if (Util.isNullOrEmpty(Util.getValue(mName))) {
            MyApplication.getInstance().showNote("You must input your username (email)");
            return;
        }
        if (Util.isNullOrEmpty(Util.getValue(mPassword))) {
            MyApplication.getInstance().showNote("You must input your password");
            return;
        }
        showLoadingBar("Log in..");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mailId", Util.getValue(mName));
            jsonObject.put("Password", Util.getValue(mPassword));
            String requestBodyStr = jsonObject.toString();
            Util.DLog(TAG, "requestBody:" + requestBodyStr);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);
            mLoginService.login(requestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                            dismissLoadingBar();
                            Log.d("onCompleted", "onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoadingBar();
                            Log.d("Error", e.toString());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            dismissLoadingBar();
                            try {
                                String res = responseBody.string();
                                LoginResultModel loginResultModel = GsonImpl.get().toObject(res, LoginResultModel.class);
                                MyApplication.getInstance().setLoginResultModel(loginResultModel);
                                Util.DLog(TAG, "login result:" + res);
                                if (loginResultModel.getResponse().getCode() == 0) {
                                    SharedPreferencesFactory.setMid(LoginActivity.this, Util.getFromEditText(mName, ""));
                                    SharedPreferencesFactory.setPASSWORD(LoginActivity.this, Util.getFromEditText(mPassword, ""));
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    LoginActivity.this.finish();
                                } else {
                                    MyApplication.getInstance().showNote("Username(email) or password error");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            dismissLoadingBar();
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
