# 첫차/막차 정보 디버깅 가이드

## 🔍 로그 확인 방법

Android Studio Logcat에서 다음 필터를 사용하여 로그를 확인하세요:

```
JunDebug_FirstLast
```

## 📋 확인 단계별 로그

### 1단계: API 응답 구조 확인
**로그 내용:**
```
Sample API Response: {...JSON 데이터...}
Available keys: routeNum, stStationNm, edStationNm, stTm, edTm, routeId, ...
```

**확인 사항:**
- ✅ `stTm` 필드가 존재하는가?
- ✅ `edTm` 필드가 존재하는가?
- 🛑 이 필드들이 없으면 API 응답 구조 자체가 다름

### 2단계: API 파싱 데이터 확인
**로그 내용:**
```
BusNum: 1 | FirstTime(stTm): 06:00 | LastTime(edTm): 22:30
BusNum: 2 | FirstTime(stTm): 06:15 | LastTime(edTm): 23:00
```

**확인 사항:**
- ✅ `stTm`과 `edTm` 값이 "HH:MM" 형식인가?
- 🛑 "미확인" 또는 빈 값이 나오면 API에서 해당 필드를 제공하지 않음

### 3단계: addItem 메서드로 데이터 전달 확인
**로그 내용:**
```
addItem called - BusNum: 1 | FirstTime: 06:00 | LastTime: 22:30
addItem called - BusNum: 2 | FirstTime: 06:15 | LastTime: 23:00
```

**확인 사항:**
- ✅ 첫차/막차 정보가 addItem으로 전달되는가?
- 🛑 빈 값이면 BusdataUpdate에서 잘못됨

### 4단계: MyAdapter.getView에서 UI 표시 확인
**로그 내용:**
```
getView position: 0 | BusNum: 1 | FirstTime: 06:00 | LastTime: 22:30
getView position: 1 | BusNum: 2 | FirstTime: 06:15 | LastTime: 23:00
```

**확인 사항:**
- ✅ 첫차/막차 정보가 getView에 도달하는가?
- 🛑 "미확인"이 나오면 MyItem에서 데이터 손실

### 5단계: Layout 이슈 확인
**로그 내용:**
```
ERROR: busFirstLastTime TextView is NULL! Layout issue detected.
```

**확인 사항:**
- ✅ 이 에러가 없으면 Layout은 정상
- 🛑 이 에러가 나면 `listview_custom2.xml`에서 `BusFirstLastTime` ID를 확인

---

## 🐛 문제 해결

### 첫차/막차가 "미확인"으로 표시되는 경우
1. `stTm`, `edTm` 필드 존재 확인 (1단계 로그)
2. API 응답의 실제 필드명 확인 (Available keys 로그)
3. 필드명이 다르면 Tab1.java의 파싱 코드 수정 필요

### 텍스트가 전혀 표시되지 않는 경우
1. Layout 에러 확인 (5단계 로그)
2. `listview_custom2.xml`의 `BusFirstLastTime` TextView 확인
3. 리스트 높이가 충분한지 확인 (layout_height가 wrap_content인지)

### 로그에는 데이터가 있지만 UI에 안 보이는 경우
1. 리스트뷰 스크롤 확인 (데이터가 밖에 있을 수 있음)
2. 텍스트 색상 확인 (배경색과 같을 수 있음)
3. layout_height 확인 (텍스트가 가려질 수 있음)

---

## 📊 전체 데이터 흐름

```
API (http://bus.andong.go.kr:8080/api/route/getDataList?type=All)
    ↓
JSONparserLocal() - stTm, edTm 추출
    ↓
XMLparserLocal() - 로그: "BusNum: X | FirstTime: XX:XX | LastTime: XX:XX"
    ↓
BusdataUpdate() - addItem() 호출
    ↓
로그: "addItem called - BusNum: X | FirstTime: XX:XX | LastTime: XX:XX"
    ↓
MyAdapter.getView() - 텍스트 설정
    ↓
로그: "getView position: X | BusNum: X | FirstTime: XX:XX | LastTime: XX:XX"
    ↓
UI 표시: "첫차: XX:XX | 막차: XX:XX"
```

---

## 💡 Tab3 확인

Tab3도 동일한 로그를 남기므로:
```
Tab3 getView position: 0 | BusNum: 1 | FirstTime: 06:00 | LastTime: 22:30
```

로그가 보이면 Tab3도 정상입니다.

