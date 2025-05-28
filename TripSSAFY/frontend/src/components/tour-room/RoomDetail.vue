<template>
  <div class="p-10 space-y-6">
    <RoomHeader
      :selectedRoom="selectedRoom"
      :formatDate="formatDate"
      :editRoom="editRoom"
      :deleteRoomConfirm="deleteRoomConfirm"
    />

    <!-- 카카오 맵 표시 -->
    <div class="relative">
      <!-- 실제 카카오 맵 컨테이너 -->
      <div
        class="kakao-map transition duration-300"
        :class="{ 'blur-sm pointer-events-none': isUILoading }"
        ref="mapContainer"
      ></div>

      <!-- 로딩 오버레이 -->
      <div
        v-if="isUILoading"
        class="absolute inset-0 flex items-center justify-center bg-white bg-opacity-60 z-10"
      >
        <img
          src="@/assets/gifs/loading.gif"
          alt="Loading..."
          class="w-16 h-16"
        />
      </div>

      <div
        v-if="hasTripPlace"
        class="flex justify-between items-center pt-4 px-4"
      >
        <!-- 왼쪽 영역 -->
        <div class="flex flex-col">
          <label class="flex items-center cursor-pointer">
            <span class="text-sm mr-2 text-gray-700">경로 상세</span>
            <input
              type="checkbox"
              v-model="toggleDetailRoute"
              class="sr-only"
              @change="loadMapUI(selectedRoom.tripPlaceList)"
            />
            <div
              class="w-10 h-5 rounded-full relative transition-colors duration-300"
              :class="toggleDetailRoute ? 'bg-ssafy_blue' : 'bg-red-900'"
            >
              <div
                class="w-5 h-5 bg-white rounded-full absolute top-0 left-0 transform transition-transform duration-300"
                :class="{ 'translate-x-5': toggleDetailRoute }"
              ></div>
            </div>
          </label>
          <!-- 예상 거리 텍스트 -->
          <p
            class="text-xs text-gray-500 mt-1 h-4 transition-opacity"
            :class="{
              'opacity-100 visible': toggleDetailRoute && !isUILoading,
              'opacity-0 invisible': !(toggleDetailRoute && !isUILoading),
            }"
          >
            예상 거리: {{ totalLinearDistance }} km
          </p>
        </div>

        <!-- 현재 위치 버튼 -->
        <button
          class="bg-white text-sm border border-gray-300 rounded-full px-2 py-2 shadow hover:bg-gray-100 transition"
          @click="moveToCurrentLocation"
        >
          <img
            src="@/assets/icons/current-location.png"
            alt="현재 위치"
            class="w-5 h-5"
          />
        </button>
      </div>
    </div>

    <!-- 여행 일정 (추후에 RoomTourSchedule.vue로 분리) -->
    <div v-if="hasTripPlace">
      <p class="text-black font-bold text-2xl py-5">여행 일정</p>

      <!-- Day N (YYYY.MM.DD) -->
      <div class="flex items-center gap-2 py-2">
        <p class="text-black text-xl font-semibold">Day {{ currentPage }}</p>
        <p class="text-gray-600 text-base">
          ({{ getDateForDay(currentPage) }})
        </p>
      </div>

      <!-- 장소 리스트 -->
      <div class="relative pl-6 border-l-2 border-gray-300 space-y-4 mb-6">
        <!-- 여행 장소 -->
        <TourSchedule
          :places-for-current-day="placesForCurrentDay"
          @memo="openMemoModal"
          @delete="deleteTourScheduleDetail"
          @changed="handleChangedSchedule"
          @show-review="handleShowReview"
        />
        <!-- 장소 추가 버튼 -->
        <div class="flex justify-between">
          <div>
            <button
              class="text-sm px-4 py-2 bg-blue-100 text-ssafy_blue rounded-md hover:bg-blue-200 transition"
              @click="isTourSpotModalOpen = true"
            >
              장소 추가
            </button>
            <button
              class="text-sm px-4 py-2 bg-purple-100 text-purple-600 rounded-md hover:bg-purple-200 transition ml-2"
              @click="isRecommendModalOpen = true"
            >
              AI 추천
            </button>
          </div>

          <!-- 여행 순서 수정 버튼 -->
          <button
            v-if="isEditButtonVisible"
            class="text-sm px-4 py-2 bg-orange-100 text-orange-600 rounded-md hover:bg-orange-200 transition"
            @click="editScheduleOrder"
          >
            수정
          </button>
        </div>
      </div>

      <div v-if="isMemoModalOpen">
        <MemoModal
          class="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center"
          :spot="selectedSpot"
          @close="isMemoModalOpen = false"
          @saved="updateSelectedRoom"
        />
      </div>
      <div v-if="isTourSpotModalOpen">
        <TourSpotModal
          @close="isTourSpotModalOpen = false"
          @add-spot="addTourSpot"
          :keywords="recommendKeywords"
        />
      </div>

      <div
        v-if="isRecommendModalOpen"
        class="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <RecommendTourModal
          @close="isRecommendModalOpen = false"
          @recommend="handleRecommendTourPlace"
        />
      </div>

      <div
        v-if="isReviewModalOpen"
        class="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <TourSpotReviewModal
          :contentId="spot.contentid"
          :title="spot.title"
          :image-url="spot.firstimage"
          @close="isReviewModalOpen = false"
        />
      </div>

      <!-- 페이지네이션 -->
      <Pagination
        :currentPage="currentPage"
        :totalPages="totalDays"
        @update:currentPage="
          (newPage) => {
            currentPage = newPage;
            isEditButtonVisible = false;
          }
        "
      />
    </div>

    <div v-else>
      <p class="text-black text-center text-2xl py-5">
        아직 여행 일정이 없으시군요~
      </p>
      <p class="text-gray-700 text-xl text-center font-medium py-5">
        지금 여행 일정을 추가하세요!
      </p>
      <button
        @click="createTripPlan"
        class="block mx-auto px-6 py-3 bg-ssafy_blue text-white rounded-xl hover:bg-blue-700 transition font-semibold shadow"
      >
        일정 추가하기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, inject, onMounted, computed, watch } from "vue";
import RoomHeader from "@/components/tour-room/RoomHeader.vue";
import TourSpotModal from "@/components/modal/TourSpotModal.vue";
import Pagination from "@/components/common/Pagination.vue";
import MemoModal from "@/components/modal/MemoModal.vue";
import { useRoomStore } from "@/stores/roomStore";
import api from "@/api/axios";
import TourSchedule from "./TourSchedule.vue";
import RecommendTourModal from "@/components/modal/RecommendTourModal.vue";
import TourSpotReviewModal from "../tour-spot/review/TourSpotReviewModal.vue";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const props = defineProps({
  deleteRoomConfirm: {
    type: Function,
  },
  editRoom: {
    type: Function,
  },
});

const roomStore = useRoomStore();
const selectedRoom = inject("selectedRoom");

const mapContainer = ref(null);
const map = ref(null);

const currentPage = ref(1);
const totalDays = computed(() => getTripDays());

const hasTripPlace = ref(false);
const isTourSpotModalOpen = ref(false);
const isEditButtonVisible = ref(false);
const isReviewModalOpen = ref(false);

const orderIndex = ref(0);

onMounted(() => {
  if (mapContainer.value) {
    loadKakaoMap(mapContainer.value);
  } else {
    console.warn("mapContainer is still null!");
  }

  if (selectedRoom.value.tripPlaceList.length > 0) {
    hasTripPlace.value = true;
    orderIndex.value = selectedRoom.value.tripPlaceList.length;
  } else {
    hasTripPlace.value = false;
  }
});

function formatDate(dateString) {
  const options = { year: "numeric", month: "short", day: "numeric" };
  return new Date(dateString).toLocaleDateString(options);
}

function formatDayToDate(day) {
  const baseDate = new Date(selectedRoom.value.tripRoom.startDate);
  baseDate.setDate(baseDate.getDate() + (day - 1));
  return baseDate.toISOString().split("T")[0];
}

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

function getDateForDay(day) {
  const baseDate = new Date(selectedRoom.value.tripRoom.startDate);
  baseDate.setDate(baseDate.getDate() + (day - 1));
  return formatDate(baseDate.toISOString());
}

const addTourSpot = async (spot) => {
  try {
    console.log(spot);
    const response = await api.post("/place", [
      {
        roomId: selectedRoom.value.tripRoom.id,
        placeId: spot.contentid,
        placeName: spot.title,
        mapX: spot.mapx,
        mapY: spot.mapy,
        visitDate: formatDayToDate(currentPage.value),
        visitOrder: orderIndex.value,
      },
    ]);
    showToastSuccess("장소 추가 성공!");
    orderIndex.value += 1; // 다음 장소 추가를 위해 인덱스 증가
    isTourSpotModalOpen.value = false;
    // 선택한 장소를 trpPlacieList에 추가
    await updateSelectedRoom();

    // 카카오 맵에 마커 표시
    // displayMarker(spot.mapy, spot.mapx, spot.title);
  } catch (error) {
    console.error("Error adding tour spot:", error);
    return;
  }
};

const placesForCurrentDay = computed(() => {
  const currentDate = formatDayToDate(currentPage.value);
  return selectedRoom.value.tripPlaceList
    .filter((place) => place.visitDate === currentDate)
    .sort((a, b) => a.visitOrder - b.visitOrder);
});

const isMemoModalOpen = ref(false);
const openMemoModal = (id) => {
  const spot = selectedRoom.value.tripPlaceList.find(
    (place) => place.id === id
  );
  selectedSpot.value = spot;
  isMemoModalOpen.value = true;
};

const deleteTourScheduleDetail = async (id) => {
  try {
    await api.delete("/place", {
      params: {
        id: id,
      },
    });
    showToastSuccess("장소 삭제 성공!");
    // 선택한 장소를 trpPlacieList에 추가
    await updateSelectedRoom();
  } catch (error) {
    console.error("Error deleting tour spot:", error);
  }
};

const updateSelectedRoom = async () => {
  try {
    await roomStore.fetchTripRoom(selectedRoom.value.tripRoom.id);
    selectedRoom.value = roomStore.tripRoom;
  } catch (error) {
    console.error("Error updating selected room:", error);
  }
};

const changedSchedule = ref([]);
const handleChangedSchedule = (localPlaces) => {
  if (localPlaces === null) {
    isEditButtonVisible.value = false;
    changedSchedule.value = [];
  } else {
    isEditButtonVisible.value = true;
    changedSchedule.value = localPlaces;
  }
};

const isRecommendModalOpen = ref(false);
const recommendKeywords = ref([]);
const handleRecommendTourPlace = (keywords) => {
  isRecommendModalOpen.value = false;
  recommendKeywords.value = keywords;
  isTourSpotModalOpen.value = true;
};

const spot = ref({});
const handleShowReview = async (place) => {
  try {
    const response = await api.get(`/tour-spot/${place.placeId}`);
    spot.value = response.data.response.body.items.item[0];
    isReviewModalOpen.value = true;
  } catch (error) {
    console.error(error);
  }
};

const editScheduleOrder = async () => {
  const places = changedSchedule.value.map((place) => {
    return { id: place.id };
  });
  console.log(places);
  try {
    const response = await api.put("/place", places);
    showToastSuccess("여행 일정 수정 성공!");
    isEditButtonVisible.value = false;
    await updateSelectedRoom();
  } catch (error) {
    console.error("Error editing schedule order:", error);
  }
};

const createTripPlan = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const lat = pos.coords.latitude;
        const lon = pos.coords.longitude;

        // init position
        hasTripPlace.value = true;

        // 위치 받아온 후 setCenter 호출 (지도 로드 이후 보장 필요)
        if (map.value) {
          //setCenter(lat, lon);
          //displayMarker(lat, lon, "현재 위치")
          map.value.setLevel(5);
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

watch(
  [() => selectedRoom.value.tripPlaceList, () => currentPage.value],
  () => {
    if (selectedRoom.value.tripPlaceList.length > 0) {
      hasTripPlace.value = true;
      orderIndex.value = selectedRoom.value.tripPlaceList.length;
    } else {
      hasTripPlace.value = false;
    }
    if (map.value) {
      loadMapUI(selectedRoom.value.tripPlaceList);
    } else {
      console.warn("지도가 아직 초기화되지 않았습니다.");
    }
  },
  { deep: true }
);

const markers = ref([]);
const infoWindows = ref([]);
const selectedSpot = ref(null);
const toggleDetailRoute = ref(false);
const isUILoading = ref(false);

const loadMapUI = async (tripPlaceList) => {
  isUILoading.value = true;

  // 현재 페이지(날짜)에 해당하는 문자열 날짜
  const currentDate = formatDayToDate(currentPage.value);

  // 현재 날짜에 해당하는 장소만 필터링
  const filteredList = tripPlaceList.filter(
    (place) => place.visitDate === currentDate
  );

  if (markers.value.length > 0 && infoWindows.value.length > 0) {
    // 기존 맵 UI 제거
    markers.value.forEach((marker) => marker.setMap(null));
    infoWindows.value.forEach((iw) => iw.close());
    if (polyline.value !== null) {
      polyline.value.setMap(null);
    }
  }

  markers.value = [];
  infoWindows.value = [];
  // 마커 표시 함수
  loadMarkers(filteredList);

  if (filteredList.length >= 2) {
    // 경로 그리기 함수

    if (toggleDetailRoute.value) {
      // 실제 자동차 경로 계산
      const response = await getCarDirection(filteredList);
      drawCarPolyline(response);
    } else {
      drawPolyline(filteredList);
    }
    // 거리 계산
    totalLinearDistance.value = Math.round(polyline.value.getLength() * 0.001); // m -> km
  }

  isUILoading.value = false;
};

const bounds = ref([]);
const loadMarkers = (places) => {
  bounds.value = new kakao.maps.LatLngBounds();

  places.forEach((spot) => {
    const position = new kakao.maps.LatLng(spot.placeMapy, spot.placeMapx);
    const marker = new kakao.maps.Marker({ position });
    marker.setMap(map.value);

    const iwContent = `
      <div style="padding:10px; font-size:16px;">
        <strong>${spot.placeName}</strong><br/>
        <p>${spot.memo ? spot.memo : ""}</p>
      </div>
    `;

    const infowindow = new kakao.maps.InfoWindow({
      content: iwContent,
    });

    kakao.maps.event.addListener(marker, "click", () => {
      // 다른 인포윈도우 닫기
      infoWindows.value.forEach((iw) => iw.close());
      infowindow.open(map.value, marker);
    });

    markers.value.push(marker);
    infoWindows.value.push(infowindow);
    bounds.value.extend(position);
  });

  if (!bounds.value.isEmpty()) {
    map.value.setBounds(bounds.value);
  }
};

const polyline = ref(null);
const totalLinearDistance = ref(0);
const drawPolyline = (places) => {
  const linePath = places.map((spot) => {
    return new kakao.maps.LatLng(spot.placeMapy, spot.placeMapx);
  });

  console.log("직선 거리 경로:", linePath);

  polyline.value = new kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 5,
    strokeColor: "#FF0000",
    strokeOpacity: 0.7,
    strokeStyle: "solid",
  });

  polyline.value.setMap(map.value);
};

const drawCarPolyline = (response) => {
  const linePath = [];

  const routes = response.routes;
  if (!routes || routes.length === 0) {
    console.error("No routes found in the response.");
    return;
  }

  // 전체 좌표 수 파악
  let totalVertexCount = 0;
  routes[0].sections.forEach((section) => {
    section.roads.forEach((road) => {
      totalVertexCount += road.vertexes.length / 2; // 위도, 경도 쌍이므로 / 2
    });
  });

  // 샘플링 기준 설정
  const desiredMaxPoints = 300; // 최대로 찍을 점 개수 (예: 300개)
  const rawSamplingRate = Math.ceil(totalVertexCount / desiredMaxPoints);
  const samplingRate = Math.max(rawSamplingRate, 1); // 최소 1 보장

  console.log(
    `총 좌표쌍 수: ${totalVertexCount}, 샘플링 간격: ${samplingRate}`
  );

  routes[0].sections.forEach((section) => {
    section.roads.forEach((road) => {
      const rawVertexes = JSON.parse(JSON.stringify(road.vertexes));

      for (let i = 0; i < rawVertexes.length; i += 2 * samplingRate) {
        const lng = rawVertexes[i];
        const lat = rawVertexes[i + 1];
        if (typeof lat === "number" && typeof lng === "number") {
          linePath.push(new kakao.maps.LatLng(lat, lng));
        }
      }

      // 마지막 좌표 꼭 추가
      const lastLng = rawVertexes[rawVertexes.length - 2];
      const lastLat = rawVertexes[rawVertexes.length - 1];
      if (typeof lastLat === "number" && typeof lastLng === "number") {
        linePath.push(new kakao.maps.LatLng(lastLat, lastLng));
      }
    });
  });

  const removeDuplicatePoints = (points) => {
    const seen = new Set();
    return points.filter((p) => {
      const key = `${p.getLat().toFixed(6)}-${p.getLng().toFixed(6)}`;
      if (seen.has(key)) return false;
      seen.add(key);
      return true;
    });
  };

  const optimizedPath = removeDuplicatePoints(linePath);

  polyline.value = new kakao.maps.Polyline({
    path: optimizedPath,
    strokeWeight: 5,
    strokeColor: "#3A7AFE",
    strokeOpacity: 0.7,
    strokeStyle: "solid",
  });

  polyline.value.setMap(map.value);
};

const getCarDirection = async (places) => {
  const url = "https://apis-navi.kakaomobility.com/v1/directions";
  const headers = {
    Authorization: `	KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
    "Content-Type": "application/json",
  };

  const origin = `${places[0].placeMapx},${places[0].placeMapy}`;
  const destination = `${places[places.length - 1].placeMapx},${
    places[places.length - 1].placeMapy
  }`;
  const waypoints = places.slice(1, -1).map((place) => {
    return `${place.placeMapx},${place.placeMapy}`;
  });

  const params = new URLSearchParams({
    origin: origin,
    destination: destination,
    waypoints: waypoints.join("|"),
  });

  try {
    const response = await fetch(`${url}?${params}`, {
      method: "GET",
      headers: headers,
    });

    return await response.json();
  } catch (error) {
    console.error("Error fetching car direction:", error);
  }
};

const moveToCurrentLocation = () => {
  if (!bounds.value.isEmpty()) {
    map.value.setBounds(bounds.value);
  }
};

const panTo = (lat, lon) => {
  const moveLatLon = new kakao.maps.LatLng(lat, lon);

  map.value.panTo(moveLatLon);
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
      // 만약 여행 일정이 존재하면
      const initPlaceList = selectedRoom.value.tripPlaceList;
      if (initPlaceList.length > 0) {
        loadMapUI(initPlaceList);
      }

      if (map.value) {
        kakao.maps.event.addListener(map.value, "click", () => {
          infoWindows.value.forEach((iw) => iw.close());
        });
      }
    });
  };
};

const handleMarkerClickEvent = (spot) => {
  const position = new kakao.maps.LatLng(spot.placeMapy, spot.placeMapx);
  map.value.panTo(position);

  // 해당 spot에 맞는 마커 인포윈도우 열기
  const targetMarker = markers.value.find((m, idx) => {
    const latlng = m.getPosition();
    return (
      latlng.getLat() === spot.placeMapy && latlng.getLng() === spot.placeMapx
    );
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
  width: 100%;
  height: 400px;
  border: 0.25px solid #0f0f0f3b;
}
</style>
