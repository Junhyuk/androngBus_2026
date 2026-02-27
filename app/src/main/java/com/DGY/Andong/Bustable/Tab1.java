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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
			TextView busFirstLastTime = (TextView) convertView.findViewById(R.id.BusFirstLastTime);
			android.widget.LinearLayout badgeLayout = (android.widget.LinearLayout) convertView.findViewById(R.id.bus_badge_layout);

			CheckBox star = (CheckBox) convertView.findViewById(R.id.btn_star);
			star.setTag((int) position);
			star.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);

			MyItem myItem = getItem(position);

			/* 각 위젯에 세팅된 아이템을 뿌려준다 */
			busNum.setText(myItem.getBusNum());
			st_edStName.setText(myItem.getBusStation());
			st_edTime.setText(myItem.getBusTime());

			// 첫차/막차 정보 표시
			String firstTime = myItem.getFirstBusTime() != null ? myItem.getFirstBusTime() : "미확인";
			String lastTime = myItem.getLastBusTime() != null ? myItem.getLastBusTime() : "미확인";

			if (busFirstLastTime != null) {
				busFirstLastTime.setText("첫차: " + firstTime + " | 막차: " + lastTime);
			} else {
				Log.e("JunDebug_FirstLast", "busFirstLastTime TextView is NULL! Layout issue detected.");
			}

			// 첫차/막차 정보 로깅
			Log.i("JunDebug_FirstLast", "getView position: " + position + " | BusNum: " + myItem.getBusNum() + " | FirstTime: " + firstTime + " | LastTime: " + lastTime);

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
		public void addItem(String BusNum, String BusStation, String BusTime, String RouteId, String firstBusTime, String lastBusTime) {

			MyItem mItem = new MyItem();

			/* MyItem에 아이템을 setting한다. */
			mItem.setBusNum(BusNum);
			mItem.setBusStation(BusStation);
			mItem.setBusTime(BusTime);
			mItem.setRouteId(RouteId);
			mItem.setFirstBusTime(firstBusTime);
			mItem.setLastBusTime(lastBusTime);

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

			// 첫차/막차 정보 추가
			Log.i("JunDebug_FirstLast", "addItem called - BusNum: " + businfoList.get(i).getBusNum() + " | FirstTime: " + startTime + " | LastTime: " + endTime);
			mMyAdapter.addItem(businfoList.get(i).getBusNum(), StationInfo, BusTimeInfo, RID, startTime, endTime);

		}

		mMyAdapter.notifyDataSetChanged();
		Log.i("JunDebug_FirstLast", "BusdataUpdate notifyDataSetChanged 완료, items=" + mMyAdapter.getCount());

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

				// API 응답 샘플 로깅 (첫 번째 아이템)
				if (jarray.length() > 0) {
					try {
						JSONObject firstItem = jarray.getJSONObject(0);
						String firstItemJson = firstItem.toString();
						Log.i("JunDebug_FirstLast", "Sample API Response: " + firstItemJson.substring(0, Math.min(500, firstItemJson.length())));

						// 모든 키 출력
						Iterator<String> keys = firstItem.keys();
						StringBuilder keyList = new StringBuilder("Available keys: ");
						while (keys.hasNext()) {
							keyList.append(keys.next()).append(", ");
						}
						Log.i("JunDebug_FirstLast", keyList.toString());
					} catch (Exception e) {
						Log.i("JunDebug_FirstLast", "Error logging sample: " + e.getMessage());
					}
				}




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

					// 첫차/막차 정보 로깅
					Log.i("JunDebug_FirstLast", "BusNum: " + BusNumStr + " | FirstTime(stTm): " + sTime + " | LastTime(edTm): " + eTime);

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
			// 시간표 페이지에서 첫차/막차 정보 비동기 로딩
			fetchTimetableAndUpdate();
		}
	}

	/**
	 * https://bus.andong.go.kr/m03/s01.do 시간표 HTML 페이지를 파싱하여
	 * 노선번호별 첫차/막차 시간을 추출하고 ListView를 갱신하는 Task.
	 * ThreadTask 대신 Thread + Handler 방식으로 비동기 처리.
	 */
	private void fetchTimetableAndUpdate() {
		final android.app.Activity act = getActivity();
		if (act == null || act.isFinishing()) {
			Log.e("JunDebug_FirstLast", "fetchTimetableAndUpdate: activity null/finishing");
			return;
		}
		Log.i("JunDebug_FirstLast", "fetchTimetableAndUpdate 시작");
		new Thread(new Runnable() {
			@Override
			public void run() {
				final HashMap<String, String[]> result = parseTimetablePage();
				Log.i("JunDebug_FirstLast", "parseTimetablePage 완료 routes=" + result.size());
				act.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i("JunDebug_FirstLast", "runOnUiThread applyTimetableToUI 시작");
						applyTimetableToUI(result);
					}
				});
			}
		}).start();
	}

	/** H:MM 또는 HH:MM → 분(int) 변환 */
	private int timeToMinutes(String t) {
		try {
			String[] p = t.trim().split(":");
			if (p.length == 2) return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
		} catch (Exception ignored) {}
		return -1;
	}

	/** 시간표 HTML 파싱 → routeNum → [firstStr, lastStr] */
	private HashMap<String, String[]> parseTimetablePage() {
		HashMap<String, String[]> routeFirstLast = new HashMap<>();
		HashMap<String, int[]>   routeMinutes    = new HashMap<>();
		try {
			java.net.HttpURLConnection conn =
					(java.net.HttpURLConnection) new URL("https://bus.andong.go.kr/m03/s01.do?c=20600&i=20610").openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) sb.append(line).append("\n");
			reader.close();
			conn.disconnect();

			String html = sb.toString();
			Log.i("JunDebug_FirstLast", "Timetable HTML length: " + html.length());

			Pattern trPat  = Pattern.compile("<tr[^>]*>(.*?)</tr>",   Pattern.DOTALL);
			Pattern tdPat  = Pattern.compile("<td[^>]*>(.*?)</td>",   Pattern.DOTALL);
			Pattern tagPat = Pattern.compile("<[^>]*>");
			Pattern timePat = Pattern.compile("^\\d{1,2}:\\d{2}$");

			Matcher trM = trPat.matcher(html);
			int parsedRows = 0;
			while (trM.find()) {
				List<String> tds = new ArrayList<>();
				Matcher tdM = tdPat.matcher(trM.group(1));
				while (tdM.find()) tds.add(tagPat.matcher(tdM.group(1)).replaceAll("").trim());

				// 구조: [0]=NO, [1]=노선명, [2]=유형, [3]=노선정보, [4~]=회차 시간
				if (tds.size() < 5) continue;
				String routeName = tds.get(1).trim();
				if (routeName.isEmpty()) continue;
				int pi = routeName.indexOf("(");
				String routeNum = (pi > 0) ? routeName.substring(0, pi).trim() : routeName;
				if (routeNum.isEmpty()) continue;

				// index 3부터 시간 데이터 스캔 (노선정보 칸에도 시간이 없으므로 안전)
				for (int i = 3; i < tds.size(); i++) {
					String ts = tds.get(i).trim();
					if (!timePat.matcher(ts).matches()) continue;
					int mins = timeToMinutes(ts);
					if (mins < 0) continue;

					if (!routeMinutes.containsKey(routeNum)) {
						routeMinutes.put(routeNum, new int[]{Integer.MAX_VALUE, -1});
						routeFirstLast.put(routeNum, new String[]{"", ""});
					}
					int[]    minArr = routeMinutes.get(routeNum);
					String[] strArr = routeFirstLast.get(routeNum);
					if (mins < minArr[0]) { minArr[0] = mins; strArr[0] = ts; }
					if (mins > minArr[1]) { minArr[1] = mins; strArr[1] = ts; }
				}
				parsedRows++;
			}
			Log.i("JunDebug_FirstLast", "Parsed rows=" + parsedRows + ", routes=" + routeFirstLast.size());
			int n = 0;
			for (Map.Entry<String, String[]> e : routeFirstLast.entrySet()) {
				Log.i("JunDebug_FirstLast", "  " + e.getKey() + " 첫차=" + e.getValue()[0] + " 막차=" + e.getValue()[1]);
				if (++n >= 15) break;
			}
		} catch (Exception e) {
			Log.e("JunDebug_FirstLast", "parseTimetablePage error: " + e.getMessage());
		}
		return routeFirstLast;
	}

	/** 파싱된 첫차/막차 데이터를 어댑터 및 SharedPreferences에 반영 */
	private void applyTimetableToUI(HashMap<String, String[]> routeFirstLast) {
		if (routeFirstLast == null || routeFirstLast.isEmpty()) {
			Log.w("JunDebug_FirstLast", "applyTimetableToUI: 데이터 없음");
			return;
		}
		if (mMyAdapter == null) {
			Log.e("JunDebug_FirstLast", "applyTimetableToUI: mMyAdapter null");
			return;
		}
		Log.i("JunDebug_FirstLast", "applyTimetableToUI start, adapter items=" + mMyAdapter.getCount());

		boolean updated = false;
		for (int i = 0; i < mMyAdapter.getCount(); i++) {
			MyItem item = mMyAdapter.getItem(i);
			if (item == null) continue;
			String busNum = item.getBusNum();
			if (busNum == null) continue;
			// "110-1" → "110"
			String key = busNum.contains("-") ? busNum.substring(0, busNum.indexOf("-")).trim() : busNum.trim();
			String[] times = routeFirstLast.get(key);
			if (times != null && !times[0].isEmpty()) {
				item.setFirstBusTime(times[0]);
				item.setLastBusTime(times[1]);
				// 운행시간 텍스트도 갱신 (BusTime 칸: "첫차 ~ 막차")
				item.setBusTime(times[0] + " ~ " + times[1]);
				// BusInfo 동기화
				if (i < businfoList.size()) {
					businfoList.get(i).startTime = times[0];
					businfoList.get(i).endTime   = times[1];
				}
				Log.i("JunDebug_FirstLast", "Updated [" + i + "] " + busNum + " 첫차=" + times[0] + " 막차=" + times[1]);
				updated = true;
			} else {
				Log.w("JunDebug_FirstLast", "No match for busNum=" + busNum + " key=" + key);
			}
		}

		if (updated) {
			mMyAdapter.notifyDataSetChanged();
			Log.i("JunDebug_FirstLast", "notifyDataSetChanged 호출 완료");
		}

		// 즐겨찾기(Tab3) SharedPreferences 동기화
		for (int i = 0; i < FavoriteBuscheckByRouteID.size(); i++) {
			String routeID = FavoriteBuscheckByRouteID.get(i);
			for (BusInfo info : businfoList) {
				if (!routeID.equals(info.getRouteID())) continue;
				String key = info.busNum.contains("-")
						? info.busNum.substring(0, info.busNum.indexOf("-")).trim()
						: info.busNum.trim();
				String[] times = routeFirstLast.get(key);
				if (times != null && !times[0].isEmpty()) {
					if (i < FavoriteBusStartTime.size()) FavoriteBusStartTime.set(i, times[0]);
					if (i < FavoriteBusEndTime.size())   FavoriteBusEndTime.set(i, times[1]);
				}
				break;
			}
		}
		if (!FavoriteBusStartTime.isEmpty()) {
			setStringArrayPref(FavoritStr.FAVORITE_BUS_START_TIME, FavoriteBusStartTime);
			setStringArrayPref(FavoritStr.FAVORITE_BUS_END_TIME,   FavoriteBusEndTime);
			Log.i("JunDebug_FirstLast", "즐겨찾기 첫차/막차 SharedPreferences 저장 완료");
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


