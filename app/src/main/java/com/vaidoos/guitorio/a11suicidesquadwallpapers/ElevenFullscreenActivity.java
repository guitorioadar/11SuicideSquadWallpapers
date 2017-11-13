package com.vaidoos.guitorio.a11suicidesquadwallpapers;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ElevenFullscreenActivity extends AppCompatActivity {




    private Button btnEleven,btnElevenShareMust;
    private ImageView btnElevenNext,btnElevenPrevious,btnElevenShare;
    private FrameLayout container;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private static final String APP_ID = "ca-app-pub-5608607730294297~6275222488";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eleven_fullscreen);
        getSupportActionBar().setTitle(R.string.title_actionbar_eleven);

        MobileAds.initialize(this,APP_ID);

        mAdView = (AdView) findViewById(R.id.adViewEleven);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5608607730294297/8709814136");
        mInterstitialAd.loadAd(adRequest);


        SharedPreferences sharedPrefe = getSharedPreferences("checkShared", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefe.edit();
        editor.putString("checkShared",Count.active.toString());
        editor.apply();

        String check = sharedPrefe.getString("checkShared",null);
        //Toast.makeText(ElevenFullscreenActivity.this, check, Toast.LENGTH_SHORT).show();


        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.btnEleven).setOnTouchListener(mDelayHideTouchListener);

        btnEleven = (Button) findViewById(R.id.btnEleven);
        btnElevenNext = (ImageView) findViewById(R.id.btnElevenNext);
        btnElevenPrevious = (ImageView) findViewById(R.id.btnElevenPrevious);
        btnElevenShare = (ImageView) findViewById(R.id.btnElevenShare);
        btnElevenShareMust = (Button) findViewById(R.id.btnElevenShareMust);
        container = (FrameLayout) findViewById(R.id.container);


        if(Count.active.equals("false")){
            container.setVisibility(View.VISIBLE);
            btnElevenShareMust .setVisibility(View.VISIBLE);
            btnEleven.setVisibility(View.GONE);
        }else {
            container.setVisibility(View.GONE);
            btnElevenShareMust .setVisibility(View.GONE);
            btnEleven.setVisibility(View.VISIBLE);
        }


        btnElevenShareMust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("checkShared",Context.MODE_PRIVATE);
                SharedPreferences.Editor editorOnPress = preferences.edit();
                editorOnPress.putString("checkShared","true");
                editorOnPress.apply();

                String check = preferences.getString("checkShared",null);
                Count.active = check;
                //Toast.makeText(ElevenFullscreenActivity.this, check, Toast.LENGTH_SHORT).show();


                try{



                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\n 11 Suicide Squad Wallpapers \n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.vaidoos.guitorio.a11suicidesquadwallpapers \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    btnElevenShareMust .postDelayed(new Runnable() {
                        public void run() {
                            container.setVisibility(View.GONE);
                            btnElevenShareMust .setVisibility(View.GONE);
                            btnEleven.setVisibility(View.VISIBLE);

                        }
                    }, 10*1000);

                } catch(Exception e) {
                    Toast.makeText(ElevenFullscreenActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnElevenShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\n 11 Suicide Squad Wallpapers \n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.vaidoos.guitorio.a11suicidesquadwallpapers \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });


        btnElevenNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElevenFullscreenActivity.this,OneFullscreenActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                finish();
            }
        });

        btnEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {

                    WallpaperManager myWallpaperManager
                            = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        myWallpaperManager.setResource(+R.drawable.eleven);
                        Toast.makeText(ElevenFullscreenActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                //Toast.makeText(OneFullscreenActivity.this, "Ad was closed", Toast.LENGTH_SHORT).show();
            }
        });

        btnElevenPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElevenFullscreenActivity.this,TenFullscreenActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                finish();
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,OneFullscreenActivity.class));
        finish();
    }
}


    /*//----variables----
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private static final String LOGIN_STATUS = "login_status";
    private static final String IS_SESSION_EXIST = "is_session_exists";

//---onCreate()----
sharedPreferences = getSharedPreferences(LOGIN_STATUS, Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean(IS_SESSION_EXIST, false)){
//hide the button
        }
        else {
//show the button
        }


//-----onClick()------
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(IS_SESSION_EXIST, true);
        sharedPreferencesEditor.commit();*/