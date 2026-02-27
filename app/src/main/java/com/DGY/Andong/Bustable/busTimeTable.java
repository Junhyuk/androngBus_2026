package com.DGY.Andong.Bustable;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class busTimeTable extends AppCompatActivity {

    private static final int DESKTOP = 4;

    String THEME_NAME = "시외버스 시간표";

    private String BusStr = "";
    private int BusID = -1;
    private TableLayout tbLayout;

    String[][] Time1 = {
            {"05:30",	"06:40",	"07:20",	"08:00",    "08:30",	"08:50",	"09:40",	"10:30",	"10:50",	"11:10",	"11:40",	"12:20",	"12:50",	"13:20",	"13:50",	"14:20",	"14:45",	"15:00",	"15:20",	"15:40",	"16:00",	"16:20",	"16:40",	"17:00",	"17:20",	"17:40",	"18:00",	"19:00",	"19:30",	"20:00",	"20:40",	"22:00"},
            {"06:00",	"06:30",	"07:00",	"07:30",	"08:00",	"08:20",	"08:40",	"09:00",	"09:20",	"09:50",	"10:20",	"10:40",	"11:00",	"11:20",	"11:40",	"12:00",	"12:30",	"13:00",	"13:20",	"13:50",	"14:30",	"15:00",	"15:30",	"15:50",	"16:10",	"16:40",	"17:00",	"17:30",	"18:00",	"18:30",	"18:50",	"19:20",	"20:00",	"20:40",	"22:30"},
            {"05:50",	"07:00",	"08:00",	"08:20",	"08:50",	"09:40",	"10:10",	"10:50",	"11:20",	"12:10",	"13:00",	"13:50",	"14:40",	"15:20",	"15:50",	"16:30",	"17:10",	"17:50",	"18:30",	"19:00",	"19:30",	"20:10",	"22:00"},
            {"06:10",	"06:50",	"07:40",	"08:10",	"09:00",	"09:40",	"10:10",	"11:00",	"11:50",	"12:30",	"13:20",	"14:00",	"14:50",	"15:30",	"16:20",	"17:00",	"17:50",	"18:40",	"19:30",	"20:10",	"22:00"},//done
            {"06:40",	"07:50",	"09:00",	"10:10",	"11:20",	"12:30",	"13:40",	"14:50",	"16:00",	"17:10",	"18:20",	"19:30"},
            {"04:00",	"06:00",	"10:00",	"14:00"},
            {"12:20 ",	"12:40",	"15:20 ",	"15:40",	"17:00",	"17:20"},
            {"07:10",	"08:20",	"09:25",	"10:40",	"11:50",	"13:00",	"14:05",	"15:20",	"16:25",	"17:40",	"18:50",	"20:00"},
            {"08:00",	"09:00",	"10:00",	"11:00",	"12:00",	"13:00",	"14:00",	"15:00",	"16:00",	"17:00",	"18:10",	"19:20",	"20:30"},
            {"09:50",	"13:55",	"16:50",	"19:50"},
            {"12:00",	"14:00",	"18:30"},
            {"08:00",	"09:00",	"11:00",	"12:30",	"14:00",	"15:30",	"17:00",	"18:00",	"19:00"},
            {"09:30",	"11:30",	"13:40",	"16:30",	"18:30",	"20:30"},
            {"08:10",	"09:10",	"13:05",	"14:30",	"17:30",	"19:40"},
            {"08:45",	"10:25",	"13:25",	"15:25",	"17:25",	"19:30"},
            {"09:20",	"16:40"},
            // [16] 안동 -> 원주
            {"08:10",	"09:10",	"13:05",	"14:30",	"17:30",	"19:40"},
            // [17] 원주 -> 안동
            {"06:45",	"08:25",	"11:25",	"13:30",	"15:30",	"17:25"},
            // [18] 안동 -> 구미
            {"07:50",	"08:45",	"10:10",	"10:25",	"11:20",	"13:25",	"15:25",	"16:30",	"17:25",	"19:30",	"20:00"},
            // [19] 구미 -> 안동
            {"06:40",	"07:30",	"07:40",	"11:35",	"13:00",	"13:15",	"15:25",	"16:00",	"17:00",	"18:10",	"20:25"},
            // [20] 안동 -> 대구(북부)
            {"06:30",	"08:30",	"08:50",	"09:00",	"10:20",	"12:00",	"12:55",	"13:55",	"14:45",	"15:55",	"17:10",	"17:30",	"18:15",	"19:55",	"22:00"},
            // [21] 대구(북부) -> 안동
            {"07:00",	"08:20",	"09:10",	"10:10",	"10:40",	"11:45",	"12:30",	"14:15",	"14:30",	"16:40",	"17:10",	"19:20",	"20:00",	"20:30"},
            // [22] 안동 -> 포항
            {"08:05",	"10:25",	"12:05",	"14:35",	"16:15",	"18:25",	"19:55"},
            // [23] 안동 -> 영덕
            {"10:00",	"12:10",	"13:30",	"15:40",	"16:20",	"17:50",	"21:20"},
            // [24] 영덕 -> 안동
            {"06:40",	"07:40",	"09:30",	"11:30",	"13:40",	"13:50",	"15:40",	"17:00",	"18:00",	"18:40"}
    };

    // 도착 시간 배열
    String[][] Time2 = {
            {}, // [0] 동서울 도착 시간 없음
            {}, // [1]
            {}, // [2]
            {}, // [3]
            {}, // [4]
            {}, // [5]
            {}, // [6]
            {}, // [7]
            {}, // [8]
            {}, // [9]
            {}, // [10]
            {}, // [11]
            {}, // [12]
            {}, // [13]
            {}, // [14]
            {}, // [15]
            // [16] 안동 -> 원주
            {"09:53",	"10:53",	"14:48",	"16:13",	"19:13",	"21:23"},
            // [17] 원주 -> 안동
            {"08:25",	"10:05",	"13:05",	"15:10",	"17:10",	"19:05"},
            // [18] 안동 -> 구미
            {"10:37",	"10:40",	"12:21",	"12:20",	"14:07",	"15:20",	"17:20",	"19:17",	"19:20",	"21:25",	"22:47"},
            // [19] 구미 -> 안동
            {"07:59",	"09:43",	"08:59",	"12:54",	"14:19",	"15:16",	"17:28",	"17:19",	"19:13",	"19:29",	"22:26"},
            // [20] 안동 -> 대구(북부)
            {"08:12",	"09:44",	"10:04",	"10:42",	"11:34",	"13:42",	"14:37",	"15:09",	"15:59",	"17:37",	"18:24",	"19:12",	"19:57",	"21:09",	"23:14"},
            // [21] 대구(북부) -> 안동
            {"08:33",	"09:27",	"10:17",	"11:17",	"12:13",	"13:18",	"13:38",	"15:22",	"16:16",	"18:13",	"18:17",	"20:27",	"21:46",	"21:37"},
            // [22] 안동 -> 포항
            {"10:28",	"12:48",	"13:59",	"16:58",	"18:09",	"20:48",	"21:49"},
            // [23] 안동 -> 영덕
            {"11:04",	"13:14",	"14:34",	"16:44",	"17:24",	"18:54",	"22:24"},
            // [24] 영덕 -> 안동
            {"07:41",	"08:41",	"10:31",	"12:31",	"14:41",	"14:51",	"16:41",	"18:01",	"19:01",	"19:41"}
    };

    // 요금 배열 (성인 기준)
    String[][] Price1 = {
            {}, // [0]
            {}, // [1]
            {}, // [2]
            {}, // [3]
            {}, // [4]
            {}, // [5]
            {}, // [6]
            {}, // [7]
            {}, // [8]
            {}, // [9]
            {}, // [10]
            {}, // [11]
            {}, // [12]
            {}, // [13]
            {}, // [14]
            {}, // [15]
            // [16] 안동 -> 원주 18,600원
            {"18,600원","18,600원","18,600원","18,600원","18,600원","18,600원"},
            // [17] 원주 -> 안동 18,600원
            {"18,600원","18,600원","18,600원","18,600원","18,600원","18,600원"},
            // [18] 안동 -> 구미 11,200원
            {"11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원"},
            // [19] 구미 -> 안동 11,200원
            {"11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원","11,200원"},
            // [20] 안동 -> 대구(북부)
            {"11,000원","11,000원","11,000원","8,400원","11,000원","11,000원","8,400원","11,000원","11,000원","11,000원","11,000원","11,000원","11,000원","11,000원","10,100원"},
            // [21] 대구(북부) -> 안동
            {"8,400원","11,000원","11,000원","11,000원","11,000원","11,000원","8,400원","11,000원","11,000원","8,400원","11,000원","11,000원","11,000원","11,000원"},
            // [22] 안동 -> 포항 18,000원
            {"18,000원","18,000원","18,000원","18,000원","18,000원","18,000원","18,000원"},
            // [23] 안동 -> 영덕 11,600원
            {"11,600원","11,600원","11,600원","11,600원","11,600원","11,600원","11,600원"},
            // [24] 영덕 -> 안동
            {"11,600원","15,100원","11,600원","11,600원","11,600원","11,600원","11,600원","11,600원","11,600원","11,600원"}
    };

    String[][] Etc1 = {
            {"우등", "일반", "일반", "우등", "우등", "우등", "일반", "일반", "우등", "우등", "우등", "일반", "우등", "일반", "우등", "우등", "우등", "일반", "우등", "일반", "우등", "우등", "일반", "일반", "우등", "우등", "우등", "우등", "우등", "우등", "우등", "심야우등"},
            {"일반",	"우등",	"일반",	"우등",	"일반",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"일반",	"일반",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"심야우등"},
            {"우등",	"일반",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"일반",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"심야우등"},
            {"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"심야우등"},
            {"일반",	"일반",	"일반(신도청 경유) ",	"일반",	"일반",	"일반",	"일반",	"일반",	"일반(신도청 경유) ",	"일반",	"일반",	"일반"},
            {"안동-도청-예천-점촌-공항",	"안동-영주-공항",	"안동-도청-예천-점촌-공항",	"안동-영주-공항"},
            {"인천공항 2터미널",	"인천공항 1터미널",	"인천공항 2터미널",	"인천공항 1터미널",	"인천공항 2터미널",	"인천공항 1터미널"},
            {"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)",	"수원 (영주 경유)"},
            {"우등",	"일반",	"일반",	"우등",	"우등",	"일반",	"일반",	"우등",	"우등",	"우등",	"일반",	"일반",	"우등"},
            {"일반.우등", "일반.우등",	"일반.우등",	"일반.우등"},
            {"일반",	"일반",	"일반"},
            {"우등",	"일반",	"우등",	"우등",	"우등",	"일반",	"일반",	"우등",	"우등"},
            {"예천경유",	"예천경유",	"예천경유",	"예천경유",	"예천경유",	"예천경유"},
            {"영주경유",	"영주경유",	"영주경유",	"영주경유",	"영주경유",	"영주경유"},
            {"공단경유",	"공단경유",	"공단경유",	"공단경유",	"공단경유",	"공단경유"},
            {"영주경유",	"영주경유"},
            // [16] 안동 -> 원주
            {"우등",	"우등",	"우등",	"우등",	"우등",	"우등"},
            // [17] 원주 -> 안동
            {"우등",	"우등",	"우등",	"우등",	"우등",	"우등"},
            // [18] 안동 -> 구미
            {"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등"},
            // [19] 구미 -> 안동
            {"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등"},
            // [20] 안동 -> 대구(북부)
            {"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"일반"},
            // [21] 대구(북부) -> 안동
            {"일반",	"우등",	"우등",	"우등",	"우등",	"우등",	"일반",	"우등",	"우등",	"일반",	"우등",	"우등",	"우등",	"우등"},
            // [22] 안동 -> 포항
            {"일반",	"일반",	"일반",	"일반",	"일반",	"일반",	"일반"},
            // [23] 안동 -> 영덕
            {"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등"},
            // [24] 영덕 -> 안동
            {"우등",	"프리미엄",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등",	"우등"}


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableview);

        // 뒤로가기 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            BusStr = bundle.getString("BusStrID");
            BusID = bundle.getInt("BusStrINTID");

            Log.i("JunDebug" , "BusID-->" + BusID);
        }



        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(THEME_NAME);
        }


        TextView busstringView = findViewById(R.id.LargeTextExt);
        busstringView.setText(BusStr);

        tbLayout = findViewById(R.id.tableLayout);

        TimeTableUpdate(BusID);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void TimeTableUpdate(int index) {

        // 배열 범위 초과 방어 처리
        if (index < 0 || index >= Time1.length) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvMsg = new TextView(this);
            tvMsg.setText("시간표 정보가 없습니다.");
            tvMsg.setGravity(Gravity.CENTER);
            tvMsg.setTextSize(16);
            tvMsg.setTextColor(Color.parseColor("#999999"));
            tvMsg.setPadding(dpToPx(16), dpToPx(32), dpToPx(16), dpToPx(32));
            tableRow.addView(tvMsg);
            tbLayout.addView(tableRow);
            return;
        }

        boolean hasArrival = (index < Time2.length && Time2[index].length == Time1[index].length);
        boolean hasPrice   = (index < Price1.length && Price1[index].length == Time1[index].length);

        // ── 헤더 행 추가 ────────────────────────────────────
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.parseColor("#1565C0"));
        addHeaderCell(headerRow, "출발");
        if (hasArrival) addHeaderCell(headerRow, "도착");
        addHeaderCell(headerRow, "등급");
        if (hasPrice) addHeaderCell(headerRow, "요금(성인)");
        tbLayout.addView(headerRow);

        View headerDivider = new View(this);
        TableLayout.LayoutParams hdParams = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(2));
        headerDivider.setLayoutParams(hdParams);
        headerDivider.setBackgroundColor(Color.parseColor("#0D47A1"));
        tbLayout.addView(headerDivider);

        for (int k = 0; k < Time1[index].length; k++) {

            // ── 행 컨테이너 ──────────────────────────────────
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(64)));   // 행 높이 64dp 고정

            // 홀짝 행 배경 구분
            boolean isEven = (k % 2 == 0);
            int rowBg = isEven
                    ? Color.parseColor("#FFFFFF")
                    : Color.parseColor("#F8FAFE");
            tableRow.setBackgroundColor(rowBg);

            // ── 출발 시간 셀 ──────────────────────────────────
            TextView tvTime = new TextView(this);
            tvTime.setText(Time1[index][k].trim());
            tvTime.setGravity(Gravity.CENTER);
            tvTime.setTextSize(17);
            tvTime.setTextColor(Color.parseColor("#0D47A1"));
            tvTime.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTime.setPadding(dpToPx(10), dpToPx(18), dpToPx(10), dpToPx(18));
            tvTime.setBackgroundColor(rowBg);
            tableRow.addView(tvTime);

            // ── 도착 시간 셀 (있는 경우만) ────────────────────
            if (hasArrival) {
                TextView tvArrival = new TextView(this);
                tvArrival.setText(Time2[index][k].trim());
                tvArrival.setGravity(Gravity.CENTER);
                tvArrival.setTextSize(17);
                tvArrival.setTextColor(Color.parseColor("#1B5E20"));
                tvArrival.setTypeface(null, android.graphics.Typeface.BOLD);
                tvArrival.setPadding(dpToPx(10), dpToPx(18), dpToPx(10), dpToPx(18));
                tvArrival.setBackgroundColor(rowBg);
                tableRow.addView(tvArrival);
            }

            // ── 등급/경유 셀 ──────────────────────────────────
            String grade = Etc1[index][k].trim();

            // 배지 배경색·글자색 결정
            int badgeBg;
            int badgeTextColor;
            if (grade.contains("심야")) {
                badgeBg    = Color.parseColor("#FCE4EC");
                badgeTextColor = Color.parseColor("#C62828");
            } else if (grade.equals("프리미엄")) {
                badgeBg    = Color.parseColor("#EDE7F6");
                badgeTextColor = Color.parseColor("#4527A0");
            } else if (grade.equals("우등")) {
                badgeBg    = Color.parseColor("#FFF3E0");
                badgeTextColor = Color.parseColor("#E65100");
            } else if (grade.equals("일반")) {
                badgeBg    = Color.parseColor("#E3F2FD");
                badgeTextColor = Color.parseColor("#1565C0");
            } else if (grade.contains("1터미널")) {
                badgeBg    = Color.parseColor("#E8F5E9"); // 연두색 배경
                badgeTextColor = Color.parseColor("#2E7D32"); // 진한 초록색 글씨
            } else if (grade.contains("2터미널")) {
                badgeBg    = Color.parseColor("#FFF3E0"); // 연한 주황색 배경 (우등과 비슷하지만 구분)
                badgeTextColor = Color.parseColor("#E64A19"); // 진한 주황색 글씨
            } else {
                // 경유 노선 등 기타
                badgeBg    = Color.parseColor("#F3E5F5");
                badgeTextColor = Color.parseColor("#6A1B9A");
            }

            TextView tvGrade = new TextView(this);
            tvGrade.setText(grade);
            tvGrade.setGravity(Gravity.CENTER);
            tvGrade.setTextSize(15);
            tvGrade.setTextColor(badgeTextColor);
            tvGrade.setTypeface(null, android.graphics.Typeface.BOLD);
            tvGrade.setMinHeight(dpToPx(40));

            // 배지 모양 (둥근 사각형)
            android.graphics.drawable.GradientDrawable badgeDrawable =
                    new android.graphics.drawable.GradientDrawable();
            badgeDrawable.setColor(badgeBg);
            badgeDrawable.setCornerRadius(dpToPx(8));

            TableRow.LayoutParams badgeParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(44));   // 배지 높이 44dp 고정
            badgeParams.setMargins(dpToPx(8), dpToPx(10), dpToPx(8), dpToPx(10));
            tvGrade.setLayoutParams(badgeParams);
            tvGrade.setBackground(badgeDrawable);
            tvGrade.setPadding(dpToPx(8), dpToPx(0), dpToPx(8), dpToPx(0));

            tableRow.addView(tvGrade);

            // ── 요금 셀 (있는 경우만) ─────────────────────────
            if (hasPrice) {
                TextView tvPrice = new TextView(this);
                tvPrice.setText(Price1[index][k].trim());
                tvPrice.setGravity(Gravity.CENTER);
                tvPrice.setTextSize(13);
                tvPrice.setTextColor(Color.parseColor("#37474F"));
                tvPrice.setTypeface(null, android.graphics.Typeface.NORMAL);
                tvPrice.setPadding(dpToPx(8), dpToPx(18), dpToPx(8), dpToPx(18));
                tvPrice.setBackgroundColor(rowBg);
                tableRow.addView(tvPrice);
            }

            // ── 행 구분선 ─────────────────────────────────────
            View divider = new View(this);
            TableLayout.LayoutParams divParams = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
            divider.setLayoutParams(divParams);
            divider.setBackgroundColor(Color.parseColor("#E8EEF7"));

            tbLayout.addView(tableRow);
            tbLayout.addView(divider);
        }
    }

    private void addHeaderCell(TableRow row, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(15);
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setPadding(dpToPx(10), dpToPx(18), dpToPx(10), dpToPx(18));
        row.addView(tv);
    }

    // dp → px 변환 헬퍼
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
