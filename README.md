# 요소수 어디 
* 해당 앱에서 제공하는 데이터는 '환경부' 에서 제공받으며 이는 공공데이터 포털에서 제공하는 API 를 사용하여 가져옵니다.
* 해당 앱은 광고가 없습니다.
* [구글 플레이스토어 '요소수 여기'](https://play.google.com/store/apps/details?id=com.turtle.yososuwhere)
---
### 기능
* 내 위치에서 가장 가까운 순서대로 목록에 출력
* 요소수 재고량 확인
---
### 업데이트 필요한 기능
* 맵 상에 마커로 요소수 재고량 뿌리기
* 검색 지원

---
# 버전
### v1.0
<img src = "https://user-images.githubusercontent.com/51182964/143018335-07516977-ebcd-484c-9ed6-58b89cdf17f8.png" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/51182964/143018401-f566df54-b87f-41b4-ad9d-c933ca06bfee.png" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/51182964/143018413-4305ef29-13e0-4a01-b041-aa2974f34fd4.png" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/51182964/143018424-cc190b42-73e2-4604-ad8c-be985e7bcb41.png" width="20%" height="20%">
---
### v1.0.0.1
* [공공데이터활용지원센터_요소수 중점 유통 주유소 재고현황] API 폐기 --> 환경부_요소수 중점 유통 주유소 재고현황 조회서비스 API 로 변경에 따른 앱 수정 필요
* 요소수 데이터 모델 변경 및 요소수 정보 추가
  * 가격
  * 데이터 시간
  * 잔량 수량 구간 (신호등 표시)
* 기능 추가
  * 요소수 재고 없는 곳 제외하여 보기

<img src = "https://user-images.githubusercontent.com/51182964/143728126-2d81094b-6e4f-4fbe-8f29-49daac14c7df.png" width="20%" height="20%"> <img src = "https://user-images.githubusercontent.com/51182964/143728125-754f43ca-d66d-49bf-a569-8ddc5f937881.png" width="20%" height="20%"> <img src = "https://user-images.githubusercontent.com/51182964/143728122-ad360992-1a31-4326-9289-73e8630267fd.png" width="20%" height="20%">


---
### v1.0.0.2
* 네이버 지도로 요소수 보기 / 필터링

<img src = "https://user-images.githubusercontent.com/51182964/144257135-d2fde504-ea22-45e1-bf0a-dab8163b9b50.png" width="20%" height="20%"> <img src = "https://user-images.githubusercontent.com/51182964/144257157-8158954c-2bc1-4c8e-b499-f4f05b9be9d8.png" width="20%" height="20%"> <img src = "https://user-images.githubusercontent.com/51182964/144257159-c5d5625e-4509-4d28-9102-4e2d1132bd11.png" width="20%" height="20%">

---
### v1.0.0.5
* 최초 주유소 개수는 100개 였으나 전국 주유소 개수를 고려하면 최대 12000개 까지 늘어날수 있어서 데이터 표시 및 요청 로직 변경
  * 맵에 표시되는 마커 20 개로 제한
  * 맵에 표시하지 않아도 되는 마커는 제거
  * API 요청시 400개 씩 나누어서 받아오기
    * 사용자가 얼마나 로딩되었는지 또는 로딩되었는지 상단 LinearProgressBar 로 표시
  * 요소수 리스트 페이징 적용
    * 10 페이지씩 불러오기
  * 요소수 맵을 메인 화면으로 변경
