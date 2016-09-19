package androiddev.milnaa.com.milnaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import androiddev.milnaa.com.milnaa.common.GsonImpl;
import androiddev.milnaa.com.milnaa.common.SharedPreferencesFactory;
import androiddev.milnaa.com.milnaa.common.Util;
import androiddev.milnaa.com.milnaa.model.RegisterModel;
import androiddev.milnaa.com.milnaa.service.RegisterService;
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

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.milnaauserid)
    EditText mMilnaauserid;
    @BindView(R.id.firstname)
    EditText mFirstname;
    @BindView(R.id.lastname)
    EditText mLastname;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.dob)
    EditText mDOB;
    @BindView(R.id.emailid)
    EditText mEmailId;
    @BindView(R.id.gender)
    EditText mGender;
    @BindView(R.id.profilepicture)
    EditText mProfilepicture;
    @BindView(R.id.createdfromip)
    EditText mCreatedfromip;
    @BindView(R.id.register)
    Button mRegister;
    RegisterService mRegisterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.milnaa.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        mRegisterService = retrofit.create(RegisterService.class);
    }

    @OnClick(R.id.register)
    public void onRegister(View view) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MilnaaUserId", getFromEditText(mMilnaauserid, ""));
            jsonObject.put("FirstName", getFromEditText(mFirstname, ""));
            jsonObject.put("LastName", getFromEditText(mLastname, ""));
            jsonObject.put("Password", getFromEditText(mPassword, ""));
            jsonObject.put("DOB", getFromEditText(mDOB, ""));
            jsonObject.put("EmailId", getFromEditText(mEmailId, ""));
            jsonObject.put("Gender", getFromEditText(mGender, ""));
            jsonObject.put("ProfilePicture", getFromEditText(mProfilepicture, ""));
            jsonObject.put("CreatedfromIP", getFromEditText(mCreatedfromip, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestBodyStr = jsonObject.toString();
        Util.DLog("register", "requestBody:" + requestBodyStr);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);

        mRegisterService.register(requestBody)
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
                        try {
                            String res = responseBody.string();
                            RegisterModel registerModel = GsonImpl.get().toObject(res, RegisterModel.class);
                            if (registerModel.getStatus().getCode() == 0) {
                                SharedPreferencesFactory.setMid(MainActivity.this, getFromEditText(mMilnaauserid, ""));
                                SharedPreferencesFactory.setPASSWORD(MainActivity.this, getFromEditText(mPassword, ""));
                                MyApplication.getInstance().showNote("Register Success");
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(i);
                            } else {
                                MyApplication.getInstance().showNote("Register fail");
                            }
                            Util.DLog("register", "register:" + res);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String getFromEditText(EditText editText, String defaultvalue) {
        String ret = defaultvalue;
        if (editText != null) {
            try {
                ret = editText.getText().toString();
            } catch (Exception e) {

            }
        }
        return ret;
    }
}
