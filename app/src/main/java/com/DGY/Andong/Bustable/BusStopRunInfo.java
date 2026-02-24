package com.DGY.Andong.Bustable;


import static android.content.Intent.ACTION_VIEW;

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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by Joon on 2020-12-11.
 */

public class BusStopRunInfo extends  Activity{


    String Businfo_fromStop = "http://bus.andong.go.kr:8080/api/facilities/station/getBusArriveData?stationId=";

    List<BusStopInfo> busrunningList = new ArrayList<BusStopInfo>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog mPriceprogressDialog = null;
    private FloatingActionButton RefreshBtn;
    private Button GPSbtn;


    private TextView BusNameTextView;
    private String BusNumID;
    private String BusStopName;
    private String GpsX;
    private String GpsY;



    public Context iContext;
    public ArrayList<BusInfo> businfoList = null;

    public List<BusInfo> stopRunningList = new ArrayList<BusInfo>();

    private MyBusStopAdapter mMyAdapter;//  new MyBusStopAdapter();

    private  ListView BusRunningListView;

    @Override
    protected void onCreate(Bundle savedInstantanceState){
        Log.i("JunDebug", "BusStop Runinfo view second");


        super.onCreate(savedInstantanceState);
        setContentView(R.layout.busstop_layout);

        iContext = getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        BusStopName = bundle.getString("BusStopName");
        BusNumID =  bundle.getString("BusStopID");
        businfoList = bundle.getParcelableArrayList("list");

        GpsX =  bundle.getString("GpsX");
        GpsY = bundle.getString("GpsY");


        Log.i("JunDebug", "BusRouteNum request Number --> " + BusStopName);
        Log.i("JunDebug", "Bus Stop ID -->   " + BusNumID);

        BusRunningListView = (ListView) findViewById(R.id.BusRunFromStop);
        mMyAdapter =  new MyBusStopAdapter();
        BusRunningListView.setAdapter(mMyAdapter);


        GPSbtn = (Button) findViewById(R.id.busgps);

        GPSbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("JunDebug", "gps btn click");
                GpsMapGo();

            }

        });







        BusNameTextView = (TextView) findViewById(R.id.BusStopName);
        RefreshBtn = (FloatingActionButton) findViewById(R.id.bus_stop_fab);

        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){


                if(mPriceprogressDialog == null) {
                    mPriceprogressDialog = new ProgressDialog(view.getContext());
                }

                mPriceprogressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mPriceprogressDialog.setMessage("잠시 기다려 주세요.");
                mPriceprogressDialog.show();

                int count = mMyAdapter.getCount();

                if(count >  0){

                    mMyAdapter.mItems.clear();
                    mMyAdapter.notifyDataSetChanged();
                    busrunningList.clear();
                }


                TaskRequestRefresh(0);
                Log.i("JunDebug" , "onClick Item called");
            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Busstop_swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i("BugDetection", "SwipeRefresh button click ");


                mSwipeRefreshLayout.setRefreshing(false);


                int count = mMyAdapter.getCount();

                Log.i("JunDebug", "count Num " + " " + count);

                if(count >  0){

                    mMyAdapter.mItems.clear();

                    Log.i("JunDebug", "mMyAdapter.mItems");

                    mMyAdapter.notifyDataSetChanged();
                    busrunningList.clear();
                }

                TaskRequestRefresh(0);


            }
        });

        TitleUIUpdate();

        TaskRequest(0);
    }

    private void TitleUIUpdate(){

        SpannableStringBuilder ssb = new SpannableStringBuilder(BusStopName);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#DB7093")),  0, BusStopName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new RelativeSizeSpan(0.8f), BusStopName.length(),BusStopName.length(), 0); // set size

        BusNameTextView.setText(ssb);
    }




    public void GpsMapGo() {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.


        String uri ="http://map.naver.com/?lng="+GpsX+"&lat="+ GpsY +"&zoon=20"+"&title=" + BusStopName;
        //https://map.naver.com/?lng=126.9783882&lat=37.5666103&title=서울시청
        Log.i("JunDebug", "gps uri --> " + uri);

        //http://google.com/maps/@35.7357204,137.9658808,11.75z?hl=ko
         Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER );
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//        startActivity(intent);
        startActivity(intent);
        Log.i("JunDebug", "start web intent !!");

    }



    private void TaskRequest(int mode){

        GetXMLTaskStationList task2 =  new GetXMLTaskStationList();
        task2.execute(Businfo_fromStop + BusNumID , mode);

        Log.i("JunDebug", "BusRouteNum NumId--> " + " " + BusNumID);

    }


    private void TaskRequestRefresh(int mode){

        GetXMLTaskStationList task2 =  new GetXMLTaskStationList();
        task2.execute(Businfo_fromStop + BusNumID , mode);

    }


    public class MyBusStopAdapter extends BaseAdapter {

        /* 아이템을 세트로 담기 위한 어레이 */
        private ArrayList<BusStopInfo> mItems = new ArrayList<>();

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public BusStopInfo getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Context context = parent.getContext();


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_busstop, parent, false);
            }

            /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
            //ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);

            TextView BusNum_fromStop = (TextView) convertView.findViewById(R.id.BusNum_fromStop);
            TextView BusLine  = (TextView) convertView.findViewById(R.id.BusLine);
            TextView BusTime_Num  = (TextView) convertView.findViewById(R.id.BusTime_Num);
            TextView Position  = (TextView) convertView.findViewById(R.id.Position);
            ImageView busArrivingIcon = (ImageView) convertView.findViewById(R.id.bus_arriving_icon);
            TextView busArrivingText = (TextView) convertView.findViewById(R.id.bus_arriving_text);
            android.view.ViewGroup itemRoot = (android.view.ViewGroup) convertView.findViewById(R.id.busstop_item_root);

            BusStopInfo StopInfo = getItem(position);

            /* 버스 번호 */
            BusNum_fromStop.setText(StopInfo.getrouteNum());

            /* 노선 시작→끝 정보: <-> 대신 → 화살표 표시 */
            String rawRoute = StopInfo.getStartEndName();
            String displayRoute = "";
            if (rawRoute != null && !rawRoute.isEmpty()) {
                displayRoute = rawRoute.replace("<->", " → ").replace("< - >", " → ");
            } else {
                displayRoute = "노선 정보 없음";
            }
            BusLine.setText(displayRoute);

            /* 도착 시간 */
            String predictTm = StopInfo.getpredictTm() != null ? StopInfo.getpredictTm() : "0";
            String plateNo = StopInfo.getpostPlateNo() != null ? StopInfo.getpostPlateNo() : "-";
            String predictTimeStr = predictTm + "분 후 도착  |  " + plateNo;
            SpannableStringBuilder ssb1 = new SpannableStringBuilder(predictTimeStr);
            ssb1.setSpan(new ForegroundColorSpan(Color.parseColor("#D32F2F")), 0, predictTm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            BusTime_Num.setText(ssb1);

            /* 남은 정류소 */
            String remainStation = StopInfo.getremainStation() != null ? StopInfo.getremainStation() : "0";
            String positionStr = remainStation + " 전 정류소";
            SpannableStringBuilder ssb2 = new SpannableStringBuilder(positionStr);
            ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#1565C0")), 0, remainStation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Position.setText(ssb2);

            /* 도착 임박(1~2분) 이면 버스 아이콘 표시 + 배경색 변경 */
            try {
                int predictMin = Integer.parseInt(predictTm.trim());
                if (predictMin <= 2) {
                    if (busArrivingIcon != null) busArrivingIcon.setVisibility(View.VISIBLE);
                    if (busArrivingText != null) busArrivingText.setVisibility(View.VISIBLE);
                    if (itemRoot != null) itemRoot.setBackgroundResource(R.drawable.bg_station_active);
                } else {
                    if (busArrivingIcon != null) busArrivingIcon.setVisibility(View.INVISIBLE);
                    if (busArrivingText != null) busArrivingText.setVisibility(View.INVISIBLE);
                    if (itemRoot != null) itemRoot.setBackgroundResource(R.drawable.bg_busstop_item);
                }
            } catch (NumberFormatException e) {
                if (busArrivingIcon != null) busArrivingIcon.setVisibility(View.INVISIBLE);
                if (busArrivingText != null) busArrivingText.setVisibility(View.INVISIBLE);
                if (itemRoot != null) itemRoot.setBackgroundResource(R.drawable.bg_busstop_item);
            }

            return convertView;
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
        public void addItem (String RouteID, String routeNum, String StartEndName, String predictTm, String postPlateNo, String remainStation) {

            BusStopInfo mItem = new BusStopInfo();

            /* MyItem에 아이템을 setting한다. */

            mItem.setRouteID(RouteID);
            mItem.setrouteNum(routeNum);
            mItem.setStartEndName(StartEndName);
            mItem.setpredictTm(predictTm);
            mItem.setpostPlateNo(postPlateNo);
            mItem.setremainStation(remainStation);

            /* mItems에 MyItem을 추가한다. */
            mItems.add(mItem);
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
        public void setItem  (String RouteID, String routeNum, String StartEndName, String predictTm, String postPlateNo, String remainStation, int index) {

            /* MyItem에 아이템을 setting한다. */
            BusStopInfo mItem = new BusStopInfo();
            /* MyItem에 아이템을 setting한다. */

            mItem.setRouteID(RouteID);
            mItem.setrouteNum(routeNum);
            mItem.setStartEndName(StartEndName);
            mItem.setpredictTm(predictTm);
            mItem.setpostPlateNo(postPlateNo);
            mItem.setremainStation(remainStation);

            mItems.set(index, mItem);
        }
        public void clear(){

            mItems.clear();
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

            Log.i("JunDebug", "XMLparserLocal Mode --> " +  mode);

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                int result  =  jsonObject.getInt("code");

                if (mode == 1 ){

                    if(result ==200 || result ==204){
                        Log.i("JunDebug" ,"Get 200 success");

                        if(result ==204){

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
                Log.e("JunDebug", "Exception Json Object ");
            }

            try {

                JSONArray jarray = new JSONObject(jsonData).getJSONArray("results");
                Log.i("JunDebug", "jarray lenghth " + jarray.length());

                for (int i = 0; i < jarray.length(); i++) {

                    JSONObject jObject = jarray.getJSONObject(i);
                    String predictTm = jObject.optString("predictTm");

                    if(!predictTm.isEmpty()){

                        String routeNum =  jObject.optString("routeNum");
                        String remainStation = jObject.optString("remainStation");
                        String postPlateNo = jObject.optString("postPlateNo");
                        String via = jObject.optString("via");
                        String routeId =  jObject.optString("routeId");

                        Log.i("JunDebug", "Bus Prediction Time to add  " + predictTm);
                        busrunningList.add(new BusStopInfo(routeId, routeNum, via, predictTm,  postPlateNo, remainStation));
                    }

                }


                Log.i("BugDetection", "Parsing bus running of jarray  " + jarray.length());

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

                    if (line == null)
                        break;

                    sBuffer.append(line);

                }

                reader.close();
                xmlStr = sBuffer.toString();

                Log.i("JunDebug","xmlStr request--> ");

                XMLparserLocal(xmlStr, mode);
                Log.i("JunDebug" ,"request done");


            } catch (Exception e) {

                e.printStackTrace();
                Log.i("JunDebug" ,"Parsing Error");
            }
            return doc;
        }


        @Override
        protected void onPostExecute(Document doc) {
            Log.i("JunDebug" ,"onPostExecute start ");


            if(mPriceprogressDialog != null)
                mPriceprogressDialog.dismiss();

            BusStopinfoUpdate();
            mMyAdapter.notifyDataSetChanged();

            //BusNameTextView.setText("테스트");

        }
    }


    private void BusStopinfoUpdate() {

        Log.i("JunDebug", "BusStopinfoUpdate called");
        Log.i("JunDebug", "busrunningList.size-> " + busrunningList.size());




        for (int i = 0; i < busrunningList.size(); i++) {

            String RouteID = busrunningList.get(i).getRouteID();
            String routeNum = busrunningList.get(i).getrouteNum();
            String StartEndName = busrunningList.get(i).getStartEndName();
            String predictTm = busrunningList.get(i).getpredictTm();
            String postPlateNo = busrunningList.get(i).getpostPlateNo();
            String remainStation = busrunningList.get(i).getremainStation();


            mMyAdapter.addItem(RouteID, routeNum, StartEndName, predictTm, postPlateNo, remainStation);

            Log.i("JunDebug", " mMyAdapter.addItem added : Name  --> " + routeNum);

            Log.i("JunDebug", " mMyAdapter.addItem added : StartEndName  --> " + StartEndName);
            Log.i("JunDebug", " mMyAdapter.addItem added : StartEndName  --> " + StartEndName);
            Log.i("JunDebug", " mMyAdapter.addItem added : predictTm  --> " + predictTm);
            Log.i("JunDebug", " mMyAdapter.addItem added : postPlateNo  --> " + postPlateNo);
            Log.i("JunDebug", " mMyAdapter.addItem added : remainStation  --> " + remainStation);

        }

        /* 리스트뷰에 어댑터 등록 */
        //BusRunningListView.setAdapter(mMyAdapter);

        BusRunningListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object listItem = BusRunningListView.getItemAtPosition(position);
                Log.i("JunDebug", "BusRunning Postion Number  BusRunningListView --> " + position);

                String RouteID = busrunningList.get(position).getRouteID();
                String routeNum =  busrunningList.get(position).getrouteNum();

                Log.i("JunDebug", "Seletected BusStop Running RouteID  -->  " + RouteID);
                Log.i("JunDebug", "Seletected BusStop Running BUsNum -->  " + routeNum);

                if(businfoList != null ) {
                    Log.i("JunDebug", "Bus Info Size () -->  " + businfoList.size());
                }

                for(int idx = 0;  businfoList != null && idx < businfoList.size() ; idx++){

                    Log.i("JunDebug", "in for loop BusStop Running BUsNum -->  " + businfoList.get(idx).getRouteID());

                    if( businfoList.get(idx).getRouteID().equals(RouteID) ){

                        Intent intent = new Intent(iContext, second.class);

                        intent.putExtra("BusRouteNum", businfoList.get(idx).getRouteID());
                        intent.putExtra("BusNum", businfoList.get(idx).getBusNum());
                        intent.putExtra("StartTime", businfoList.get(idx).getStartTime());
                        intent.putExtra("EndTime", businfoList.get(idx).getEndTime());

                        intent.putExtra("StartEndTime", businfoList.get(idx).getStartName() + " < - >" + businfoList.get(idx).getEndName());
                        intent.putParcelableArrayListExtra("list", businfoList);

                        startActivity(intent);
                        break;
                    }
                }



            }
        });

        }

}
