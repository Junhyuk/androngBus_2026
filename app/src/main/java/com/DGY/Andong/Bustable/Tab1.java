package com.DGY.Andong.Bustable;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.fragment.app.FragmentActivity;
//import androidx.core.widget.SwipeRefreshLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import android.support.v7.app.AppCompatActivity;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.widget.CompoundButton.OnCheckedChangeListener;

import static android.content.Context.MODE_PRIVATE;

import android.os.Parcel;
import android.os.Parcelable;

import com.DGY.Andong.Bustable.StringValue;

@SuppressLint("ValidFragment")
public class Tab1 extends Fragment implements Parcelable {

	public Context mContext;
	public static Context iContext = null;

	ListView BusinfoTable;

	private Boolean KeyExist = false;
	Boolean first_reqflag = true;
	private List routeNum = new ArrayList();
	private List stStationNm = new ArrayList();
	private List edStationNm = new ArrayList();
	private List stTm = new ArrayList();
	private List edTm = new ArrayList();

	private StringValue FavoritStr;
	private FragmentListener fragmentListener;


//
//	private static final String SHARED_STOREVER = "10";
//
//	private static final String FAVORITE_BUS_SETTING = "favorite_bus_setting";
//	private static final String FAVORITE_BUS_KEY = "favorite_bus_line_key" + SHARED_STOREVER;
//	private static final String FAVORITE_BUS_VALUE = "favorite_bus_line_value" + SHARED_STOREVER;
//
//	private static final String FAVORITE_BUS_CHECK = "favorite_bus_check" + SHARED_STOREVER;
//
//	private static final String FAVORITE_BUS_START_TIME = "favorite_bus_starttime" + SHARED_STOREVER;
//	private static final String FAVORITE_BUS_END_TIME = "favorite_bus_endtime" + SHARED_STOREVER;
//
//	private static final String FAVORITE_BUS_START_STATION = "favorite_bus_startstation" + SHARED_STOREVER;
//	private static final String FAVORITE_BUS_END_STATION = "favorite_bus_endstation" + SHARED_STOREVER;

	private static final String FAVORITE_TRUE = "true";
	private static final String FAVORITE_FALSE = "false";


	public ArrayList<BusInfo> businfoList = new ArrayList<BusInfo>();

	public ArrayList<BusInfo> businfoAllList = new ArrayList<BusInfo>();

	//List<BusInfo> businfoList = new ArrayList<>();

	private ArrayList<String> FavoriteBusChk = new ArrayList<String>();

	//key number
	private ArrayList<String> FavoriteBusKey = new ArrayList<String>();

	//routeid
	private ArrayList<String> FavoriteBusValue = new ArrayList<String>();

	private ArrayList<String> AllBusRouteID = new ArrayList<String>();


	//private ArrayList<String> FavoriteBuscheck = new ArrayList<String>();
	private ArrayList<String> FavoriteBuscheckByRouteID = new ArrayList<String>();

	ArrayList<String> FavoriteKey = new ArrayList<String>();

	private ArrayList<String> FavoriteBusStartTime = new ArrayList<String>();
	private ArrayList<String> FavoriteBusEndTime = new ArrayList<String>();

	private ArrayList<String> FavoriteBus_sStation = new ArrayList<String>();
	private ArrayList<String> FavoriteBus_eStation = new ArrayList<String>();

	MyAdapter mMyAdapter;


	public Tab1(Context context) {
		mContext = context;
	}

	public Tab1() {
	}


	public final static boolean DEBUG_JUN = true;


	protected Tab1(Parcel in) {
		byte tmpKeyExist = in.readByte();
		KeyExist = tmpKeyExist == 0 ? null : tmpKeyExist == 1;

		FavoriteBusKey = in.createStringArrayList();
		FavoriteBusValue = in.createStringArrayList();
		//FavoriteBuscheck = in.createStringArrayList();
		FavoriteBuscheckByRouteID = in.createStringArrayList();
		FavoriteBusStartTime = in.createStringArrayList();
		FavoriteBusEndTime = in.createStringArrayList();
		FavoriteBus_sStation = in.createStringArrayList();
		FavoriteBus_eStation = in.createStringArrayList();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (KeyExist == null ? 0 : KeyExist ? 1 : 2));
		dest.writeStringList(FavoriteBusKey);
		dest.writeStringList(FavoriteBusValue);
		//dest.writeStringList(FavoriteBuscheck);
		dest.writeStringList(FavoriteBuscheckByRouteID);
		dest.writeStringList(FavoriteBusStartTime);
		dest.writeStringList(FavoriteBusEndTime);
		dest.writeStringList(FavoriteBus_sStation);
		dest.writeStringList(FavoriteBus_eStation);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Tab1> CREATOR = new Creator<Tab1>() {
		@Override
		public Tab1 createFromParcel(Parcel in) {
			return new Tab1(in);
		}

		@Override
		public Tab1[] newArray(int size) {
			return new Tab1[size];
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		Log.i("BugDetection", "Tab 1 View onCreate");

		View view = inflater.inflate(R.layout.activity_tab1, null);
		iContext = inflater.getContext();

		//FavoriteBusChk = getStringArrayPref(FavoritStr.FAVORITE_BUS_KEY);

		if (mMyAdapter != null && !mMyAdapter.isEmpty()) {

			mMyAdapter.mItems.clear();
			mMyAdapter.notifyDataSetChanged();
			businfoList.clear();
		}


//		if (FavoriteBusChk.isEmpty()) {
//			KeyExist = false;
//			Log.i("JunDebug", "Key None Exist !!");
//
//		} else
		{

			KeyExist = true;


			FavoriteBusKey = getStringArrayPref(FavoritStr.FAVORITE_BUS_KEY);
			FavoriteBusValue = getStringArrayPref(FavoritStr.FAVORITE_BUS_VALUE);
			//FavoriteBuscheck = getStringArrayPref(FavoritStr.FAVORITE_BUS_CHECK);

			//jhLee 2023 0226
			FavoriteBuscheckByRouteID = getStringArrayPref(FavoritStr.FAVORITE_BUS_BY_ROUTID);

			FavoriteBusStartTime = getStringArrayPref(FavoritStr.FAVORITE_BUS_START_TIME);
			FavoriteBusEndTime = getStringArrayPref(FavoritStr.FAVORITE_BUS_END_TIME);

			FavoriteBus_sStation = getStringArrayPref(FavoritStr.FAVORITE_BUS_START_STATION);
			FavoriteBus_eStation = getStringArrayPref(FavoritStr.FAVORITE_BUS_END_STATION);
			Log.i("JunDebug", "Exist !! " + FavoriteBusValue.size());

		}




		//ArrayAdapter<String> Adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, RestaurantArray);

		BusinfoTable = (ListView) view.findViewById(R.id.listView2);
		mMyAdapter = new MyAdapter();

		/* 리스트뷰에 어댑터 등록 */
		BusinfoTable.setAdapter(mMyAdapter);

		// AdMob 광고 초기화
		loadAdMobBanner();

		BusinfoTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object listItem = BusinfoTable.getItemAtPosition(position);


				Intent intent = new Intent(iContext, second.class);

				intent.putExtra("BusRouteNum", businfoList.get(position).getRouteID());
				intent.putExtra("BusNum", businfoList.get(position).getBusNum());
				intent.putExtra("StartTime", businfoList.get(position).getStartTime());
				intent.putExtra("EndTime", businfoList.get(position).getEndTime());

				intent.putExtra("StartEndTime", businfoList.get(position).getStartName() + " < - >" + businfoList.get(position).getEndName());
				//intent.putParcelableArrayListExtra("list", businfoList);
				intent.putParcelableArrayListExtra("list", businfoList);


				Log.i("JunDebug", "onItemClick function" + " " + businfoList.get(position).getRouteID());

				startActivity(intent);
				Log.i("JunDebug", "Postion number !!  --> " + position);
			}
		});


		GetXMLTask task = new GetXMLTask();

		task.execute("http://bus.andong.go.kr:8080/api/route/getDataList?type=All", 0);

		return view;

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i("JunDebug", " STop onStop call Tab1");
		UpdateFavorite();
	}


	public void setFragmentListener(FragmentListener listener) {
		this.fragmentListener = listener;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity() instanceof FragmentListener) {
			this.fragmentListener = (FragmentListener) getActivity();
		}
	}

	private void UpdateFavorite() {


		setStringArrayPref(FavoritStr.FAVORITE_BUS_KEY, FavoriteBusKey); // bus number
		setStringArrayPref(FavoritStr.FAVORITE_BUS_VALUE, FavoriteBusValue); // rount id
		//setStringArrayPref(FavoritStr.FAVORITE_BUS_CHECK, FavoriteBuscheck);
		setStringArrayPref(FavoritStr.FAVORITE_BUS_BY_ROUTID, FavoriteBuscheckByRouteID);
		setStringArrayPref(FavoritStr.FAVORITE_BUS_START_TIME, FavoriteBusStartTime);
		setStringArrayPref(FavoritStr.FAVORITE_BUS_END_TIME, FavoriteBusEndTime);
		setStringArrayPref(FavoritStr.FAVORITE_BUS_START_STATION, FavoriteBus_sStation);
		setStringArrayPref(FavoritStr.FAVORITE_BUS_END_STATION, FavoriteBus_eStation);

	}


	private void loadAdMobBanner() {
		if (getView() == null) return;
		com.google.android.gms.ads.AdView mAdView = getView().findViewById(R.id.adView_tab1);
		if (mAdView != null) {
			com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}
	}

	public class MyAdapter extends BaseAdapter {

		/* 아이템을 세트로 담기 위한 어레이 */
		private ArrayList<MyItem> mItems = new ArrayList<>();

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public MyItem getItem(int position) {
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
				convertView = inflater.inflate(R.layout.listview_custom2, parent, false);
			}

			/* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
			TextView busNum = (TextView) convertView.findViewById(R.id.BusNum);
			TextView st_edStName = (TextView) convertView.findViewById(R.id.Busstation);
			TextView st_edTime = (TextView) convertView.findViewById(R.id.BusTime);
			android.widget.LinearLayout badgeLayout = (android.widget.LinearLayout) convertView.findViewById(R.id.bus_badge_layout);

			CheckBox star = (CheckBox) convertView.findViewById(R.id.btn_star);
			star.setTag((int) position);
			star.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);

			MyItem myItem = getItem(position);

			/* 각 위젯에 세팅된 아이템을 뿌려준다 */
			busNum.setText(myItem.getBusNum());
			st_edStName.setText(myItem.getBusStation());
			st_edTime.setText(myItem.getBusTime());

			/* 노선 유형별 배지 색상 및 아이템 배경 구분
			 * 1) "순환" 포함    → 초록색 배지
			 * 2) "급행" 포함    → 빨간색 배지
			 * 3) 숫자 외 한글(지역명 등) 포함 (예: 풍천3) → 주황색 배지
			 * 4) 숫자만         → 파란색 배지 (기본)
			 */
			String busNumStr = myItem.getBusNum() != null ? myItem.getBusNum() : "";
			if (busNumStr.contains("순환")) {
				// 순환 노선 - 초록
				if (badgeLayout != null) badgeLayout.setBackgroundResource(R.drawable.bus_badge_circular);
				convertView.setBackgroundResource(R.drawable.bg_item_circular);
				busNum.setTextColor(android.graphics.Color.WHITE);
				st_edStName.setTextColor(android.graphics.Color.parseColor("#2E7D32"));
				st_edTime.setTextColor(android.graphics.Color.parseColor("#1B5E20"));
			} else if (busNumStr.contains("급행")) {
				// 급행 노선 - 빨간색
				if (badgeLayout != null) badgeLayout.setBackgroundResource(R.drawable.bus_badge_express);
				convertView.setBackgroundResource(R.drawable.bg_item_express);
				busNum.setTextColor(android.graphics.Color.WHITE);
				st_edStName.setTextColor(android.graphics.Color.parseColor("#C62828"));
				st_edTime.setTextColor(android.graphics.Color.parseColor("#B71C1C"));
			} else if (!busNumStr.isEmpty() && !busNumStr.matches("\\d+")) {
				// 숫자 외 한글/특수문자 포함 (마을버스, 지역명 등) - 주황색
				if (badgeLayout != null) badgeLayout.setBackgroundResource(R.drawable.bus_badge_village);
				convertView.setBackgroundResource(R.drawable.bg_item_village);
				busNum.setTextColor(android.graphics.Color.WHITE);
				st_edStName.setTextColor(android.graphics.Color.parseColor("#E65100"));
				st_edTime.setTextColor(android.graphics.Color.parseColor("#D84315"));
			} else {
				// 숫자만 (일반 노선) - 기본 파란색
				if (badgeLayout != null) badgeLayout.setBackgroundResource(R.drawable.bus_num_badge_bg);
				convertView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
				busNum.setTextColor(android.graphics.Color.WHITE);
				st_edStName.setTextColor(android.graphics.Color.parseColor("#0D47A1"));
				st_edTime.setTextColor(android.graphics.Color.parseColor("#1565C0"));
			}

			Log.i("JunDebug", "myItem.getRouteId() " + myItem.getRouteId());
			Log.i("JunDebug", "FavoriteKey SIze --> " + FavoriteKey.size());
			//Log.i("JunDebug" , "FavoriteBusValue.get(position " + FavoriteBusValue.get(position));

			if (KeyExist && FavoriteBuscheckByRouteID.size() > 0) {

				Log.i("JunDebugchk", " FavoriteBuscheckByRouteID. size ()  -->  " + FavoriteBuscheckByRouteID.size());
				Log.i("JunDebugchk", " Position -->  " + position);

				String RouteID = myItem.getRouteId();

//				if (RouteID.contains("354300200")){
//					Log.i("JunDebugchk", " RouteID  -->  " + RouteID);
//					FavoriteBuscheckByRouteID.add(RouteID);
//				}

				int favoriteIdx = FavoriteBuscheckByRouteID.indexOf(RouteID);

				if (favoriteIdx != -1) {
					Log.i("JunDebug", "myItem.getRouteId() " + myItem.getRouteId());
					star.setChecked(true);

				} else {
					star.setChecked(false);
				}


			}
			return convertView;
		}


		private OnCheckedChangeListener mStarCheckedChanceChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Cyril: Not implemented yet!

				int index = (Integer) buttonView.getTag();
				Log.i("JunDebug", "onCheckedChanged " + index);

				//Log.i("JunDebug", "Bus Number -->  " + businfoList.get(index).getBusNum());
				//FavoriteBusValue.set (index, businfoList.get(index).getRouteID());
				String RouteID = businfoList.get(index).getRouteID();
				Log.i("JunDebugchk", "Bus Route  -->  " + businfoList.get(index).getRouteID());

				if (isChecked) {

					Log.i("JunDebug", "checked Favorite");
					Log.i("JunDebug", "FavoriteBuscheckByRouteID index --> " +  FavoriteBuscheckByRouteID.indexOf(RouteID) );


					if (FavoriteBuscheckByRouteID.indexOf(RouteID) == -1) {

						FavoriteBuscheckByRouteID.add(RouteID);
						FavoriteBusKey.add(businfoList.get(index).getBusNum());
						FavoriteBusValue.add(businfoList.get(index).getRouteID());

						FavoriteBusStartTime.add(businfoList.get(index).getStartTime());
						FavoriteBusEndTime.add(businfoList.get(index).getEndTime());
						FavoriteBus_sStation.add(businfoList.get(index).getStartName());
						FavoriteBus_eStation.add(businfoList.get(index).getEndName());
					}

				} else {

					//FavoriteBuscheck.set(index, FAVORITE_FALSE);
					int favoriteIdx = FavoriteBuscheckByRouteID.indexOf(RouteID);

					if (favoriteIdx != -1) {

						FavoriteBuscheckByRouteID.remove(favoriteIdx);

						FavoriteBusKey.remove(favoriteIdx);
						FavoriteBus_sStation.remove(favoriteIdx);
						FavoriteBus_eStation.remove(favoriteIdx);
						FavoriteBusStartTime.remove(favoriteIdx);
						FavoriteBusEndTime.remove(favoriteIdx);
						FavoriteBusValue.remove(favoriteIdx);
					}

				}

				//removeRedundantRouteID();

				//setStringArrayPref(FavoritStr.FAVORITE_BUS_CHECK, FavoriteBuscheck);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_BY_ROUTID, FavoriteBuscheckByRouteID);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_KEY, FavoriteBusKey);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_START_STATION, FavoriteBus_sStation);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_END_STATION, FavoriteBus_eStation);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_START_TIME, FavoriteBusStartTime);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_END_TIME, FavoriteBusEndTime);
				setStringArrayPref(FavoritStr.FAVORITE_BUS_VALUE, FavoriteBusValue);

			}
		};


		/* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
		public void addItem(String BusNum, String BusStation, String BusTime, String RouteId) {

			MyItem mItem = new MyItem();

			/* MyItem에 아이템을 setting한다. */
			mItem.setBusNum(BusNum);
			mItem.setBusStation(BusStation);
			mItem.setBusTime(BusTime);
			mItem.setRouteId(RouteId);

			/* mItems에 MyItem을 추가한다. */
			mItems.add(mItem);
		}
	}

	private void removeRedundantRouteID() {

		ArrayList <String > resultList  = new ArrayList<String>();
			for (int i = 0; i < FavoriteBuscheckByRouteID.size(); i++) {
				if (!resultList.contains(FavoriteBuscheckByRouteID.get(i))) {
					resultList.add(FavoriteBuscheckByRouteID.get(i));
				}
		}

		FavoriteBuscheckByRouteID = resultList;
	}
	private void BusdataUpdate(){

		AllBusRouteID.clear();
		removeDatabyKey(FavoritStr.ALL_BUS_ROUTEID);

		for (int i = 0; i < businfoAllList.size(); i++) {
			//int curworkingNum  = businfoAllList.get(i).getNumofworking();
			AllBusRouteID.add(businfoAllList.get(i).getRouteID());
		}

		for (int i = 0; i < businfoList.size(); i++) {

			String startName = businfoList.get(i).getStartName();
			String endName = businfoList.get(i).getEndName();
			String startTime = businfoList.get(i).getStartTime();
			String endTime = businfoList.get(i).getEndTime();

			// 노선 정보: "안동 → 예안" 형식으로 표시
			String StationInfo = startName + " → " + endName;

			// 운행 시간: "06:30 ~ 22:00" 형식으로 표시
			String BusTimeInfo = startTime + " ~ " + endTime;

			String RID = businfoList.get(i).getRouteID();

			mMyAdapter.addItem(businfoList.get(i).getBusNum(), StationInfo, BusTimeInfo, RID);

		}

//		Intent intent = new Intent(iContext, Tab3.class);
//		intent.putParcelableArrayListExtra(FavoritStr.ALL_BUS_ROUTEID);
//		startActivity(intent);

		//Tab3 obj = new Tab3(AllBusRouteID);

		Fragment delivery = new Tab3();//프래그먼트2 선언
		Bundle bundle = new Bundle(); // 번들을 통해 값 전달
		bundle.putStringArrayList("name", (ArrayList <String>) AllBusRouteID);//번들에 넘길 값 저장
		delivery.setArguments(bundle);

		setStringArrayPref(FavoritStr.ALL_BUS_ROUTEID, AllBusRouteID);

	}

	private class GetXMLTask extends ThreadTask<String, Integer ,Document> {

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

		private void XMLparserLocal (String jsonData){

			Log.i("JunDebug" ,"XMLparserLocal start");

			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int result  =  jsonObject.getInt("code");

				if(result ==200 ){
					Log.i("JunDebug" ,"Get 200 success");
				}
				else{
					return;
				}

			} catch (JSONException e) {
				//some exception handler code.

				e.printStackTrace();
				Log.i("JunDebug", "Exception Json Object ");
			}

			try {
				JSONArray jarray = new JSONObject(jsonData).getJSONArray("results");

				Log.i("JunDebug", "Parsing  num of jarray  " + jarray.length());


				for (int i = 0; i < jarray.length(); i++) {

					JSONObject jObject = jarray.getJSONObject(i);
					String runcnt =jObject.optString("runCnt", "noValue");
					String runTotCntNm = jObject.optString("runTotCnt");
					String BusNumStr = jObject.optString("routeNum");

					Log.i("JunDebug", "Bus Run Count number -->   " + runTotCntNm);





					if(runcnt.compareTo("noValue") == 0 ) {

						// minla
						//continue;
					}else{
						int cntIdx = Integer.parseInt(runcnt);
						//if(cntIdx < 3)
						//		continue;
					}

					if(routeNum.contains(BusNumStr)){
						//continue;
					}

					String sStation =  jObject.optString("stStationNm");
					String eStation =  jObject.optString("edStationNm");
					String sTime  = jObject.optString("stTm");
					String eTime  = jObject.optString("edTm");
					String routeID  = jObject.optString("routeId");

					int idx = BusNumStr.indexOf("-");
					String busidx = "";
					int runCnt  = Integer.parseInt(runTotCntNm);

					businfoAllList.add(new BusInfo (BusNumStr, sStation, eStation, sTime, eTime, busidx, routeID, runCnt));



					if(runCnt == 0 ){

						continue;
					}


					if(idx == -1)
					{
						busidx = BusNumStr;
					}else{
						busidx = BusNumStr.substring(0, idx);
					}

					Log.i("JunDebug", "Bus name  " + BusNumStr);
					Log.i("JunDebug", "index number   " + idx);

					businfoList.add(new BusInfo (BusNumStr, sStation, eStation, sTime, eTime, busidx, routeID, runCnt));
					routeNum.add(BusNumStr);

				}


				Collections.sort(businfoList);
				Log.i("JunDebug", "processed Bus num list size" + routeNum.size());


			}catch (JSONException e) {
				//some exception handler code.
				e.printStackTrace();
				Log.i("JunDebug", "Exception Json jarray ");
			}
		}
		@Override
		protected void onPreExecute() {
			Log.i("JunDebug" ,"onPreExecute start ");
			businfoList.clear();
			businfoAllList.clear();

			FavoriteBuscheckByRouteID =   getStringArrayPref (FavoritStr.FAVORITE_BUS_BY_ROUTID);

		}


		@Override
		protected Document doInBackground(String urls, Integer mode) {

			Log.i("JunDebug" ,"doInBackground start ");
			String  url = urls;
			Integer Mode = mode;


			StringBuilder sBuffer = new StringBuilder();

			try {

				URL url2 = new URL("http://bus.andong.go.kr:8080/api/route/getDataList?type=All");

				InputStream inputStream = url2.openStream();

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

				XMLparserLocal(xmlStr);

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
			BusdataUpdate();
		}
	}

	public void setStringArrayPref(String key, ArrayList<String> values) {
		SharedPreferences prefs = iContext.getSharedPreferences(FavoritStr.FAVORITE_BUS_SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		JSONArray a = new JSONArray();
		for (int i = 0; i < values.size(); i++) {
			a.put(values.get(i));
		}
		if (!values.isEmpty()) {
			editor.putString(key, a.toString());
		} else {
			editor.putString(key, null);
		}
		editor.commit();
	}
	public void removeDatabyKey(String removekey){

		SharedPreferences prefs = iContext.getSharedPreferences(FavoritStr.FAVORITE_BUS_SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		for (String key : prefs.getAll().keySet()) {
			if (key.contains(removekey)) {
				editor.remove(key);
			}
		}
		editor.apply();
	}

	public ArrayList<String> getStringArrayPref(String key) {
		SharedPreferences prefs = iContext.getSharedPreferences(FavoritStr.FAVORITE_BUS_SETTING, MODE_PRIVATE);
		String json = prefs.getString(key, null);
		ArrayList<String> urls = new ArrayList<String>();

		if (json != null) {
			try {
				JSONArray a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					String url = a.optString(i);
					urls.add(url);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}


}


