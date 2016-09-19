package androiddev.milnaa.com.milnaa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import androiddev.milnaa.com.milnaa.common.SharedPreferencesFactory;
import androiddev.milnaa.com.milnaa.common.Util;
import androiddev.milnaa.com.milnaa.service.LoginService;
import butterknife.BindView;
import butterknife.ButterKnife;
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
public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.milnaauserid)
    TextView mMilnaauserid;
    @BindView(R.id.result)
    TextView mResult;
    @BindView(R.id.login_wrap)
    View mLoginWrap;
    LoginService mLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        String userId = SharedPreferencesFactory.getMid(this);
        String pwd = SharedPreferencesFactory.getPASSWORD(this);
        mMilnaauserid.setText(userId);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.milnaa.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        mLoginService = retrofit.create(LoginService.class);
        if (!Util.isNullOrEmpty(userId) && !Util.isNullOrEmpty(pwd)) {
            mLoginWrap.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mailId", userId);
                jsonObject.put("Password", pwd);
                String requestBodyStr = jsonObject.toString();
                Util.DLog("login", "requestBody:" + requestBodyStr);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);
                mLoginService.login(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                                Log.d("onCompleted", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("Error", e.toString());
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                mLoginWrap.setVisibility(View.GONE);
                                try {
                                    String res = responseBody.string();
                                    Util.DLog("login", "login result:" + res);
                                    mResult.setText(res);
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
}
