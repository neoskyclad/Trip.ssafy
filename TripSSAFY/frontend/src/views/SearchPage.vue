<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <div class="px-32 py-8">
      <h1 class="text-4xl font-bold text-center">광고</h1>

      <div class="my-8">
        <h1
          class="flex justify-center text-xl mb-4 border-b-2 text-gray-500 pb-1"
        >
          숙소
        </h1>
        <div class="bg-white rounded-2xl shadow-md px-6 py-4">
          <PromotionList :content-id="1" />
        </div>
      </div>

      <div class="my-12">
        <h1
          class="flex justify-center text-xl mb-4 border-b-2 text-gray-500 pb-1"
        >
          항공권
        </h1>
        <div class="bg-white rounded-2xl shadow-md px-6 py-4">
          <PromotionList :content-id="2" />
        </div>
      </div>

      <div class="my-8">
        <h1
          class="flex justify-center text-xl mb-4 border-b-2 text-gray-500 pb-1"
        >
          여행 상품
        </h1>
        <div class="bg-white rounded-2xl shadow-md px-6 py-4">
          <PromotionList :content-id="3" />
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import api from "@/api/axios";
import PromotionList from "@/components/promotion/PromotionList.vue";
import { showToastError } from "@/api/toastMsg";

const mapContainer = ref(null);

const areaCode = ref("");
const areaCodeList = ref([]);
const sigunguCode = ref("");
const sigunguCodeList = ref([]);
const searchKeyword = ref("");
const pageNo = ref(1);
const spotList = ref([]);
const totalPages = ref(0);

onMounted(async () => {
  loadKakaoMap(mapContainer.value);

  try {
    const response = await api.get("/tour-spot/area-code");

    areaCodeList.value = response.data.response.body.items.item;
  } catch (error) {
    console.log("area-code 불러오기 실패: ", error);
  }
});

const handleAreaCodeChange = async (e) => {
  try {
    const response = await api.get(`/tour-spot/sigungu-code/${areaCode.value}`);
    sigunguCodeList.value = response.data.response.body.items.item;
  } catch (error) {
    console.log("sigungu-code 불러오기 실패: ", error);
  }
};

const handleSearch = async (e) => {
  if (searchKeyword.value.trim() === "") {
    showToastError("검색어를 입력하세요.");
    return;
  }
  try {
    const request = {
      areaCode: areaCode.value,
      sigunguCode: sigunguCode.value,
      keyword: searchKeyword.value,
      numOfRows: 5,
      pageNo: pageNo.value,
    };
    const response = await api.get("/tour-spot/search", {
      params: request,
    });

    spotList.value = response.data.response.body.items.item;
    console.log(spotList.value);
    //카카오 맵 마커 찍기
    createMarkers();
    let totalCount = response.data.response.body.totalCount;
    totalPages.value = Math.ceil(totalCount / 5);
  } catch (err) {
    spotList.value = [];
    console.log(err);
  }
};

//카카오 맵, 마커 관련 script
const map = ref(null);
const markers = ref([]);
const infoWindows = ref([]);
const loadKakaoMap = (container) => {
  const script = document.createElement("script");
  script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${
    import.meta.env.VITE_KAKAO_JAVASCRIPT_API_KEY
  }&autoload=false`;
  document.head.appendChild(script);

  script.onload = () => {
    window.kakao.maps.load(() => {
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };

      map.value = new window.kakao.maps.Map(container, options);
    });
  };
};
const createMarkers = () => {
  // 기존 마커 및 인포윈도우 제거
  markers.value.forEach((marker) => marker.setMap(null));
  infoWindows.value.forEach((iw) => iw.close());

  markers.value = [];
  infoWindows.value = [];
  const bounds = new kakao.maps.LatLngBounds(); // ✅ 추가

  spotList.value.forEach((spot) => {
    const position = new kakao.maps.LatLng(spot.mapy, spot.mapx);
    const marker = new kakao.maps.Marker({ position });
    marker.setMap(map.value);

    const iwContent = `
      <div style="padding:10px; font-size:16px;">
        <strong>${spot.title}</strong><br/>
        ${spot.addr1}
      </div>
    `;
    const infowindow = new kakao.maps.InfoWindow({
      content: iwContent,
    });

    kakao.maps.event.addListener(marker, "click", () => {
      infowindow.open(map.value, marker);
    });

    markers.value.push(marker);
    infoWindows.value.push(infowindow);

    bounds.extend(position); // ✅ 각 마커 위치를 bounds에 추가
  });
  if (!bounds.isEmpty()) {
    map.value.setBounds(bounds); // ✅ 모든 마커가 포함되도록 지도 범위 자동 조정
  }
  //set level
};

const moveToSpot = (spot) => {
  const position = new kakao.maps.LatLng(spot.mapy, spot.mapx);
  console.log("position: " + position);
  map.value.setCenter(position);
  map.value.setLevel(3);

  // 해당 spot에 맞는 마커 인포윈도우 열기
  const targetMarker = markers.value.find((m, idx) => {
    const latlng = m.getPosition();
    return latlng.getLat() === spot.mapy && latlng.getLng() === spot.mapx;
  });

  const targetInfo = infoWindows.value[markers.value.indexOf(targetMarker)];
  if (targetInfo && targetMarker) {
    infoWindows.value.forEach((iw) => iw.close()); // 모든 창 닫고
    targetInfo.open(map.value, targetMarker);
  }
};
</script>

<style scoped>
.kakao-map {
  flex: 1; /* 부모 flex 컨테이너 기준으로 확장 */
  width: 100%;
  min-height: 600px; /* 너무 작아지지 않게 */
  border: 0.25px solid #0f0f0f3b;
}
select {
  width: 200px;
  height: 40px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px; /* 둥근 테두리 */
  font-size: 16px;
  /* appearance: none; 브라우저 기본 스타일 제거 */
  background-color: white;
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 10px;
}

/* 옵션 목록 스타일링 (Chrome, Edge, Opera 등 지원) */
select option {
  padding: 8px;
}
</style>
