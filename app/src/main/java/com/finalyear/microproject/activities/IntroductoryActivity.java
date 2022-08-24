package com.finalyear.microproject.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.finalyear.microproject.R;
import com.finalyear.microproject.onBoarding.OnBoardingFragment1;
import com.finalyear.microproject.onBoarding.OnBoardingFragment2;
import com.finalyear.microproject.onBoarding.OnBoardingFragment3;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView appName,Logo,Splash;
    LottieAnimationView lottieAnimationView;
    String prevStarted;

    private static final  int NUM_PAGES =3;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        Logo=findViewById(R.id.logo);
        appName=findViewById(R.id.app_name);
        Splash=findViewById(R.id.splashscreen);
       // lottieAnimationView=findViewById(R.id.lottie);

        ViewPager viewPager = findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        Splash.animate().translationY(-3600).setDuration(1000).setStartDelay(1000);
        Logo.animate().translationY(2400).setDuration(1000).setStartDelay(1000);
        appName.animate().translationY(2400).setDuration(1000).setStartDelay(1000);
       // lottieAnimationView.animate().translationY(2400).setDuration(1000).setStartDelay(4000);

    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new OnBoardingFragment1();

                case 1:
                    return new OnBoardingFragment2();

                case 2:
                    return new OnBoardingFragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //SharedPreferences sharedPreference = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        //if(!sharedPreference.getBoolean(prevStarted,false)) {
        //    SharedPreferences.Editor editor = sharedPreference.edit();
        //    editor.putBoolean(prevStarted, Boolean.TRUE);
        //    editor.apply();
        //}
        //else{
        //    Intent intent = new Intent(this, LoginActivity.class);
        //    startActivity(intent);
        //}
    }
}

