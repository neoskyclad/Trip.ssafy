<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
  >
    <div
      class="bg-white rounded-xl shadow-lg w-4/5 h-[90vh] flex flex-col px-6 py-4 overflow-y-scroll"
    >
      <!-- 모달 헤더 -->
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-semibold">여행 장소 추가</h2>
        <button class="text-gray-500 hover:text-black" @click="$emit('close')">
          ✕
        </button>
      </div>

      <!-- 필터 -->
      <div class="flex gap-4 mb-4">
        <select
          class="custom"
          v-model="areaCode"
          @change="handleAreaCodeChange"
        >
          <option value="">지역 선택</option>
          <option
            v-for="area in areaCodeList"
            :key="area.rnum"
            :value="area.code"
          >
            {{ area.name }}
          </option>
        </select>

        <select class="custom" v-model="sigunguCode">
          <option value="">시군구 선택</option>
          <option
            v-for="sigungu in sigunguCodeList"
            :key="sigungu.rnum"
            :value="sigungu.code"
          >
            {{ sigungu.name }}
          </option>
        </select>

        <div
          class="flex items-center border border-gray-300 rounded-lg px-4 py-2 bg-white w-1/2 relative"
          ref="searchBoxRef"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="w-5 h-5 text-gray-400 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1010.5 18a7.5 7.5 0 006.15-3.35z"
            />
          </svg>
          <input
            type="text"
            placeholder="검색어를 입력하세요"
            v-model="searchKeyword"
            @click="isSearchDropdownOpen = true"
            @keyup.enter="handleSearch"
            class="w-full outline-none text-sm text-gray-700 bg-transparent"
          />
          <div
            v-if="isSearchDropdownOpen && gptKeywords.length > 0"
            class="absolute mt-32 w-1/2 bg-white border border-purple-300 rounded-lg shadow z-10"
          >
            <ul>
              <li
                v-for="(keyword, index) in gptKeywords"
                :key="index"
                class="px-4 py-2 text-purple-700 hover:bg-purple-100 cursor-pointer text-sm"
                @click="selectGPTKeyword(keyword)"
              >
                {{ keyword }}
              </li>
            </ul>
          </div>
        </div>

        <button
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
          @click="handleSearch"
        >
          검색
        </button>
      </div>

      <!-- 지도 + 검색결과 (좌우 분할) -->
      <div class="flex flex-1 gap-4 mb-4">
        <!-- 지도 -->
        <div class="w-2/3 flex flex-col justify-between">
          <div class="kakao-map flex-1" ref="mapContainer"></div>
          <div class="flex justify-end gap-4 mt-2">
            <button
              v-if="selectedSpot"
              class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
              @click="handleAddSpot"
            >
              선택한 장소 추가
            </button>
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

        <!-- 검색 결과 -->
        <div class="w-1/3 relative overflow-y-auto h-full text-left">
          <h2 class="text-2xl font-semibold mt-4 pt-2 pb-2 px-2 text-left">
            검색 결과
          </h2>
          <div class="flex flex-col gap-2 px-2 text-left">
            <div
              v-if="isLoading"
              class="absolute inset-0 flex items-center justify-center bg-white bg-opacity-60 z-10"
            >
              <img
                src="@/assets/gifs/loading.gif"
                alt="Loading..."
                class="w-16 h-16"
              />
            </div>

            <template v-if="spotList.length > 0">
              <div class="text-left">
                <TourSpotItem
                  v-for="spot in spotList"
                  :key="spot.contentid"
                  :spotItem="spot"
                  @click="moveToSpot(spot)"
                  class="text-left"
                />
              </div>
              <Pagination
                :currentPage="pageNo"
                :totalPages="totalPages"
                @update:currentPage="
                  (newPage) => {
                    pageNo = newPage;
                    handleSearch();
                  }
                "
                class="pb-4 text-left"
              />
            </template>
            <template v-else>
              <div class="pt-8 px-2 text-gray-500 text-left">
                검색 결과가 없습니다.
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>

    <AddPlaceModal
      v-if="isAddPlaceModalOpen"
      :latlng="clickedLatLng"
      @close="isAddPlaceModalOpen = false"
      @submit="handleAddCustomSpot"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, defineEmits, onBeforeUnmount } from "vue";
import api from "@/api/axios";
import AddPlaceModal from "./AddPlaceModal.vue";
import TourSpotItem from "../tour-spot/TourSpotItem.vue";
import Pagination from "../common/Pagination.vue";
import { showToastError } from "@/api/toastMsg";

const mapContainer = ref(null);

const areaCode = ref("");
const areaCodeList = ref([]);
const sigunguCode = ref("");
const sigunguCodeList = ref([]);
const searchKeyword = ref("");

const spotList = ref([]);
const markers = ref([]);
const infoWindows = ref([]);
const map = ref(null);

const pageNo = ref(1);
const totalPages = ref(0);

const selectedSpot = ref(null);
const isSearchDropdownOpen = ref(false);
const gptKeywords = ref([]);

const isLoading = ref(false);

const emit = defineEmits(["add-spot"]);

const props = defineProps({
  keywords: Array,
});

onMounted(async () => {
  loadKakaoMap(mapContainer.value);

  try {
    const response = await api.get("/tour-spot/area-code");
    areaCodeList.value = response.data.response.body.items.item;
  } catch (error) {
    console.error("지역 코드 불러오기 실패", error);
  }

  if (props.keywords.length > 0) {
    gptKeywords.value = props.keywords;
    isSearchDropdownOpen.value = true;
  } else {
    gptKeywords.value = null;
    isSearchDropdownOpen.value = false;
  }

  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

const handleAreaCodeChange = async () => {
  try {
    const response = await api.get(`/tour-spot/sigungu-code/${areaCode.value}`);
    sigunguCodeList.value = response.data.response.body.items.item;
  } catch (error) {
    console.error("시군구 코드 불러오기 실패", error);
  }
};

const handleSearch = async () => {
  isLoading.value = true;
  isSearchDropdownOpen.value = false;

  if (!searchKeyword.value.trim()) {
    showToastError("검색어를 입력하세요.");
    return;
  }

  try {
    const request = {
      areaCode: areaCode.value,
      sigunguCode: sigunguCode.value,
      keyword: searchKeyword.value,
      numOfRows: 4,
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
  isLoading.value = false;
};

const searchBoxRef = ref(null);

const selectGPTKeyword = (keyword) => {
  searchKeyword.value = keyword;
  isSearchDropdownOpen.value = false;
};

// 모달 외부 클릭 감지 이벤트 핸들러
const handleClickOutside = (event) => {
  if (searchBoxRef.value && !searchBoxRef.value.contains(event.target)) {
    isSearchDropdownOpen.value = false;
  }
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
        center: new kakao.maps.LatLng(37.5665, 126.978), // 서울 중심
        level: 7,
      };
      map.value = new kakao.maps.Map(container, options);
    });

    if (map.value) {
      kakao.maps.event.addListener(map.value, "click", function (mouseEvent) {
        infoWindows.value.forEach((iw) => iw.close());
        selectedSpot.value = null;
        handleClickLocation(mouseEvent);
      });

      kakao.maps.event.addListener(
        map.value,
        "rightclick",
        function (mouseEvent) {
          overlay.setMap(null);
          marker.value.setMap(null);
          isOverlayOpen.value = false;
        }
      );
    }
  };
};

const createMarkers = () => {
  markers.value.forEach((m) => m.setMap(null));
  infoWindows.value.forEach((iw) => iw.close());
  markers.value = [];
  infoWindows.value = [];

  const bounds = new kakao.maps.LatLngBounds();

  spotList.value.forEach((spot) => {
    const position = new kakao.maps.LatLng(spot.mapy, spot.mapx);
    const marker = new kakao.maps.Marker({ position });
    marker.setMap(map.value);

    const content = `
      <div style="width:300px; padding:10px;">
      <div style="font-weight:bold; font-size:16px; margin-bottom:4px;">
        ${spot.title}
      </div>
      <div style="margin-bottom:4px;">${spot.addr1}</div>
      ${
        spot.firstimage
          ? `<img src="${spot.firstimage}" style="width:100%; height:150px; object-fit:cover; border-radius:6px; margin-bottom:4px;" />`
          : ""
      }
      <div style="color:#444; font-size:13px;">${
        spot.overview ?? ""
      }</div></div>
    `;
    const infoWindow = new kakao.maps.InfoWindow({ content });

    kakao.maps.event.addListener(marker, "click", () => {
      selectedSpot.value = spot;
      console.log("Selected Spot:", selectedSpot.value);
      infoWindows.value.forEach((iw) => iw.close());
      infoWindow.open(map.value, marker);
    });

    markers.value.push(marker);
    infoWindows.value.push(infoWindow);
    bounds.extend(position);
  });

  if (!bounds.isEmpty()) {
    map.value.setBounds(bounds);
  }
};

const isAddPlaceModalOpen = ref(false);
const isOverlayOpen = ref(false);
const clickedLatLng = ref(null);
const marker = ref(null);
let overlay = null;

const handleClickLocation = (mouseEvent) => {
  if (isOverlayOpen.value) {
    return;
  }

  const latlng = mouseEvent.latLng;
  clickedLatLng.value = latlng;

  // 기존 오버레이 제거
  if (overlay) {
    overlay.setMap(null);
    isOverlayOpen.value = false;
  }

  // 오버레이 생성
  const content = document.createElement("div");
  content.innerHTML = `
<div
  style="
    background: white;
    border: 1px solid #ccc;
    padding: 10px;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  "
>
  <p style="margin: 0 0 8px 0; font-size: 12px; color: #888;">우클릭으로 닫기</p>
  <p style="margin-bottom: 8px;">여행 장소를 추가하시겠습니까?</p>
  <button
    id="addPlaceBtn"
    style="
      background: #3A7AFE;
      color: white;
      border: none;
      padding: 5px 10px;
      border-radius: 4px;
      cursor: pointer;
    "
  >
    여행 장소 추가
  </button>
</div>

  `;

  // 이벤트 등록
  content.querySelector("#addPlaceBtn").addEventListener("click", (event) => {
    event.stopPropagation(); // 클릭 이벤트 버블링 차단
    isAddPlaceModalOpen.value = true;
  });

  // 마커 인스턴스 생성
  marker.value = new kakao.maps.Marker({
    position: latlng,
  });
  marker.value.setMap(map.value);

  // 오버레이 인스턴스 생성
  overlay = new kakao.maps.CustomOverlay({
    content: content,
    position: latlng,
    xAnchor: 0.5,
    yAnchor: 1.2,
  });

  overlay.setMap(map.value);

  isOverlayOpen.value = true;
};

const handleAddSpot = () => {
  if (!selectedSpot.value) {
    showToastError("장소를 선택하세요!");
    return;
  }
  // 부모 컴포넌트로 이벤트 전달
  emit("add-spot", selectedSpot.value);
  selectedSpot.value = null;
};

const handleAddCustomSpot = (spot) => {
  emit("add-spot", spot);
};

const currentLocationMarker = ref({});
const moveToCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        const locPosition = new kakao.maps.LatLng(lat, lng);

        map.value.setCenter(locPosition);
        map.value.setLevel(3);

        // 선택적으로 현재 위치에 마커 추가
        currentLocationMarker.value = new kakao.maps.Marker({
          position: locPosition,
        });
        currentLocationMarker.value.setMap(map.value);
      },
      (error) => {
        showToastError("현재 위치를 가져올 수 없습니다.");
        console.error(error);
      }
    );
  } else {
    showToastError("이 브라우저는 Geolocation을 지원하지 않습니다.");
  }
};

const moveToSpot = (spot) => {
  const position = new kakao.maps.LatLng(spot.mapy, spot.mapx);
  console.log("position: " + position);
  map.value.setCenter(position);
  map.value.setLevel(3);
  selectedSpot.value = spot;

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
  width: 100%;
  height: 500px;
  border-radius: 12px;
}

select {
  width: 180px;
  height: 40px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  background-color: white;
}
</style>
