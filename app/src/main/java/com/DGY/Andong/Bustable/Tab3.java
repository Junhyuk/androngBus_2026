package com.DGY.Andong.Bustable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import static android.content.Context.MODE_PRIVATE;

import com.DGY.Andong.Bustable.StringValue;

@SuppressLint("ValidFragment")
public class Tab3 extends Fragment {


	public Context mContext;
	public Context iContext;

	private Boolean KeyExist = false;
	private Boolean AddItemFlag = true;

	private static final String FAVORITE_TRUE = "true";
	private static final String FAVORITE_FALSE = "false";

	ArrayList<String> FavoriteBusKey   = new ArrayList<String>();
	ArrayList<String> FavoriteBusValue = new ArrayList<String>();

	ArrayList<String> FavoriteBuscheck = new ArrayList<String>();

	private ArrayList<String> FavoriteBuscheckByRouteID = new ArrayList<String>();

	ArrayList<String> FavoriteBusStartTime = new ArrayList<String>();
	ArrayList<String> FavoriteBusEndTime = new ArrayList<String>();

	ArrayList<String> FavoriteBus_sStation = new ArrayList<String>();
	ArrayList<String> FavoriteBus_eStation= new ArrayList<String>();

	private ArrayList<String> AllBusRouteID = new ArrayList<String>();

	private  ListView BusinfoTable;
	Tab3.MyAdapter mMyAdapter;

	List<BusInfo> businfoList = new ArrayList<BusInfo>();

	public Tab3(Context context) {
		mContext = context;
	}

	public Tab3(){
		if (getArguments() != null)
		{
			AllBusRouteID = getArguments().getStringArrayList(com.DGY.Andong.Bustable.StringValue.ALL_BUS_ROUTEID); // 프래그먼트1에서 받아온 값 넣기
		}
	}


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_tab3, null);
		iContext = inflater.getContext();
		Bundle bundle = getArguments();



		if (bundle != null)
		{
			ArrayList <String >  listexample = bundle.getStringArrayList("name"); // 프래그먼트1에서 받아온 값 넣기
			Log.i("JunDebugFavor", "all busRoute Size !! " + listexample.size());
		}


		AllBusRouteID = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.ALL_BUS_ROUTEID);


		//ArrayList<String> FavoriteBusChk  = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_KEY);
		ArrayList<String> FavoriteBusRouteID  = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_BY_ROUTID);

		if(FavoriteBusRouteID.isEmpty()){
			KeyExist = false;
			Log.i("JunDebug", "No Faviorite !! ");
		}
		else{

			KeyExist = true;

			FavoriteBusKey = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_KEY);
			FavoriteBusValue = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_VALUE);
			FavoriteBuscheck = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_CHECK);

			//jh lee 2023 0226

			FavoriteBuscheckByRouteID =  getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_BY_ROUTID);
			FavoriteBusStartTime = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_START_TIME);
			FavoriteBusEndTime = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_END_TIME);
			FavoriteBus_sStation = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_START_STATION);
			FavoriteBus_eStation = getStringArrayPref(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_END_STATION);

			Log.i("JunDebug", "Exist !! " + FavoriteBusValue.size() );
		}

		//businfoList.clear();
		Log.i("JunDebugchk", "businfoList clear !!");
		Log.i("JunDebugchk", "businfoList clear !!");

		if(mMyAdapter != null && !mMyAdapter.isEmpty()){
			mMyAdapter.mItems.clear();
			mMyAdapter.notifyDataSetChanged();
		}

		businfoList.clear();

		//DGB();
		FavoriteBusAdd();

		BusinfoTable = (ListView) view.findViewById(R.id.FavoriteListView);
		mMyAdapter = new MyAdapter();

		/* 리스트뷰에 어댑터 등록 */
		BusinfoTable.setAdapter(mMyAdapter);

		// AdMob 광고 초기화
		loadAdMobBanner();

		BusinfoTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object listItem = BusinfoTable.getItemAtPosition(position);


				Intent intent = new Intent(iContext , second.class);

				intent.putExtra("BusRouteNum", businfoList.get(position).getRouteID());
				intent.putExtra("BusNum", businfoList.get(position).getBusNum());
				intent.putExtra("StartTime", businfoList.get(position).getStartTime());
				intent.putExtra("EndTime", businfoList.get(position).getEndTime());

				intent.putExtra("StartEndTime", businfoList.get(position).getStartName()  +  " < - >" + businfoList.get(position).getEndName());


				Log.i("JunDebug", "onItemClick function" + " " + businfoList.get(position).getRouteID());

				startActivity(intent);
				Log.i("JunDebug", "Postion Number! --> " + position);
			}
		});

		BusdataUpdate();
		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i("JunDebug" , " STop onStop call");
		return ;


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("JunDebug" , "onDestroy ");
	}

	private void DGB(){

		ArrayList <String > resultList = new ArrayList<String>();
		for (int i = 0; i < FavoriteBuscheckByRouteID.size(); i++) {
			if (!resultList.contains(FavoriteBuscheckByRouteID.get(i))) {
				resultList.add(FavoriteBuscheckByRouteID.get(i));
			}
		}
		for(int i =0; i < resultList.size(); i ++) {
			String busRouteID = resultList.get(i);
			Log.i("JunDebugchk", "New Favorite  Route Number --> " + busRouteID);
		}
	}


	public void FavoriteBusAdd(){

		Log.i("JunDebugchk", "FavoriteBusValue List Num  --> " + FavoriteBusValue.size());



		for(int i =0; i < AllBusRouteID.size(); i ++){

			String busRouteID = AllBusRouteID.get(i);
			Log.i("JunDebug", "All bus Route Number --> " + busRouteID);

			int favoriteIdx = FavoriteBuscheckByRouteID.indexOf(busRouteID);

			if(favoriteIdx == -1){
				continue;
			}

			String BusNumStr = FavoriteBusKey.get(favoriteIdx);
			Log.i("JunDebug", "FavoriteBusValue BusNum  --> " + BusNumStr);


			int idx = BusNumStr.indexOf("-");
			String busidx = "";

			if(idx == -1)
			{
				busidx = BusNumStr;
			}else{
				busidx = BusNumStr.substring(0, idx);
			}

			businfoList.add(new Tab3.BusInfo(BusNumStr, FavoriteBus_sStation.get(favoriteIdx), FavoriteBus_eStation.get(favoriteIdx), FavoriteBusStartTime.get(favoriteIdx),
					FavoriteBusEndTime.get(favoriteIdx), busidx, FavoriteBusValue.get(favoriteIdx)));

		}
	}

	private void loadAdMobBanner() {
		if (getView() == null) return;
		com.google.android.gms.ads.AdView mAdView = getView().findViewById(R.id.adView_tab3);
		if (mAdView != null) {
			com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}
	}

	private void BusdataUpdate(){

		Log.i("JunDebug", "Bus List number  --> " + businfoList.size());

		for (int i = 0; i < businfoList.size(); i++) {

			String StationInfo = businfoList.get(i).getStartName()  +  " < - >" + businfoList.get(i).getEndName();
			String BusTimeInfo = "첫차 : " + businfoList.get(i).getStartTime()  +  "  |  막차 : " + businfoList.get(i).getEndTime();
			String RID = businfoList.get(i).getRouteID();


			mMyAdapter.addItem(businfoList.get(i).getBusNum(), 	StationInfo, BusTimeInfo, RID);


			//Log.i("JunDebug", "Bus EndTime --> " + businfoList.get(i).getEndName());
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
				convertView = inflater.inflate(R.layout.listview_custom3, parent, false);
			}

			/* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
			//ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			TextView busNum = (TextView) convertView.findViewById(R.id.BusNum2);
			TextView st_edStName = (TextView) convertView.findViewById(R.id.Busstation2);
			TextView st_edTime = (TextView) convertView.findViewById(R.id.BusTime2);
			android.widget.LinearLayout badgeLayout = (android.widget.LinearLayout) convertView.findViewById(R.id.bus_badge_layout_tab3);

			MyItem myItem = getItem(position);

			/* 각 위젯에 세팅된 아이템을 뿌려준다 */

			Log.i("JunDebug" , "myItem.getBusNum() " + myItem.getBusNum());

			if (busNum ==null)
				Log.i("JunDebug" , "busNum widget is NULL");

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

			Log.i("JunDebug" , "myItem.getRouteId() " + myItem.getRouteId());
			Log.i("JunDebug" , "FavoriteBusValue.get(position " + FavoriteBusValue.get(position));


			return convertView;
		}




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


	public void setStringArrayPref(String key, ArrayList<String> values) {
		SharedPreferences prefs = iContext.getSharedPreferences(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_SETTING, MODE_PRIVATE);
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

	public ArrayList<String> getStringArrayPref(String key) {
		SharedPreferences prefs = iContext.getSharedPreferences(com.DGY.Andong.Bustable.StringValue.FAVORITE_BUS_SETTING, MODE_PRIVATE);
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


	class BusInfo implements Comparable<BusInfo> {

		String busNum;
		String startName;
		String endName;
		String startTime;
		String endTime;
		String busIdx;
		String routeID;

		public BusInfo(String busNum, String startName, String endName, String startTime , String endTime, String busIdx, String routeID) {

			this.busNum = busNum;
			this.startName = startName;
			this.endName = endName;
			this.startTime = startTime;
			this.endTime = endTime;
			this.busIdx = busIdx;
			this.routeID = routeID;
		}

		public String getBusNum() {
			return this.busNum;
		}

		public String getStartName() {
			return this.startName;
		}
		public String getEndName() {
			return this.endName;
		}
		public String getStartTime() {
			return this.startTime;
		}
		public String getEndTime() {
			return this.endTime;
		}
		public String getRouteID() {
			return this.routeID;
		}

		@Override
		public int compareTo(BusInfo s) {

			if(this.busNum.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				return -1;
			} else if( s.busNum.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				return 1;
			}else{

			}

			if (this.busIdx.length() == s.busIdx.length()) {
				return this.busNum.compareTo(s.busNum);
			} else{
				return Integer.compare(this.busIdx.length(), s.busIdx.length());
			}
		}
	}


}
