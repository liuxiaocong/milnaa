package androiddev.milnaa.com.milnaa.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LiuXiaocong on 8/12/2016.
 */
public class SharedPreferencesFactory {
    private static final String KEY_MID = "KEY_MID";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";


    public static void setMid(Context context, String value) {
        set(context, KEY_MID, value);
    }

    public static String getMid(Context context) {
        return get(context, KEY_MID, null);
    }

    public static void setPASSWORD(Context context, String value) {
        set(context, KEY_PASSWORD, value);
    }

    public static String getPASSWORD(Context context) {
        return get(context, KEY_PASSWORD, null);
    }


    private static String get(Context mContext, String _key, String _defaultValue) {
        if (null != mContext) {
            SharedPreferences spf = mContext.getSharedPreferences(_key, Context.MODE_PRIVATE);
            if (null != spf) {
                return spf.getString(_key, _defaultValue);
            }
        }

        return _defaultValue;
    }

    private static void set(Context mContext, String _key, String _value) {
        if (null != mContext) {
            SharedPreferences.Editor dateEditor = mContext.getSharedPreferences(_key, Context.MODE_PRIVATE).edit();
            dateEditor.putString(_key, _value);
            dateEditor.apply();
        }
    }
}
