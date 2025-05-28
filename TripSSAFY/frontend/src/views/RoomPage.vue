<template>
  <div class="flex min-h-screen">
    <LeftSidebar
      :triproom="triproom"
      :handleRoomDetail="handleRoomDetail"
      :selectedRoom="selectedRoom"
    />

    <MainContent class="flex-1" @update-room="fetchTripRoom" />

    <RightSidebar :isCollapsed="rightCollapsed" />
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/userStore";
import { useRoomStore } from "@/stores/roomStore";
import { ref, onMounted, provide, watch } from "vue";
import api from "@/api/axios";

import LeftSidebar from "@/components/tour-room/LeftSidebar.vue";
import MainContent from "@/components/tour-room/MainContent.vue";
import RightSidebar from "@/components/tour-room/RightSidebar.vue";
import { showToastError } from "@/api/toastMsg";

const router = useRouter();

const rightCollapsed = ref(true);
provide("rightCollapsed", rightCollapsed);

const userStore = useUserStore();
provide("userStore", userStore);
const roomStore = useRoomStore();

const triproom = ref([]);
const selectedRoom = ref(null);
provide("selectedRoom", selectedRoom);
onMounted(async () => {
  if (!userStore.isLoggedIn) {
    showToastError("로그인 후 이용해주세요.");
    setTimeout(() => {
      router.push("/");
    }, 1500);
  }
  // 유저가 속한 여행 방 불러오기
  try {
    fetchTripRoom();
  } catch (error) {
    console.log("여행 방 불러오기 실패: ", error);
  }
});

const fetchTripRoom = async () => {
  const response = await api.get("/room");
  triproom.value = response.data;
};

async function handleRoomDetail(id) {
  if (selectedRoom.value != null && selectedRoom.value.tripRoom.id === id) {
    return;
  }
  try {
    await roomStore.fetchTripRoom(id);
    selectedRoom.value = roomStore.tripRoom;
    rightCollapsed.value = false;
    console.log("방 상세 조회 성공:", selectedRoom.value);
  } catch (error) {
    console.error("방 상세 조회 실패:", error);
  }
}
</script>
