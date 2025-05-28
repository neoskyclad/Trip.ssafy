<!-- 나중에 모달 분리할 것!! -->

<template>
  <!-- 카카오 맵 표시 -->
  <div class="kakao-map" ref="mapContainer"></div>

  <p class="text-black font-bold text-2xl py-5">여행 일정</p>
  <!-- 일자 pagination -->
  <p class="text-black text-xl py-5">Day {{ currentPage }}</p>
  <div v-for="place in getPlacesForDay(currentPage)" :key="place.id">
    <!-- 각 일자별 tripPlace 출력 (테이블로) -->
    <div>{{ place.name }}</div>
  </div>
  <button
    class="px-6 py-3 bg-ssafy_blue text-white rounded-xl hover:bg-blue-700 transition font-semibold shadow"
  >
    장소 추가
  </button>
  <Pagination
    :currentPage="currentPage"
    :totalPages="totalDays"
    @update:currentPage="(newPage) => (currentPage = newPage)"
  />
</template>

<script setup>
import { showToastError } from "@/api/toastMsg";
import Pagination from "@/components/common/Pagination.vue";
import { ref, onMounted, computed, inject, defineExpose } from "vue";

const selectedRoom = inject("selectedRoom");

const mapContainer = ref(null);
const map = ref(null);

const currentPage = ref(1);
const totalDays = computed(() => getTripDays());

onMounted(() => {
  if (mapContainer.value) {
    loadKakaoMap(mapContainer.value);
  } else {
    console.warn("mapContainer is still null!");
  }
});

defineExpose({
  initTripPlan,
});

function getTripDays() {
  if (
    !selectedRoom.value?.tripRoom?.startDate ||
    !selectedRoom.value?.tripRoom?.endDate
  )
    return 0;
  const start = new Date(selectedRoom.value.tripRoom.startDate);
  const end = new Date(selectedRoom.value.tripRoom.endDate);
  // 양 끝 포함
  return Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
}

function getPlacesForDay(day) {
  // tripPlaceList의 각 place에 day(1부터 시작) 필드가 있다고 가정
  // 만약 없다면, 날짜별로 매핑하는 로직을 추가해야 함
  return selectedRoom.value.tripPlaceList.filter((place) => place.day === day);
}

const initTripPlan = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const lat = pos.coords.latitude;
        const lon = pos.coords.longitude;

        // init position
        selectedRoom.value.tripPlaceList.push({
          latitude: lat,
          longitude: lon,
        });

        // 위치 받아온 후 setCenter 호출 (지도 로드 이후 보장 필요)
        if (map.value) {
          //setCenter(lat, lon);
          //displayMarker(lat, lon, "현재 위치")
          map.value.setLevel(5);

          // 지도는 보기 전용
          map.value.setDraggable(false); // 드래그 방지
          map.value.setZoomable(false); // 줌 방지
          panTo(lat, lon);
        } else {
          console.warn("지도 로딩이 아직 안 됐습니다. 위치 이동은 생략합니다.");
        }
      },
      (err) => {
        console.error("위치 정보를 가져오는데 실패했습니다:", err);
      }
    );
  } else {
    showToastError("이 브라우저는 위치 정보를 지원하지 않습니다.");
  }
};

const setCenter = (lat, lon) => {
  if (!map.value) {
    console.warn("map이 초기화되지 않았습니다.");
    return;
  }
  const moveLatLon = new kakao.maps.LatLng(lat, lon);
  console.log(lat, lon);
  map.value.setCenter(moveLatLon);
};

const panTo = (lat, lon) => {
  const moveLatLon = new kakao.maps.LatLng(lat, lon);

  map.value.panTo(moveLatLon);
};

const displayMarker = (lat, lon, message) => {
  // 마커를 생성합니다

  const locPosition = new kakao.maps.LatLng(lat, lon);

  const marker = new kakao.maps.Marker({
    position: locPosition,
  });

  marker.setMap(map.value);

  const iwContent = `
      <div style="padding:10px; font-size:16px;">
        <strong>${message}</strong><br/>
      </div>
    `; // 인포윈도우에 표시할 내용

  // 인포윈도우를 생성합니다
  const infowindow = new kakao.maps.InfoWindow({
    content: iwContent,
    removable: true,
  });

  // 인포윈도우를 마커위에 표시합니다
  infowindow.open(map.value, marker);
};

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
    const initPlaceList = selectedRoom.tripPlaceList;
    if (initPlaceList) {
      panTo(initPlaceList[0].latitude, initPlaceList[0].long);
    }

    if (map.value) {
      kakao.maps.event.addListener(map.value, "click", () => {
        infoWindows.value.forEach((iw) => iw.close());
      });
    }
  };
};
</script>

<style scoped>
.kakao-map {
  width: 100%;
  height: 400px;
  border: 0.25px solid #0f0f0f3b;
}
</style>
