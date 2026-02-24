package com.DGY.Andong.Bustable;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.os.Parcel;
import android.os.Parcelable;



import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.SpannableStringBuilder;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Thread.sleep;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * Created by Joon on 2020-12-11.
 */

public class second extends Activity{

    public Context iContext;

    String BusRouteNum = "";
    String StartEndTime;

    String CurrentBusURL = "http://bus.andong.go.kr:8080/api/route/bus/getDataList?routeId=";
    String stationURL =    "http://bus.andong.go.kr:8080/api/route/station/getDataList?routeId=";

    public List<BusStationInfo> busstaionInfo = new ArrayList<BusStationInfo>();
    List<CurBusStatus> curBusinfo = new ArrayList<CurBusStatus>();
    MyBusStationAdapter mMyAdapter = new MyBusStationAdapter();
    public ArrayList<BusInfo> businfoList = null;


    private String BusNum = "";
    private String StartTime = "";
    private String EndTime = "";
    private String StartDest_Stop = "" ;

    private  ListView BusStationList;
    GetXMLTaskStationList task_reqstations;
    private  Boolean DoneFlagStation = false;

    private TextView BusNuminfo ;
    private TextView BusStartEnd ;
    private TextView BusRun ;
    private ExtendedFloatingActionButton  RefreshBtn;

    private String bus_running = "";
    private View arg0;

    private ProgressDialog mPriceprogressDialog = null;


    private final int MY_PERMISSION_REQUEST_CALL= 100;
    private static final String TAG = "AppPermission";
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.i("JunDebug", "second second");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        Bundle bundle = getIntent().getExtras();
        iContext = getApplicationContext();

        BusRouteNum = bundle.getString("BusRouteNum");
        BusNum = bundle.getString("BusNum");
        StartTime = bundle.getString("StartTime");
        EndTime = bundle.getString("EndTime");
        StartEndTime= bundle.getString("StartEndTime");

        businfoList = bundle.getParcelableArrayList("list");

        BusStationList = (ListView) findViewById(R.id.stationList);
        BusNuminfo = (TextView) findViewById(R.id.LargeText);
        BusStartEnd = (TextView) findViewById(R.id.BusSTartEnd);
        BusRun = (TextView) findViewById(R.id.BusRunning);
        RefreshBtn = (ExtendedFloatingActionButton) findViewById(R.id.fab);

        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                if(mPriceprogressDialog == null) {
                    mPriceprogressDialog = new ProgressDialog(view.getContext());
                }

                mPriceprogressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mPriceprogressDialog.setMessage("잠시 기다려 주세요.");
                mPriceprogressDialog.show();

                TaskRequestRefresh(2);

                Log.i("JunDebug" , "onClick Item called");
            }
        });
        Log.i("JunDebug", "BusRouteNum request Number --> " + BusRouteNum);
        Log.i("JunDebug", "BusRouteNum request Number --> " + BusRouteNum);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i("BugDetection", "SwipeRefresh button click ");

                TaskRequestRefresh(2);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // AdMob 광고 초기화
        loadAdMobBanner();

        TitleUIUpdate();
        TaskRequest(1);





        /*
        GetXMLTaskStationList task2 =  new GetXMLTaskStationList();
        task2.execute(CurrentBusURL + BusRouteNum , 1);

        try
        {
            Thread.sleep(20);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        task_reqstations = new GetXMLTaskStationList();
        task_reqstations.execute(stationURL + BusRouteNum, 0);

        */
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        Log.i(TAG, "CheckPermission : " + checkSelfPermission(Manifest.permission.CALL_PHONE));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Phone call", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE},
                    MY_PERMISSION_REQUEST_CALL);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            Log.e(TAG, "permission deny");

        }
    }
    private void TaskRequest(int mode){

        GetXMLTaskStationList task2 =  new GetXMLTaskStationList();
        task2.execute(CurrentBusURL + BusRouteNum , mode);

        try
        {
            Thread.sleep(20);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        task_reqstations = new GetXMLTaskStationList();
        task_reqstations.execute(stationURL + BusRouteNum, 0);

        Log.i("JunDebug", "BusRouteNum NumId--> " + " " + BusRouteNum);
    }


    private void TaskRequestRefresh(int mode){

        GetXMLTaskStationList task2 =  new GetXMLTaskStationList();
        task2.execute(CurrentBusURL + BusRouteNum , mode);

        try
        {
            Thread.sleep(20);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //task_reqstations = new GetXMLTaskStationList();
        //task_reqstations.execute(stationURL + BusRouteNum, 0);

        Log.i("JunDebug", "BusRouteNum NumId--> " + " " + BusRouteNum);
    }

    private void TitleUIUpdate(){

        String BusNumStr1 = BusNum + " 번" ;

        // 노선 정보: "봉정사 → 교보생명" 형식으로 변경
        String routeInfo = StartEndTime.replace("<->", " → ").replace("< - >", " → ");
        String BusStartEndTime = " " + StartTime + " ~ " + EndTime;

        SpannableStringBuilder ssb = new SpannableStringBuilder(BusNumStr1);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#DB7093")),  0, BusNumStr1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 노선 정보 (시작 → 종료)
        SpannableStringBuilder ssb_StartDest = new SpannableStringBuilder(routeInfo);
        ssb_StartDest.setSpan(new ForegroundColorSpan(Color.parseColor("#1565C0")),  0, ssb_StartDest.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 운행 시간 (시작 ~ 종료)
        SpannableStringBuilder ssb2 = new SpannableStringBuilder(BusStartEndTime);
        ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#D32F2F")),  1, StartTime.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")),  BusStartEndTime.length() -  EndTime.length(),  BusStartEndTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        BusNuminfo.setText(ssb);
        BusStartEnd.setText(ssb_StartDest);
    }

    private void loadAdMobBanner() {
        com.google.android.gms.ads.AdView mAdView = findViewById(R.id.adView_second);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    public class MyBusStationAdapter extends BaseAdapter {

        /* 아이템을 세트로 담기 위한 어레이 */
        private ArrayList<BusRoute> mItems = new ArrayList<>();

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public BusRoute getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Context context = parent.getContext();
            /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_listview2, parent, false);
            }

            /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
            TextView BusStationName = (TextView) convertView.findViewById(R.id.customBusStationTitle);
            TextView StationNum  = (TextView) convertView.findViewById(R.id.stationNum);
            View stopPoint = convertView.findViewById(R.id.stop_point);
            android.widget.LinearLayout busHereLayout = (android.widget.LinearLayout) convertView.findViewById(R.id.bus_here_layout);
            android.view.ViewGroup stationRoot = (android.view.ViewGroup) convertView.findViewById(R.id.station_item_root);

            BusRoute myStationItem = getItem(position);

            boolean isBusHere = (myStationItem.getCurBusPos() != 0 && !myStationItem.getPlateName().equals(""));

            if(isBusHere) {
                // 버스 도착 정류장: 배경 활성화, 왼쪽 포인트 빨간 원, 오른쪽 버스아이콘 표시
                if (stationRoot != null) stationRoot.setBackgroundResource(R.drawable.bg_station_active);
                if (stopPoint != null) stopPoint.setBackgroundResource(R.drawable.shape_stop_point_active);
                if (busHereLayout != null) busHereLayout.setVisibility(View.VISIBLE);

                StationNum.setTypeface(null, Typeface.BOLD);
                StationNum.setTextColor(Color.parseColor("#D32F2F"));
                StationNum.setText("🚌 " + myStationItem.getPlateName());

                BusStationName.setTextColor(Color.parseColor("#BF360C"));
                Log.i("JunDebug" ,"PlateName check-->  " + myStationItem.getPlateName());

            } else {
                // 일반 정류장
                if (stationRoot != null) stationRoot.setBackgroundResource(android.R.color.transparent);
                if (stopPoint != null) stopPoint.setBackgroundResource(R.drawable.shape_stop_point);
                if (busHereLayout != null) busHereLayout.setVisibility(View.INVISIBLE);

                StationNum.setText(myStationItem.getStationNum());
                StationNum.setTypeface(null, Typeface.NORMAL);
                StationNum.setTextColor(Color.parseColor("#BDBDBD"));
                BusStationName.setTextColor(Color.parseColor("#212121"));
            }

            /* 각 위젯에 세팅된 아이템을 뿌려준다 */
            BusStationName.setText(myStationItem.getStationName());


            return convertView;
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
        public void addItem(String StationName, String StationNum, String CurStationName, String BusPlateName, Integer curBusposition) {

            BusRoute mItem = new BusRoute();

            /* MyItem에 아이템을 setting한다. */
            mItem.setStationName(StationName);
            mItem.setStationNum(StationNum);

            mItem.setCurStationName(CurStationName);
            mItem.setPlateName(BusPlateName);
            mItem.setCurBusPos(curBusposition);

            /* mItems에 MyItem을 추가한다. */
            mItems.add(mItem);
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
        public void setItem(String StationName, String StationNum, String CurStationName, String BusPlateName, Integer curBusposition, int index) {

            BusRoute mItem = new BusRoute();

            /* MyItem에 아이템을 setting한다. */
            mItem.setStationName(StationName);
            mItem.setStationNum(StationNum);

            mItem.setCurStationName(CurStationName);
            mItem.setPlateName(BusPlateName);
            mItem.setCurBusPos(curBusposition);

            /* mItems에 MyItem을 추가한다. */
            mItems.set(index, mItem);
        }
    }

    private class GetXMLTaskStationList extends ThreadTask <String, Integer, Document> {

        Document doc = null;
        String xmlStr;

        private  Document convertStringToXMLDocument(String xmlString)
        {
            //Parser that produces DOM object trees from XML content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //API to obtain DOM Document instance
            DocumentBuilder builder = null;

            try
            {
                //Create DocumentBuilder with default configuration
                builder = factory.newDocumentBuilder();

                //Parse the content to Document object
                Document ret_Doc = builder.parse(new InputSource(new StringReader(xmlString)));
                return ret_Doc;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        private void XMLparserLocal (String jsonData, Integer mode){

            Log.i("JunDebug" ,"XMLparserLocal start");
            Log.i("BugDetection", "XMLparserLocal Mode --> " +  mode);

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                int result  =  jsonObject.getInt("code");

                if (mode == 1 ){

                    if(result ==200 || result ==204){
                        Log.i("JunDebug" ,"Get 200 success");

                        if(result ==204){
                            bus_running = "0";
                        }




                    }
                    else{
                        return;
                    }

                }else{

                    if(result == 200) {
                        Log.i("JunDebug" ,"Get 200 success");
                    }
                    else{
                        return;
                    }
                }


            } catch (JSONException e) {
                //some exception handler code.

                e.printStackTrace();
                Log.i("JunDebug", "Exception Json Object ");
            }

            try {

                JSONArray jarray = new JSONObject(jsonData).getJSONArray("results");
                Log.i("JunDebug", "Parsing  num of jarray  " + jarray.length());
                Log.i("BugDetection", "Parsing  num of jarray  " + jarray.length());

                if (mode == 0) {

                    int posidx = 0;
                    Log.i("JunDebug", "운행중 bus list Before --> " + curBusinfo.size());

                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject jObject = jarray.getJSONObject(i);
                        String stationID = jObject.optString("stationId");
                        String stationNm = jObject.optString("stationNm");

                        String gpsX = jObject.optString("gpsX");
                        String gpsY = jObject.optString("gpsY");



                        String CurStationName ="";
                        String BusPlateName ="";
                        Integer curPos = 0;

                        Iterator  iterator = curBusinfo.iterator();
                        Log.i("JunDebug", "Bus 정류장 " + stationID);

                        while(iterator.hasNext()){

                            CurBusStatus temp  = (CurBusStatus)iterator.next();
                            String tmpstationID = temp.getCurStationID();

                            Log.i("JunDebug", "Bus 운행중 " + tmpstationID);

                            if(stationID.equals(tmpstationID)){

                                CurStationName = temp.getStationName();
                                BusPlateName = temp.getPlateName();
                                curPos = temp.getCurBusPos();

                                iterator.remove();
                                break;
                            }
                        }

                        //busstaionInfo.add(new BusStationInfo(BusRouteNum, stationNm, stationID, CurStationName,  BusPlateName, curPos));
                        busstaionInfo.add(new BusStationInfo(BusRouteNum, stationNm, stationID, CurStationName,  BusPlateName, curPos, gpsX, gpsY));
                    }

                    Log.i("JunDebug", "운행중 bus list num --> " + curBusinfo.size());
                    Log.i("JunDebug", "운행중 bus list num --> " + posidx);

                    DoneFlagStation = true;

                } else if (mode == 1) {

                    Log.i("JunDebug", "Mode 1 enter -> " + mode);
                    if(jarray != null)
                        Log.i("JunDebug", "Length --> " + jarray.length());

                    //if(jarray.length())
                    //bus_running =  String.valueOf(jarray.length());

                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject jObject = jarray.getJSONObject(i);
                        String stationOrd = jObject.optString("stationOrd");

                        Integer posIdx = Integer.parseInt(stationOrd);
                        String stationNm = jObject.optString("stationNm");
                        String plateNo =  jObject.optString("plateNo");
                        String CurStationID =  jObject.optString("stationId");

                        curBusinfo.add(new CurBusStatus (stationNm, plateNo, posIdx, CurStationID));
                    }

                    bus_running = String.valueOf (curBusinfo.size());
                }else if (mode == 2) {

                    Log.i("JunDebug", "Mode 2 enter -> " + mode);
                    if(jarray != null)
                        Log.i("JunDebug", "Mode 2 Length --> " + jarray.length());

                    DoneFlagStation = true;

                    //if(jarray.length())
                    //bus_running =  String.valueOf(jarray.length());
                    curBusinfo.clear();
                    //busstaionInfo.clear();

                    for (int i = 0; i < jarray.length(); i++) {

                        Log.i("BugDetection", "cur bus numbering index -> " + i);

                        JSONObject jObject = jarray.getJSONObject(i);
                        String stationOrd = jObject.optString("stationOrd");

                        Integer posIdx = Integer.parseInt(stationOrd);
                        String stationNm = jObject.optString("stationNm");
                        String plateNo =  jObject.optString("plateNo");
                        String CurStationID =  jObject.optString("stationId");

                        Log.i("JunDebug", "stationNm --> " + stationNm);
                        curBusinfo.add(new CurBusStatus (stationNm, plateNo, posIdx, CurStationID));
                    }

                    bus_running = String.valueOf (curBusinfo.size());

                }
            } catch (JSONException e) {
                //some exception handler code.
                e.printStackTrace();
                Log.i("JunDebug", "Exception Json jarray ");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Document doInBackground(String urls, Integer mode) {
            Log.i("JunDebug" ,"doInBackground start ");
            String urlstr = urls;
            Integer Mode = mode;
            URL convertURL;
            Document retdoc = null;

            StringBuilder sBuffer = new StringBuilder();

            try {

                convertURL = new URL(urlstr);
                InputStream inputStream = convertURL.openStream();

                Log.i("JunDebug" ,"openStream Done");
                InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");

                Log.i("JunDebug" ,"InputStreamReader");
                BufferedReader reader = new BufferedReader(streamReader);

                while (true) {
                    String line = reader.readLine();
                    //Log.i("JunDebug", line);
                    if (line == null)
                        break;

                    sBuffer.append(line);
                    //XMLStr += line;
                }

                reader.close();
                xmlStr = sBuffer.toString();

                Log.i("JunDebug","xmlStr request--> ");

                XMLparserLocal(xmlStr, mode);
                Log.i("JunDebug" ,"request done");


            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.i("JunDebug" ,"Parsing Error");
            }
            return doc;
        }


        @Override
        protected void onPostExecute(Document doc) {
            Log.i("JunDebug" ,"onPostExecute start ");
            //super.onPostExecute(doc);
            //textview.setText(s);super.onPostExecute(doc);

            if(DoneFlagStation) {
                BusStationUpdate();
                DoneFlagStation = false;
            }

            if(mPriceprogressDialog != null)
                mPriceprogressDialog.dismiss();

        }
    }

    public class CurBusStatus {

        private String StationName;
        private String BusplateNo;
        private Integer CurBusPos;
        private String CurStationID;


        public CurBusStatus(String StationName, String BusplateNo, Integer CurBusPos, String CurStationID) {

            this.StationName = StationName;
            this.BusplateNo = BusplateNo;
            this.CurBusPos = CurBusPos;
            this.CurStationID = CurStationID;
        }


        public String  getStationName() {
            return StationName;
        }

        public void setStationName(String StationName) {
            this.StationName = StationName;
        }


        public String  getPlateName() {
            return BusplateNo;
        }

        public void setPlateName(String BusplateNo) {
            this.BusplateNo = BusplateNo;
        }

        public Integer  getCurBusPos() {
            return CurBusPos;
        }

        public void setCurBusPos(Integer CurBusPos) {
            this.CurBusPos = CurBusPos;
        }


        public String  getCurStationID() {
            return CurStationID;
        }

        public void setCurStationID(String CurStationID) {
            this.CurStationID = CurStationID;
        }

    }



    class BusStationInfo {

        String routeID;
        String StationName;
        String StationNum;
        String CurStation;
        String BusPlateName;
        Integer StationPos;

        String GpsX;
        String GpsY;

        public   BusStationInfo(String routeID, String StationName, String StationNum, String CurStation, String BusPlateName, Integer StationPos) {

            this.routeID = routeID;
            this.StationName = StationName;
            this.StationNum = StationNum;

            this.CurStation = CurStation;
            this.BusPlateName = BusPlateName;
            this.StationPos = StationPos;

        }



        public   BusStationInfo(String routeID, String StationName, String StationNum, String CurStation, String BusPlateName, Integer StationPos, String gpsX, String gpsY) {

            this.routeID = routeID;
            this.StationName = StationName;
            this.StationNum = StationNum;

            this.CurStation = CurStation;
            this.BusPlateName = BusPlateName;
            this.StationPos = StationPos;

            this.GpsX = gpsX;
            this.GpsY = gpsY;
        }


        public String getRouteID () {
            return this.routeID;
        }

        public String getStationName () {
            return this.StationName;
        }
        public String getStationNum () {
            return this.StationNum;
        }

        public String getCurStation () {
            return this.CurStation;
        }

        public String getBusPlateName () {
            return this.BusPlateName;
        }

        public Integer getStationPos () {
            return this.StationPos;
        }

        public String getGpsX () {
            return this.GpsX;
        }
        public String getGpsY () {
            return this.GpsY;
        }



    }

    private void BusStationUpdate(){

        Log.i("JunDebug", "BusStationUpdate called ");

        int idx = 0;
        String BusRunning  = "현재 " + bus_running + "대 운행중";

        Log.i("JunDebug", "운행 버스->  " + BusRunning);

        SpannableStringBuilder ssb2 = new SpannableStringBuilder(BusRunning);
        ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")),  ("현재 ".length()),  bus_running.length() +  ("현재 ".length() ), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        BusRun.setText(ssb2);

        Log.i("BugDetection", "busstaionInfo.size()-- >  " + busstaionInfo.size());

        if(mMyAdapter.getCount() > 0 ){

            Log.i("BugDetection", "mMyAdapter.getCount() ==> None zero");
            Log.i("BugDetection", "mMyAdapter.getCount()  --> " + mMyAdapter.getCount());


            for (int i = 0; i < busstaionInfo.size(); i++) {

                String StationName = busstaionInfo.get(i).getStationName();
                String StationNum  = busstaionInfo.get(i).getStationNum();

                String CurStationName = busstaionInfo.get(i).getCurStation();
                String BusPlateName  = busstaionInfo.get(i).getBusPlateName();
                Integer curStationPos  = busstaionInfo.get(i).getStationPos();

                String LocalCurStationName ="";
                String LocalBusPlateName ="";
                Integer curPos = 0;

                Iterator  iterator = curBusinfo.iterator();

                Log.i("BugDetection", "curBusinfo size -->   " + curBusinfo.size());
                //Log.i("BugDetection", "Bus 정류장  " + StationNum);

                while(iterator.hasNext()){

                    CurBusStatus temp  = (CurBusStatus)iterator.next();
                    String tmpstationID = temp.getCurStationID();
                    String CurBusStationName  = temp.getStationName();
                    //Log.i("BugDetection", "Bus 운행중 " + tmpstationID);

                    if(StationName.equals(CurBusStationName)){

                        LocalCurStationName = temp.getStationName();
                        LocalBusPlateName = temp.getPlateName();
                        curPos = temp.getCurBusPos();

                        iterator.remove();

                        //Log.i("BugDetection", "Find ! Done");
                        //Log.i("BugDetection", "BusPlatName-> " + LocalBusPlateName);
                        break;
                    }
                }


                //mMyAdapter.addItem(StationName, StationNum, LocalCurStationName, LocalBusPlateName, curPos);
                mMyAdapter.setItem(StationName, StationNum, LocalCurStationName, LocalBusPlateName, curPos, i);

                //original code //
                //mMyAdapter.setItem(StationName, StationNum, CurStationName, BusPlateName, curStationPos, i);

                //busstaionInfo.add(new BusStationInfo(BusRouteNum, stationNm, stationID, CurStationName,  BusPlateName, curPos));
                //Log.i("JunDebug", "Bus Station ID Number  --> " + StationNum);
            }
        }else{

            Log.i("BugDetection", "mMyAdapter.getCount() ==> zero");

            Log.i("BugDetection", "mMyAdapter.getCount()  --> " + mMyAdapter.getCount());
            Log.i("BugDetection", "busstaionInfo.size()   --> " + busstaionInfo.size());


            for (int i = 0; i < busstaionInfo.size(); i++) {

                String StationName = busstaionInfo.get(i).getStationName();
                String StationNum  = busstaionInfo.get(i).getStationNum();


                String CurStationName = busstaionInfo.get(i).getCurStation();
                String BusPlateName  = busstaionInfo.get(i).getBusPlateName();
                Integer curStationPos  = busstaionInfo.get(i).getStationPos();

                mMyAdapter.addItem(StationName, StationNum, CurStationName, BusPlateName, curStationPos);
                Log.i("JunDebug", "Bus Station ID Number  --> " + StationNum);
            }

        }


        /* 리스트뷰에 어댑터 등록 */
        BusStationList.setAdapter(mMyAdapter);
        BusStationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object listItem = BusStationList.getItemAtPosition(position);
                Log.i("JunDebug", "Postion Number --> " + position);

                String StationName = busstaionInfo.get(position).getStationName();
                String StationNum =  busstaionInfo.get(position).getStationNum();
                String GpsX =  busstaionInfo.get(position).getGpsX();
                String GpsY =  busstaionInfo.get(position).getGpsY();


                Intent intent = new Intent(iContext, BusStopRunInfo.class);

                intent.putExtra("BusStopName", StationName);
                intent.putExtra("BusStopID", StationNum);

                intent.putExtra("GpsX", GpsX);
                intent.putExtra("GpsY", GpsY);

                intent.putParcelableArrayListExtra("list", businfoList);

                Log.i("JunDebug", "Seletected Bus statiton info -->  " + StationName);
                Log.i("JunDebug", "Seletected curStationID statiton info -->  " + StationNum);

                startActivity(intent);

            }
        });


        mMyAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.



                } else {

                    Log.d(TAG, "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }
}
