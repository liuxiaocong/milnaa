package androiddev.milnaa.com.milnaa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androiddev.milnaa.com.milnaa.common.GsonImpl;
import androiddev.milnaa.com.milnaa.common.SharedPreferencesFactory;
import androiddev.milnaa.com.milnaa.common.Util;
import androiddev.milnaa.com.milnaa.model.CheckEmailModel;
import androiddev.milnaa.com.milnaa.model.RegisterModel;
import androiddev.milnaa.com.milnaa.service.CheckEmailService;
import androiddev.milnaa.com.milnaa.service.EntRegisterService;
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

/**
 * Created by LiuXiaocong on 9/19/2016.
 */
public class SignUpActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    String TAG = "SignUpActivity";
    @BindView(R.id.sign)
    TextView mSignTextView;
    @BindView(R.id.sex)
    Spinner mSexSpinner;
    @BindView(R.id.birth)
    TextView mBirthday;
    @BindView(R.id.firstname)
    EditText mFirstname;
    @BindView(R.id.lastname)
    EditText mLastname;
    @BindView(R.id.pwd)
    EditText mPassword;
    @BindView(R.id.email)
    EditText mEmail;
    String mBirthdayText = "01-01-1970";
    String mSexText = "M";

    @BindView(R.id.agree)
    CheckBox mAgree;

    EntRegisterService mEntRegisterService;
    RegisterService mRegisterService;
    CheckEmailService mCheckEmailService;
    boolean isSpinnerFirst = true;

    @BindView(R.id.ent_wrap)
    View mEntWrap;

    @BindView(R.id.ind_wrap)
    View mIndWrap;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    @BindView(R.id.ent_name)
    EditText mEntName;
    @BindView(R.id.ent_firstname)
    EditText mEntFirstname;
    @BindView(R.id.ent_lastname)
    EditText mEntLastname;
    @BindView(R.id.ent_pwd)
    EditText mEntPassword;
    @BindView(R.id.ent_email)
    EditText mEntEmail;
    @BindView(R.id.ent_mobile)
    EditText mEntMobile;
    @BindView(R.id.ent_url)
    EditText mEntUrl;
    @BindView(R.id.ent_description)
    EditText mEntDescriptione;

    boolean mIsEnt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mSignTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mSexSpinner.setOnItemSelectedListener(this);
        List<String> sexList = new ArrayList<String>();
        sexList.add("Male");
        sexList.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexSpinner.setAdapter(dataAdapter);
        mSexSpinner.setSelection(-1);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.btnIndividual) {
                    mIsEnt = false;
                    mIndWrap.setVisibility(View.VISIBLE);
                    mEntWrap.setVisibility(View.GONE);
                } else {
                    mIsEnt = true;
                    mIndWrap.setVisibility(View.GONE);
                    mEntWrap.setVisibility(View.VISIBLE);
                }
            }
        });


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.getBaseApiUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        mRegisterService = retrofit.create(RegisterService.class);
        mCheckEmailService = retrofit.create(CheckEmailService.class);
        mEntRegisterService = retrofit.create(EntRegisterService.class);
    }

    @OnClick(R.id.sign)
    public void onClickSign(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.birth)
    public void onClickBirthday(View view) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d(TAG, "onDateSet:" + i + "," + i1 + "," + i2);
                mBirthdayText = "";
                if (i2 < 10) {
                    mBirthdayText = mBirthdayText + "0" + i2 + "-";
                } else {
                    mBirthdayText = mBirthdayText + i2 + "-";
                }
                if (i1 < 10) {
                    mBirthdayText = mBirthdayText + "0" + i1 + "-";
                } else {
                    mBirthdayText = mBirthdayText + i1 + "-";
                }
                mBirthdayText = mBirthdayText + i;
                Log.d(TAG, "onDateSet:" + mBirthdayText);
                mBirthday.setText(mBirthdayText);
                mBirthday.setTextColor(Color.parseColor("#534947"));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.join)
    public void onJoinClick(View view) {
        if (mIsEnt) {
            entSign();
        } else {
            indSign();
        }

    }

    private void entSign() {
        if (Util.isNullOrEmpty(getValue(mEntName))) {
            MyApplication.getInstance().showNote("You must input name");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntFirstname))) {
            MyApplication.getInstance().showNote("You must input first name");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntLastname))) {
            MyApplication.getInstance().showNote("You must input last name");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntPassword))) {
            MyApplication.getInstance().showNote("You must input password");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntEmail))) {
            MyApplication.getInstance().showNote("You must input email");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntMobile))) {
            MyApplication.getInstance().showNote("You must input mobile");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEntUrl))) {
            MyApplication.getInstance().showNote("You must input url");
            return;
        }


        if (!mAgree.isChecked()) {
            MyApplication.getInstance().showNote("You must agree the term and condition");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", getValue(mEntEmail));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestBodyStr = jsonObject.toString();
        Util.DLog("register", "requestBody:" + requestBodyStr);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);
        showLoadingBar("Signing..");
        mCheckEmailService.checkEmail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String res = responseBody.string();
                            CheckEmailModel checkEmailModel = GsonImpl.get().toObject(res, CheckEmailModel.class);
                            if (checkEmailModel.getResponse().getCode() == 0) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("EntEmail", getFromEditText(mEntEmail, ""));
                                    jsonObject.put("EntFirstName", getFromEditText(mEntFirstname, ""));
                                    jsonObject.put("EntLastName", getFromEditText(mEntLastname, ""));
                                    jsonObject.put("EntPassword", getFromEditText(mEntPassword, ""));
                                    jsonObject.put("EntName", getFromEditText(mEntName, ""));
                                    jsonObject.put("EntMobile", getFromEditText(mEntMobile, ""));
                                    jsonObject.put("EnterPriseDescription", getFromEditText(mEntDescriptione, ""));
                                    jsonObject.put("EnterPriseUrl", getFromEditText(mEntUrl, ""));
                                    jsonObject.put("CreatedfromIP", Util.getHostIp());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String requestBodyStr = jsonObject.toString();
                                Util.DLog(TAG, "requestBody:" + requestBodyStr);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);

                                mEntRegisterService.register(requestBody)
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
                                                try {
                                                    String res = responseBody.string();
                                                    RegisterModel registerModel = GsonImpl.get().toObject(res, RegisterModel.class);
                                                    if (registerModel.getStatus().getCode() == 0) {
                                                        SharedPreferencesFactory.setMid(SignUpActivity.this, getFromEditText(mEntEmail, ""));
                                                        SharedPreferencesFactory.setPASSWORD(SignUpActivity.this, getFromEditText(mPassword, ""));
                                                        MyApplication.getInstance().showNote("Register Success");
                                                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        MyApplication.getInstance().showNote("Register fail");
                                                    }
                                                    Util.DLog(TAG, "register:" + res);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            } else {
                                dismissLoadingBar();
                                MyApplication.getInstance().showNote("Email is not valid , please try another email");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void indSign() {
        if (Util.isNullOrEmpty(getValue(mFirstname))) {
            MyApplication.getInstance().showNote("You must input first name");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mLastname))) {
            MyApplication.getInstance().showNote("You must input last name");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mPassword))) {
            MyApplication.getInstance().showNote("You must input password");
            return;
        }
        if (Util.isNullOrEmpty(getValue(mEmail))) {
            MyApplication.getInstance().showNote("You must input email");
            return;
        }
        if (Util.isNullOrEmpty(mBirthdayText)) {
            MyApplication.getInstance().showNote("You must input birthday");
            return;
        }
        if (!mAgree.isChecked()) {
            MyApplication.getInstance().showNote("You must agree the term and condition");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", getValue(mEmail));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestBodyStr = jsonObject.toString();
        Util.DLog("register", "requestBody:" + requestBodyStr);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);
        showLoadingBar("Signing..");
        mCheckEmailService.checkEmail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String res = responseBody.string();
                            CheckEmailModel checkEmailModel = GsonImpl.get().toObject(res, CheckEmailModel.class);
                            if (checkEmailModel.getResponse().getCode() == 0) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("MilnaaUserId", getFromEditText(mEmail, ""));
                                    jsonObject.put("FirstName", getFromEditText(mFirstname, ""));
                                    jsonObject.put("LastName", getFromEditText(mLastname, ""));
                                    jsonObject.put("Password", getFromEditText(mPassword, ""));
                                    jsonObject.put("DOB", mBirthdayText);
                                    jsonObject.put("EmailId", getFromEditText(mEmail, ""));
                                    jsonObject.put("Gender", mSexText);
                                    jsonObject.put("ProfilePicture", "");
                                    jsonObject.put("CreatedfromIP", Util.getHostIp());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String requestBodyStr = jsonObject.toString();
                                Util.DLog(TAG, "requestBody:" + requestBodyStr);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyStr);

                                mRegisterService.register(requestBody)
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
                                                try {
                                                    String res = responseBody.string();
                                                    RegisterModel registerModel = GsonImpl.get().toObject(res, RegisterModel.class);
                                                    if (registerModel.getStatus().getCode() == 0) {
                                                        SharedPreferencesFactory.setMid(SignUpActivity.this, getFromEditText(mEmail, ""));
                                                        SharedPreferencesFactory.setPASSWORD(SignUpActivity.this, getFromEditText(mPassword, ""));
                                                        MyApplication.getInstance().showNote("Register Success");
                                                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        MyApplication.getInstance().showNote("Register fail");
                                                    }
                                                    Util.DLog(TAG, "register:" + res);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            } else {
                                dismissLoadingBar();
                                MyApplication.getInstance().showNote("Email is not valid , please try another email");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if (isSpinnerFirst) {
//            view.setVisibility(View.INVISIBLE);
//        }
//        isSpinnerFirst = false;
        Log.d(TAG, "onItemSelected:" + i);
        if (i == 1) {
            mSexText = "F";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private String getValue(EditText editText) {
        if (editText == null) return "";
        if (editText.getText() != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }
}
