package com.DGY.Andong.Bustable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import com.DGY.Andong.Bustable.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import static android.widget.AdapterView.*;


public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    //변수 선언


    public String[] restaurantArray = {"고궁", "하이델크룩", "바첸하우스", "도모", "서울식당", "송학"};
    public String[] restaurantPhone = {"+49 61717 79451", "+49 6171 971 600", "+49 6196 23430", "+49 6173 78898", "+49 61717 508 2448 ", "+49 6192 200 0122"};
    public String[] restaurantAddress = {"Bahnstrasse 28, 61449 Steinbach (Taunus)", "Konigsteiner Strasse 30, 61440 Oberursel (Taunus)",
                                             "Konigsteiner Str. 157,65812 Bad Soden am Taunus", "Westerbachstrasse 23,61476 Kronberg im Taunus",
                                            "Hohemarkstrasse 194, 61440 Oberursel (Taunus)", "Frankfurt str17, 65830 Kriftel" };
    String[] permission_list = {
            //android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET
    };

    private static final int MULTIPLE_PERMISSION = 10235;


    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        ArrayAdapter<String> Adapter;

        //Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantArray);
        //ListView list = (ListView)findViewById(R.id.listView);

        //list.setAdapter(Adapter);
        //list.setOnItemClickListener(this);

        Log.i("JunDebug", "onCreate View Start");
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

        Log.i("MWC", "onItemClick function" + " "+ restaurantArray [i]);

        String c_list = restaurantArray [i];
        String addr_list = restaurantAddress [i];
        String phone_num = restaurantPhone [i];


        Intent intent = new Intent(MainActivity.this, second.class);

        intent.putExtra("arr_text", c_list);
        intent.putExtra("addr_text", addr_list);
        intent.putExtra("phone_num", phone_num);

        Log.i("MWC", "onItemClick function" + " " + addr_list);
        Log.i("MWC", "onItemClick function" + " " + phone_num);


        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                Log.e("JunDebug"  ,"request --> " + permission);
                requestPermissions(permission_list, 0);
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //권한 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*..권한이 있는경우 실행할 코드....*/
                    //makeDir();
                    Log.i("JubDebug", "Permission already done!! ");

                } else {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");
                    // 권한설정 클릭시 이벤트 발생
                    alertDialog.setPositiveButton("권한설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    //취소
                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                return;
        }
    }



}
