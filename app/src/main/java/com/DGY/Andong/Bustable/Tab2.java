package com.DGY.Andong.Bustable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class Tab2 extends Fragment{

	public String[] Destination = {
			"안동 → 동서울", "동서울 → 안동",                              // 0, 1
			"안동 → 고속버스터미널(고터)", "고속버스터미널(고터) → 안동",   // 2, 3
			"안동 → 인천", "안동 → 인천공항", "인천공항 → 안동",            // 4, 5, 6
			"안동 → 수원",                                                  // 7
			"안동 → 부산", "부산 → 안동",                                   // 8, 9
			"안동 → 경주.울산", "안동 → 마산.창원",                          // 10, 11
			"안동 → 대전", "대전 → 안동",                                   // 12, 13
			"안동 → 청주", "청주 → 안동",                                   // 14, 15
			"안동 → 원주", "원주 → 안동",                                   // 16, 17
			"안동 → 구미", "구미 → 안동",                                   // 18, 19
			"안동 → 대구(북부)", "대구(북부) → 안동",                        // 20, 21
			"안동 → 포항",                                                  // 22
			"안동 → 영덕", "영덕 → 안동"                                    // 23, 24
	};

	public Integer[] ExtBusID ;

	public Context mContext;
	public Context iContext;

	private  ListView BusTerminalTable;
	private Tab2.MyAdapterExt mMyAdapter;
	private List<BusExtInfo> businfoList = new ArrayList<BusExtInfo>();


	public Tab2 (Context context) {
		mContext = context;
	}
	public Tab2(){

	}


	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		//super.onCreate(savedInstanceState);

			//setContentView(R.layout.activity_main);
		View view = inflater.inflate(R.layout.activity_tab2, null);
		iContext = inflater.getContext();

		BusTerminalTable = (ListView) view.findViewById(R.id.tab2_listView);

		//add admob banner type//
		mMyAdapter = new MyAdapterExt();




		for(int i =0; i < Destination.length ; i ++) {

			businfoList.add(new BusExtInfo(Destination[i], i));
			Log.i("JunDebug", "Debugging string " + Destination[i]);
		}

		BusExtUIUpdate();

		/* 리스트뷰에 어댑터 등록 */
		BusTerminalTable.setAdapter(mMyAdapter);

		// AdMob 광고 초기화
		loadAdMobBanner();

		BusTerminalTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object listItem = BusTerminalTable.getItemAtPosition(position);

				Intent intent = new Intent(iContext , busTimeTable.class);

				intent.putExtra("BusStrID", businfoList.get(position).getBusID());
				intent.putExtra("BusStrINTID", businfoList.get(position).getBusINTID());

				Log.i("JunDebug", "onItemClick function" + " " + businfoList.get(position).getBusID());

				startActivity(intent);
				Log.i("JunDebug", "Postion Number !--> " + position);
			}
		});

		return view;
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

	private void loadAdMobBanner() {
		if (getView() == null) return;
		com.google.android.gms.ads.AdView mAdView = getView().findViewById(R.id.adView_tab2);
		if (mAdView != null) {
			com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}
	}

	public class MyAdapterExt extends BaseAdapter {

		/* 아이템을 세트로 담기 위한 어레이 */
		private ArrayList<MyBusExtItem> mItems = new ArrayList<>();

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public MyBusExtItem getItem(int position) {
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
				convertView = inflater.inflate(R.layout.listview_custom4, parent, false);
			}

			/* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
			//ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			TextView busNum = (TextView) convertView.findViewById(R.id.Busstation3);
			MyBusExtItem myItem = getItem(position);

			/* 각 위젯에 세팅된 아이템을 뿌려준다 */
			busNum.setText(myItem.getBusStr());

			Log.i("JunDebug", "MyBusExtItem Bus string name " + myItem.getBusStr());

			return convertView;
		}

		/* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
		public void addItem(String BusStr, Integer BusINTID) {

			MyBusExtItem mItem = new MyBusExtItem();

			/* MyItem에 아이템을 setting한다. */
			mItem.setBusStr(BusStr);
			mItem.setBusOrderINT(BusINTID);
			/* mItems에 MyItem을 추가한다. */
			mItems.add(mItem);
		}
	}


	class BusExtInfo {

		String BusID;
		int BusIntID;

		public BusExtInfo(String BusID, int BusIntID) {

			this.BusID = BusID;
			this.BusIntID  = BusIntID;

		}

		public String getBusID() {
			return this.BusID;
		}

		public int getBusINTID() {
			return this.BusIntID;
		}
	}

	private void BusExtUIUpdate(){

		for (int i = 0; i < businfoList.size(); i++) {

			String  Busstr = businfoList.get(i).getBusID();
			Integer BusINTID = businfoList.get(i).getBusINTID();
			mMyAdapter.addItem(businfoList.get(i).getBusID(), BusINTID);
			//Log.i("JunDebug", "Bus EndTime --> " + businfoList.get(i).getEndName());
		}

	}




}