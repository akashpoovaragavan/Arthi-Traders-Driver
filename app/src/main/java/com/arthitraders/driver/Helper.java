package com.arthitraders.driver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class Helper {
    public static ProgressBarHandler mProgressBarHandlerdriver;
    public static SharedPreferences sharedpreferencesdriver;
    public static final String MyPREFERENCES = "arthitradersdriver" ;

    /**************************API***********************************/
    public static String login_url              = "https://treevibestech.com/ats/api/vehicle_login.php";
    public static String Shippinglist_url       = "https://treevibestech.com/ats/api/shipping_list.php";
    public static String Updateorder_url        = "https://treevibestech.com/ats/api/driver_update.php";
    //public static String imageurl_url           = "http://treevibestech.com/ats/doc/";


    public static void loading(Activity activity) {
        mProgressBarHandlerdriver = new ProgressBarHandler(activity); // In onCreate
        mProgressBarHandlerdriver.show(); // To show the progress bar
    }
    public static class ProgressBarHandler {
        private ProgressBar mProgressBar;
        private Context mContext;

        public ProgressBarHandler(Activity context) {
            mContext = context;
            ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
            mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            mProgressBar.setIndeterminate(true);
            RelativeLayout.LayoutParams params = new
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            RelativeLayout rl = new RelativeLayout(context);
            rl.setGravity(Gravity.CENTER);
            rl.addView(mProgressBar);
            layout.addView(rl, params);
            hide();
        }
        public void show() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        public void hide() {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
