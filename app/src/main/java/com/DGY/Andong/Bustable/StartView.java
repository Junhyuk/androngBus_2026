package com.DGY.Andong.Bustable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Administrator on 2016-02-17.
 */
public class StartView extends Activity {
    /** Called when the activity is first created. */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startview);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent(StartView.this, Tab_MainActivity.class);
                startActivity(intent);
                // 뒤로가기 했을경우 안나오도록 없애+주기 >> finish!!
                finish();
            }
        }, 1500);

    }




}