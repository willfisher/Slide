package com.retrochicken.slide;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Constants.SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels - dpToPx(50);
        Constants.SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.Y_PIXELS_TO_INCH = metrics.ydpi;
        Constants.X_PIXELS_TO_INCH = metrics.xdpi;

        AdView adView = new AdView(this);
        adView.setBackgroundColor(Color.TRANSPARENT);
        adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        GamePanel panel = new GamePanel(this);

        RelativeLayout.LayoutParams adViewLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adViewLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adViewLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        adView.setLayoutParams(adViewLayoutParam);

        RelativeLayout.LayoutParams surfaceViewLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        panel.setLayoutParams(surfaceViewLayoutParam);

        RelativeLayout.LayoutParams parentLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        //RelativeLayout adViewLayout = new RelativeLayout(this);
        //adViewLayout.setLayoutParams(adViewLayoutParam);

        RelativeLayout parentLayout = new RelativeLayout(this);
        parentLayout.addView(panel);
        parentLayout.addView(adView);
        parentLayout.setLayoutParams(parentLayoutParam);

        setContentView(parentLayout);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
