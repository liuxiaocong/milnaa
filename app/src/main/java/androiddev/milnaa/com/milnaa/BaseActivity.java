package androiddev.milnaa.com.milnaa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;

import androiddev.milnaa.com.milnaa.common.Util;

/**
 * Created by LiuXiaocong on 9/19/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadingBar;

    public void showLoadingBar(String message) {
        if (Util.isNullOrEmpty(message)) {
            message = "";
        }

        if (null == mLoadingBar) {
            mLoadingBar = new ProgressDialog(this);
        }
        mLoadingBar.getWindow().setGravity(Gravity.CENTER);
        mLoadingBar.setProgressStyle(android.R.attr.progressBarStyleSmall);
        mLoadingBar.setMessage(message);
        mLoadingBar.setIndeterminate(false);
        mLoadingBar.setCancelable(false);

        mLoadingBar.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissLoadingBar();
                    return true;
                }
                return keyCode == KeyEvent.KEYCODE_MENU;
            }
        });

        mLoadingBar.show();
    }

    public void dismissLoadingBar() {
        if (mLoadingBar != null && mLoadingBar.isShowing()) {
            mLoadingBar.dismiss();
            mLoadingBar = null;
        }
    }
}
