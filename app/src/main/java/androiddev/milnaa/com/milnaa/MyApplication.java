package androiddev.milnaa.com.milnaa;

import android.app.Application;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.drawee.backends.pipeline.Fresco;

import androiddev.milnaa.com.milnaa.common.Util;

/**
 * Created by LiuXiaocong on 8/15/2016.
 */

public class MyApplication extends Application {
    private final String TAG = "MyApplication";
    private static MyApplication instance;
    private View mToastView;
    private Toast mToast;
    private boolean mWeatherDataReady = false;
    private boolean mConfigDataReady = false;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco.initialize(this);
        instance = this;

        Util.DLog(TAG, "onCreate");
        LoadData();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public boolean isWeatherDataReady() {
        return mWeatherDataReady;
    }

    public boolean isConfigDataReady() {
        return mConfigDataReady;
    }


    private void LoadData() {

    }

    public void showNote(String str) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = initToast();
        if (mToast != null && mToastView != null) {
            ((TextView) mToastView.findViewById(R.id.toast_text)).setText(str);
            mToast.show();
        }
    }

    private Toast initToast() {
        Toast toast = null;
        try {
            toast = new Toast(instance);
            LayoutInflater layoutInflater = LayoutInflater.from(instance);
            mToastView = layoutInflater.inflate(R.layout.globe_toast_layout, null);

            toast.setView(mToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);

        } catch (Exception e) {

        }
        return toast;
    }

}
